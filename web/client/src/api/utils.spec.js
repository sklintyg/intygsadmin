import * as utils from './utils'
import fetchMock from 'fetch-mock'

describe('api utils test', () => {
  beforeAll(() => {
    fetchMock.mockGlobal()
  })

  afterEach(() => {
    fetchMock.removeRoutes()
  })

  afterAll(() => {
    fetchMock.unmockGlobal()
  })

  describe('buildUrlFromParams', () => {
    const path = 'path'

    it('empty state', () => {
      const result = utils.buildUrlFromParams(path, {})

      expect(result).toEqual(path + '')
    })

    it('no parameters state', () => {
      const result = utils.buildUrlFromParams(path)

      expect(result).toEqual(path)
    })

    it("don't include empty parameter values", () => {
      const state = {
        page: '1',
        sort: 'p',
        value: '',
        nullValue: null,
      }
      const result = utils.buildUrlFromParams(path, state)

      expect(result).toEqual(`${path}?page=1&sort=p`)
    })
  })

  describe('handleResponse', () => {
    describe('success', () => {
      const reponse = {
        ok: true,
        json: () => 'json',
      }

      it('empty config', () => {
        const result = utils.handleResponse({})(reponse)

        expect(result).toEqual('json')
      })

      it('no config', () => {
        const result = utils.handleResponse()(reponse)

        expect(result).toEqual('json')
      })

      it('config, noBody', () => {
        const result = utils.handleResponse({ emptyBody: true })(reponse)

        expect(result).toEqual({})
      })
    })

    const methods = [
      {
        method: 'makeServerRequestTest',
        fetch: 'get',
      },
      {
        method: 'makeServerPost',
        fetch: 'post',
      },
      {
        method: 'makeServerPut',
        fetch: 'put',
      },
      {
        method: 'makeServerDelete',
        fetch: 'delete',
      },
    ]

    utils.makeServerRequestTest = (path, body, config) => utils.makeServerRequest(path, config)

    methods.forEach((method) => {
      const path = 'end:/api/test'

      describe(`${method.method}`, () => {
        it('success', async () => {
          fetchMock[method.fetch](
            path,
            {
              body: {
                name: 'test',
              },
              headers: { 'content-type': 'application/json' },
            },
            { repeat: 1 }
          )

          const response = await utils[method.method]('test', {})
          expect(response.name).toEqual('test')
        })

        it('success noBody', async () => {
          fetchMock[method.fetch](
            path,
            {
              headers: { 'content-type': 'application/json' },
            },
            { repeat: 1 }
          )

          const response = await utils[method.method]('test', {}, { emptyBody: true })
          expect(response).toEqual({})
        })

        it('error - network problems', async () => {
          fetchMock[method.fetch](
            path,
            {
              throws: { message: 'failed' },
            },
            { repeat: 1 }
          )

          const error = {
            statusCode: -1,
            error: {
              errorCode: 'NETWORK_ERROR',
              message: { message: 'failed' },
              logId: null,
            },
          }

          await expect(utils[method.method]('test', {})).rejects.toEqual(error)
        })

        it('error - from server', async () => {
          fetchMock[method.fetch](
            path,
            {
              body: {
                name: 'failed',
              },
              status: 500,
              headers: { 'content-type': 'application/json' },
            },
            { repeat: 1 }
          )

          await expect(utils[method.method]('test', {})).rejects.toEqual(
            expect.objectContaining({
              error: { name: 'failed' },
            })
          )
        })

        it('error - from server noBody', async () => {
          fetchMock[method.fetch](
            path,
            {
              status: 500,
              headers: { 'content-type': 'application/json' },
            },
            { repeat: 1 }
          )

          await expect(utils[method.method]('test', {})).rejects.toEqual(
            expect.objectContaining({
              error: {
                errorCode: 'UNKNOWN_INTERNAL_PROBLEM',
                logId: null,
                message: 'Invalid or missing JSON',
              },
            })
          )
        })

        it('error - not found', async () => {
          fetchMock[method.fetch](
            path,
            {
              status: 404,
              headers: { 'content-type': 'application/json' },
            },
            { repeat: 1 }
          )

          await expect(utils[method.method]('test', {})).rejects.toEqual(
            expect.objectContaining({
              error: {
                errorCode: 'NOT_FOUND',
                logId: null,
                message: 'Resource not found',
              },
            })
          )
        })
      })
    })
  })
})
