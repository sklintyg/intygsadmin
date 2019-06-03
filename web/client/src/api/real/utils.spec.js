import *  as utils from "./utils";
import fetchMock from 'fetch-mock'

describe('api utils test', () => {

  afterEach(() => {
    fetchMock.restore()
  })

  describe('buildUrlFromParams', () => {
    const path = 'path';

    it('empty state', () => {
      const result = utils.buildUrlFromParams(path, {});

      expect(result).toEqual(path + '')
    })

    it('no parameters state', () => {
      const result = utils.buildUrlFromParams(path);

      expect(result).toEqual(path)
    })

    it('don\'t include empty parameter values', () => {
      const state = {
        page: '1',
        sort: 'p',
        value: '',
        nullValue: null
      };
      const result = utils.buildUrlFromParams(path, state);

      expect(result).toEqual(`${path}?page=1&sort=p`)
    })
  })

  describe('handleResponse', () => {

    describe('success', () => {
      const reponse = {
        ok: true,
        json: () => ('json')
      };

      it('empty config', () => {
        const result = utils.handleResponse({})(reponse);

        expect(result).toEqual('json')
      })

      it('no config', () => {
        const result = utils.handleResponse()(reponse);

        expect(result).toEqual('json')
      })

      it('config, noBody', () => {
        const result = utils.handleResponse({emptyBody: true})(reponse);

        expect(result).toEqual({})
      })
    });

    const methods = [ {
      method: 'makeServerRequestTest',
      fetch: 'getOnce'
    }, {
      method: 'makeServerPost',
      fetch: 'postOnce'
    }, {
      method: 'makeServerPut',
      fetch: 'putOnce'
    }, {
      method: 'makeServerDelete',
      fetch: 'deleteOnce'
    }];

    utils.makeServerRequestTest = (path, body, config) => utils.makeServerRequest(path, config)

    methods.map(method => {
      const path = 'path:/api/test'

      describe(method.method, () => {
        it('success', (done) => {
          fetchMock[method.fetch](path, {
            body: {
              name: 'test'
            },
            headers: { 'content-type': 'application/json' }
          })

          utils[method.method]('test', {}).then((response) => {
            expect(response.name).toEqual('test');

            done();
          });
        });

        it('success noBody', (done) => {
          fetchMock[method.fetch](path, {
            headers: { 'content-type': 'application/json' }
          })

          utils[method.method]('test', {}, {emptyBody: true}).then((response) => {
            expect(response).toEqual({});

            done();
          });
        });

        it('error - network problems', (done) => {
          fetchMock[method.fetch](path, {
            throws: {message: 'failed'}
          })

          utils[method.method]('test', {}).then().catch((response) => {
            const error = {
              statusCode: -1,
              error: {
                errorCode: 'NETWORK_ERROR',
                message: {message: 'failed'},
                logId: null
              }
            };

            expect(response).toEqual(error);

            done();
          });
        });

        it('error - from server', (done) => {
          fetchMock[method.fetch](path, {
            body: {
              name: 'failed'
            },
            status: 500,
            headers: { 'content-type': 'application/json' }
          })

          utils[method.method]('test', {}).then().catch((response) => {
            expect(response.error).toEqual({name: 'failed'});

            done();
          });
        });

        it('error - from server noBody', (done) => {
          fetchMock[method.fetch](path, {
            status: 500,
            headers: { 'content-type': 'application/json' }
          })

          utils[method.method]('test', {}).then().catch((response) => {
            expect(response.error).toEqual({
              errorCode: 'UNKNOWN_INTERNAL_PROBLEM',
              logId: null,
              message: 'Invalid or missing JSON'
            });

            done();
          });
        });

        it('error - not found', (done) => {
          fetchMock[method.fetch](path, {
            status: 404,
            headers: { 'content-type': 'application/json' }
          })

          utils[method.method]('test', {}).then().catch((response) => {
            expect(response.error).toEqual({
              errorCode: 'NOT_FOUND',
              logId: null,
              message: 'Resource not found'
            });

            done();
          });
        });
      });
    })
  })

});
