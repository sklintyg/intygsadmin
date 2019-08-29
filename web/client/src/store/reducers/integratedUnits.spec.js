import reducer from './integratedUnits'
import * as actions from '../actions/integratedUnits'
import { createStore } from 'redux'

describe('integrated units reducer', () => {
  let stateBefore = {
    integratedUnit: {},
    isFetching: false,
    errorMessage: null,
    isFetchingIntegratedUnitsFile: false,
    errorMessageIntegratedUnitsFile: null
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

  test('should add item in integratedUnit', () => {
    let action = {
      type: actions.FETCH_INTEGRATED_UNIT_SUCCESS,
      response: {
        enhetsId: 'enhetsid',
        enhetsNamn: 'enhetsnamn',
        vardgivarId: 'vardgivare',
        vardgivarNamn: 'vardgivarnamn',
        skapadDatum: '2019-04-27 11:11:19',
        senasteKontrollDatum: '2019-05-02 11:11:19'
      }
    }

    store.dispatch(action)

    let stateAfter = {
      ...stateBefore,
      integratedUnit: {
        enhetsId: 'enhetsid',
        enhetsNamn: 'enhetsnamn',
        vardgivarId: 'vardgivare',
        vardgivarNamn: 'vardgivarnamn',
        skapadDatum: '2019-04-27 11:11:19',
        senasteKontrollDatum: '2019-05-02 11:11:19'
      }
    }

    expect(store.getState()).toEqual(stateAfter)
  })
})
