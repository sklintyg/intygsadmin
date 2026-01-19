import { vi } from 'vitest'
import { functionToTest, mockStore } from '@/testUtils/actionUtils'
import * as actions from './privatePractitioner'
import * as api from '../../api/privatePractitioner.api'

describe('privatePractitioner actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      privatePractitioner: {
        privatePractitioner: {
          response: {},
        },
      },
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('fetchPrivatePractitioner', () => {
    test('success', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      vi.spyOn(api, 'fetchPrivatePractitioner').mockResolvedValue(response)

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_SUCCESS, response },
      ]

      return functionToTest(store, () => actions.fetchPrivatePractitioner(hsaId), expectedActions)
    })

    test('failure', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      vi.spyOn(api, 'fetchPrivatePractitioner').mockRejectedValue(response)

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FAILURE, payload: response },
      ]

      return functionToTest(store, () => actions.fetchPrivatePractitioner(hsaId), expectedActions)
    })
  })

  describe('fetchPrivatePractitionerFile', () => {
    test('success', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchPrivatePractitionerFile').mockResolvedValue(response)

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS },
      ]

      return functionToTest(store, () => actions.fetchPrivatePractitionerFile(), expectedActions)
    })

    test('failure', () => {
      const response = [{}]

      vi.spyOn(api, 'fetchPrivatePractitionerFile').mockRejectedValue(response)

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_FAILURE, payload: response },
      ]

      return functionToTest(store, () => actions.fetchPrivatePractitionerFile(), expectedActions)
    })
  })
})
