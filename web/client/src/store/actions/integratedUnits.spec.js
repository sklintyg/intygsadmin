import { vi } from 'vitest'
import { functionToTest, mockStore } from '@/testUtils/actionUtils'
import * as actions from './integratedUnits'
import * as api from '../../api/integratedUnits.api'

describe('integratedUnits actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      integratedUnits: {
        integratedUnit: {
          response: {},
        },
      },
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('fetchIntegratedUnit', () => {
    test('success', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      vi.spyOn(api, 'fetchIntegratedUnit').mockResolvedValue(response)

      const expectedActions = [{ type: actions.FETCH_INTEGRATED_UNIT_REQUEST }, { type: actions.FETCH_INTEGRATED_UNIT_SUCCESS, response }]

      return functionToTest(store, () => actions.fetchIntegratedUnit(hsaId), expectedActions)
    })

    test('failure', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      vi.spyOn(api, 'fetchIntegratedUnit').mockRejectedValue(response)

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNIT_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNIT_FAILURE, payload: response },
      ]

      return functionToTest(store, () => actions.fetchIntegratedUnit(hsaId), expectedActions)
    })
  })

  describe('fetchIntegratedUnitsFile', () => {
    test('success', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchIntegratedUnitsFile').mockResolvedValue(response)

      const expectedActions = [{ type: actions.FETCH_INTEGRATED_UNITS_FILE_REQUEST }, { type: actions.FETCH_INTEGRATED_UNITS_FILE_SUCCESS }]

      return functionToTest(store, () => actions.fetchIntegratedUnitsFile(), expectedActions)
    })

    test('failure', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchIntegratedUnitsFile').mockRejectedValue(response)

      const expectedActions = [
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_REQUEST },
        { type: actions.FETCH_INTEGRATED_UNITS_FILE_FAILURE, payload: response },
      ]

      return functionToTest(store, () => actions.fetchIntegratedUnitsFile(), expectedActions)
    })
  })
})
