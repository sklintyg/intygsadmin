import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from './privatePractitioner'
import * as api from '../../api/privatePractitioner.api'

describe('privatePractitioner actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      privatePractitioner: {
        privatePractitioner: {
          response: {}
        }
      },
    })
  })

  describe('fetchPrivatePractitioner', () => {

    test('success', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      api.fetchPrivatePractitioner = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.fetchPrivatePractitioner(hsaId),
        expectedActions
      )
    })

    test('failure', () => {
      const hsaId = 'HsaId'
      const response = [{}]

      api.fetchPrivatePractitioner = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchPrivatePractitioner(hsaId),
        expectedActions
      )
    })
  })

  describe('fetchPrivatePractitionerFile', () => {

    test('success', () => {
      const response = [{}]

      api.fetchPrivatePractitionerFile = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST },
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_SUCCESS },
      ]

      return functionToTest(
        store,
        () => actions.fetchPrivatePractitionerFile(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchPrivatePractitionerFile = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_PRIVATE_PRACTITIONER_FILE_REQUEST },
        { type: actions.FETCH_PRACTITIONER_FILE_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchPrivatePractitionerFile(),
        expectedActions
      )
    })
  })

})
