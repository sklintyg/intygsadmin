import reducer from './privatePractitioner'
import * as actions from '../actions/privatePractitioner'
import { createStore } from 'redux'

describe('private practitioner reducer', () => {
  let stateBefore = {
    privatePractitioner: {},
    isFetching: false,
    errorMessage: null,
    isFetchingPrivatePractitionerFile: false,
    errorMessagePrivatePractitionerFile: null
  }

  let store
  beforeEach(() => {
    store = createStore(reducer)
  })

  test('should return default', () => {
    expect(reducer(undefined, {})).toEqual(stateBefore)
  })

  test('should return statebefore', () => {
    expect(store.getState()).toEqual(stateBefore)
  })

  test('should add item in privatePractitioner', () => {
    let action = {
      type: actions.FETCH_PRIVATE_PRACTITIONER_SUCCESS,
      response: {
        hsaId: 'hsaid',
        name: 'namn',
        careproviderName: 'bolag',
        email: 'email',
        registrationDate: '2019-04-27 11:11:19'
      }
    }

    store.dispatch(action)

    let stateAfter = {
      ...stateBefore,
      privatePractitioner: {
        hsaId: 'hsaid',
        name: 'namn',
        careproviderName: 'bolag',
        email: 'email',
        registrationDate: '2019-04-27 11:11:19'
      }
    }

    expect(store.getState()).toEqual(stateAfter)
  })
})
