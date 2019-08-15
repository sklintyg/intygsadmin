import * as actions from './sessionPoll'
import {mockStore, syncronousActionTester} from '../../testUtils/actionUtils'
import * as sinon from 'sinon'
import AppConstants from '../../AppConstants'
import * as api from '../../api/userApi'

describe('session poll actions', () => {
  let store, clock

  beforeEach(() => {
    store = mockStore({})
    clock = sinon.useFakeTimers()
  })
  afterEach(() => {
    // Restore the default sandbox here
    sinon.restore()
  })

  describe('startPoll ', () => {
    it('should do nothing when already started', () => {
      store = mockStore({
        sessionPoll: {
          handle: 1,
        },
      })

      syncronousActionTester(store, actions.startPoll, [])
    })

    it('should schedule interval when not started', () => {
      let setIntervalFake = sinon.fake.returns(12345)
      sinon.replace(window, 'setInterval', setIntervalFake)

      store = mockStore({
        sessionPoll: {
          handle: null,
        },
      })

      const expectedActions = [{payload: {handle: 12345}, type: 'SET_POLL_HANDLE'}]
      const tested = () => actions.startPoll()
      syncronousActionTester(store, tested, expectedActions)
      expect(setIntervalFake.lastCall.lastArg).toEqual(AppConstants.POLL_SESSION_INTERVAL_MS)
    })

    it('should schedule interval when not started', () => {
      let setIntervalFake = sinon.fake.returns(12345)
      sinon.replace(window, 'setInterval', setIntervalFake)

      store = mockStore({
        sessionPoll: {
          handle: null,
        },
      })

      const expectedActions = [{payload: {handle: 12345}, type: 'SET_POLL_HANDLE'}]
      const tested = () => actions.startPoll()
      syncronousActionTester(store, tested, expectedActions)
      expect(setIntervalFake.lastCall.lastArg).toEqual(AppConstants.POLL_SESSION_INTERVAL_MS)
    })

    it('should fetch after scheduled interval is passed', (done) => {
      store = mockStore({
        sessionPoll: {
          handle: null,
        },
      })

      let pollSessionFake = sinon.fake.resolves({sessionState: {authenticated: true}})
      sinon.replace(api, 'pollSession', pollSessionFake)

      let setIntervalSpy = sinon.spy(window, 'setInterval')

      const dispatchedActions = syncronousActionTester(store, actions.startPoll)

      expect(dispatchedActions).toEqual([{payload: {handle: setIntervalSpy.returnValues[0]}, type: 'SET_POLL_HANDLE'}])

      store.clearActions()
      clock.tick(AppConstants.POLL_SESSION_INTERVAL_MS + 1000)

      //Need to wrap in promise so that
      Promise.resolve().then(() => {
        expect(store.getActions()).toEqual([
          {type: 'GET_POLL_REQUEST'},
          {type: 'GET_POLL_SUCCESS', payload: {sessionState: {authenticated: true}}},
        ])
        done()
      })
    })
  })

  describe('requestPollUpdate ', () => {
    it('should execute update directly', (done) => {
      let pollSessionFake = sinon.fake.resolves({sessionState: {authenticated: true}})
      sinon.replace(api, 'pollSession', pollSessionFake)

      syncronousActionTester(store, actions.requestPollUpdate)

      //Need to wrap in promise so that
      Promise.resolve().then(() => {
        expect(store.getActions()).toEqual([
          {type: 'GET_POLL_REQUEST'},
          {type: 'GET_POLL_SUCCESS', payload: {sessionState: {authenticated: true}}},
        ])
        done()
      })
    })

    it('should redirect if no longer authenticated', (done) => {
      sinon.stub(window.location, 'href')
      sinon.stub(window.location, 'reload')

      let pollSessionFake = sinon.fake.resolves({sessionState: {authenticated: false}})
      sinon.replace(api, 'pollSession', pollSessionFake)

      syncronousActionTester(store, actions.requestPollUpdate)

      //Need to wrap in promise so that the promise inside setInterval callback is invoked before asserting
      Promise.resolve().then(() => {
        expect(store.getActions()).toEqual([
          {type: 'GET_POLL_REQUEST'},
          {type: 'GET_POLL_SUCCESS', payload: {sessionState: {authenticated: false}}},
        ])
        expect(window.location.href).toContain(AppConstants.TIMEOUT_REDIRECT_URL)
        sinon.assert.calledOnce(window.location.reload)
        done()
      })
    })

    it('should redirect if api call is rejected', (done) => {
      sinon.stub(window.location, 'href')
      sinon.stub(window.location, 'reload')

      let pollSessionFake = sinon.fake.rejects(Error('some error'))
      sinon.replace(api, 'pollSession', pollSessionFake)

      syncronousActionTester(store, actions.requestPollUpdate)

      Promise.resolve().then(() => {
        Promise.resolve().then(() => {
          expect(store.getActions()).toEqual([{type: 'GET_POLL_REQUEST'}, {type: 'GET_POLL_FAIL', payload: expect.any(Error)}])
          expect(window.location.href).toContain(AppConstants.TIMEOUT_REDIRECT_URL)
          sinon.assert.calledOnce(window.location.reload)
          done()
        })
      })
    })
  })

  describe('stopPoll ', () => {
    it('should cancel any pending interval', () => {
      let clearIntervalFake = sinon.fake.returns(true)
      sinon.replace(window, 'clearInterval', clearIntervalFake)

      store = mockStore({
        sessionPoll: {
          handle: 1111,
        },
      })

      const expectedActions = [{payload: {handle: null}, type: 'SET_POLL_HANDLE'}]
      const tested = () => actions.stopPoll()
      syncronousActionTester(store, tested, expectedActions)
      expect(clearIntervalFake.lastCall.lastArg).toEqual(1111)
    })
  })
})
