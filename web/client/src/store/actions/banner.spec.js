import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from './banner'
import * as api from '../../api/banner.api'

describe('Banner actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      banner: {},
    })
  })

  describe('removeBanner', () => {
    test('success', () => {
      const id = 'userId'
      const response = [{}]

      api.removeBanner = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [{ type: actions.REMOVE_BANNER_REQUEST }, { type: actions.REMOVE_BANNER_SUCCESS, response }]

      return functionToTest(store, () => actions.removeBanner(id), expectedActions)
    })

    test('failure', () => {
      const id = 'userId'
      const response = [{}]

      api.removeBanner = () => {
        return Promise.reject(response)
      }

      const expectedActions = [{ type: actions.REMOVE_BANNER_REQUEST }, { type: actions.REMOVE_BANNER_FAILURE, payload: response }]

      return functionToTest(store, () => actions.removeBanner(id), expectedActions)
    })
  })

  describe('createBanner', () => {
    test('success', () => {
      const response = { id: 'userId' }

      api.createBanner = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [{ type: actions.CREATE_BANNER_REQUEST }, { type: actions.CREATE_BANNER_SUCCESS, response }]

      return functionToTest(store, () => actions.createBanner({ name: 'name' }), expectedActions)
    })

    test('failure', () => {
      const response = { id: 'userId' }

      api.createBanner = () => {
        return Promise.reject(response)
      }

      const expectedActions = [{ type: actions.CREATE_BANNER_REQUEST }, { type: actions.CREATE_BANNER_FAILURE, payload: response }]

      return functionToTest(store, () => actions.createBanner({ name: 'name' }), expectedActions)
    })
  })

  describe('updateBanner', () => {
    const bannerId = 'userId'

    test('success', () => {
      const response = { name: 'userId' }

      api.updateBanner = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [{ type: actions.UPDATE_BANNER_REQUEST }, { type: actions.UPDATE_BANNER_SUCCESS, response }]

      return functionToTest(store, () => actions.updateBanner({ name: 'name' }, bannerId), expectedActions)
    })

    test('failure', () => {
      const response = { name: 'userId' }

      api.updateBanner = () => {
        return Promise.reject(response)
      }

      const expectedActions = [{ type: actions.UPDATE_BANNER_REQUEST }, { type: actions.UPDATE_BANNER_FAILURE, payload: response }]

      return functionToTest(store, () => actions.updateBanner({ name: 'name' }, bannerId), expectedActions)
    })
  })

  describe('fetchFutureBanners', () => {
    test('success', () => {
      const response = { name: 'userId' }

      api.fetchFutureBanners = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [{ type: actions.FETCH_FUTURE_REQUEST }, { type: actions.FETCH_FUTURE_SUCCESS, response }]

      return functionToTest(store, () => actions.fetchFutureBanners('WEBCERT'), expectedActions)
    })

    test('failure', () => {
      const response = { name: 'userId' }

      api.fetchFutureBanners = () => {
        return Promise.reject(response)
      }

      const expectedActions = [{ type: actions.FETCH_FUTURE_REQUEST }, { type: actions.FETCH_FUTURE_FAILURE, payload: response }]

      return functionToTest(store, () => actions.fetchFutureBanners('WEBCERT'), expectedActions)
    })
  })
})
