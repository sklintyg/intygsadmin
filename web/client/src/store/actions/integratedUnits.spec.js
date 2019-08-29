import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from './integratedUnits'
import * as api from '../../api/integratedUnits.api'

describe('bannerList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      integratedUnits: {
        integratedUnit: {
          response: {}
        }
      },
    })
  })

  describe('fetchIntegratedUnit', () => {

    test('success', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      api.fetchIntegratedUnit = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNIT_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNIT_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntegratedUnit(hsaId),
        expectedActions
      )
    })

    test('failure', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      api.fetchIntegratedUnit = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNIT_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNIT_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntegratedUnit(hsaId),
        expectedActions
      )
    })
  })

  describe('fetchIntegratedUnitsFile', () => {

    test('success', () => {
      const response = [{}]

      api.fetchIntegratedUnitsFile = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_SUCCESS },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntegratedUnitsFile(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchIntegratedUnitsFile = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchIntegratedUnitsFile(),
        expectedActions
      )
    })
  })

})
