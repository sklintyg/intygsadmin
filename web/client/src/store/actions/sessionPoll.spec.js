import { vi } from 'vitest'
import * as actions from './sessionPoll'
import { mockStore, syncronousActionTester } from '@/testUtils/actionUtils'
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
    sinon.restore()
    vi.restoreAllMocks()
  })

  describe('startPoll', () => {
    it('should do nothing when already started', () => {
      store = mockStore({
        sessionPoll: {
          handle: 1,
        },
      })

      syncronousActionTester(store, actions.startPoll, [])
    })

    it('should schedule interval when not started', () => {
      const setIntervalSpy = vi.spyOn(window, 'setInterval').mockReturnValue(12345)

      store = mockStore({
        sessionPoll: {
          handle: null,
        },
      })

      const expectedActions = [{ payload: { handle: 12345 }, type: 'SET_POLL_HANDLE' }]
      const tested = () => actions.startPoll()
      syncronousActionTester(store, tested, expectedActions)
      expect(setIntervalSpy.mock.lastCall[1]).toEqual(AppConstants.POLL_SESSION_INTERVAL_MS)
    })

    it('should fetch after scheduled interval is passed', async () => {
      store = mockStore({
        sessionPoll: {
          handle: null,
        },
      })

      vi.spyOn(api, 'pollSession').mockResolvedValue({ sessionState: { authenticated: true } })

      const setIntervalSpy = vi.spyOn(window, 'setInterval')

      const dispatchedActions = syncronousActionTester(store, actions.startPoll)

      expect(dispatchedActions).toEqual([{ payload: { handle: setIntervalSpy.mock.results[0].value }, type: 'SET_POLL_HANDLE' }])

      store.clearActions()
      await clock.tickAsync(AppConstants.POLL_SESSION_INTERVAL_MS + 1000)

      expect(store.getActions()).toEqual([
        { type: 'GET_POLL_REQUEST' },
        { type: 'GET_POLL_SUCCESS', payload: { sessionState: { authenticated: true } } },
      ])
    })
  })

  describe('requestPollUpdate', () => {
    it('should execute update directly', async () => {
      vi.spyOn(api, 'pollSession').mockResolvedValue({ sessionState: { authenticated: true } })

      const promise = store.dispatch(actions.requestPollUpdate())
      await promise

      expect(store.getActions()).toEqual([
        { type: 'GET_POLL_REQUEST' },
        { type: 'GET_POLL_SUCCESS', payload: { sessionState: { authenticated: true } } },
      ])
    })

    it('should redirect if no longer authenticated', async () => {
      const originalLocation = window.location
      delete window.location
      window.location = { href: '', reload: sinon.fake() }

      vi.spyOn(api, 'pollSession').mockResolvedValue({ sessionState: { authenticated: false } })

      const promise = store.dispatch(actions.requestPollUpdate())
      await promise

      expect(store.getActions()).toEqual([
        { type: 'GET_POLL_REQUEST' },
        { type: 'GET_POLL_SUCCESS', payload: { sessionState: { authenticated: false } } },
      ])
      expect(window.location.href).toContain(AppConstants.TIMEOUT_REDIRECT_URL)
      sinon.assert.calledOnce(window.location.reload)

      window.location = originalLocation
    })

    it('should redirect if api call is rejected', async () => {
      const originalLocation = window.location
      delete window.location
      window.location = { href: '', reload: sinon.fake() }

      vi.spyOn(api, 'pollSession').mockRejectedValue(Error('some error'))

      const promise = store.dispatch(actions.requestPollUpdate())
      await promise

      expect(store.getActions()).toEqual([{ type: 'GET_POLL_REQUEST' }, { type: 'GET_POLL_FAIL', payload: expect.any(Error) }])
      expect(window.location.href).toContain(AppConstants.TIMEOUT_REDIRECT_URL)
      sinon.assert.calledOnce(window.location.reload)

      window.location = originalLocation
    })
  })

  describe('stopPoll', () => {
    it('should cancel any pending interval', () => {
      const clearIntervalSpy = vi.spyOn(window, 'clearInterval').mockReturnValue(true)

      store = mockStore({
        sessionPoll: {
          handle: 1111,
        },
      })

      const expectedActions = [{ payload: { handle: null }, type: 'SET_POLL_HANDLE' }]
      const tested = () => actions.stopPoll()
      syncronousActionTester(store, tested, expectedActions)
      expect(clearIntervalSpy.mock.lastCall[0]).toEqual(1111)
    })
  })
})
