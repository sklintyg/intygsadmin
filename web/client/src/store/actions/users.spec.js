import {functionToTest, mockStore} from '../../testUtils/actionUtils'
import * as actions from './users'
import * as api from '../../api/users.api'

describe('Users actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      users: {
        usersList: {
          content: {},
          errorMessage: null,
          isFetching: false,
          sortColumn: 'createdAtNow',
          sortDirection: 'ASC'
        }
      },
    })
  })

  describe('fetchUsersList', () => {
    test('success', () => {
      const response = [{}]

      api.fetchUsersList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_USERS_LIST_REQUEST },
        { type: actions.FETCH_USERS_LIST_SUCCESS, response, sortColumn: 'createdAt', sortDirection: 'DESC' },
      ]

      return functionToTest(
        store,
        () => actions.fetchUsersList({ sortColumn: 'createdAt', sortDirection: 'DESC' }),
        expectedActions
      )
    })

    test('load sort from store', () => {
      const response = [{}]

      api.fetchUsersList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_USERS_LIST_REQUEST },
        { type: actions.FETCH_USERS_LIST_SUCCESS, response, sortColumn: 'createdAtNow', sortDirection: 'ASC' },
      ]

      return functionToTest(
        store,
        () => actions.fetchUsersList(),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchUsersList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_USERS_LIST_REQUEST },
        { type: actions.FETCH_USERS_LIST_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchUsersList({ sortColumn: 'createdAt', sortDirection: 'DESC' }),
        expectedActions
      )
    })
  })

  describe('removeUser', () => {

    test('success', () => {
      const id = 'userId'
      const response = [{}]

      api.removeUser = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.REMOVE_USER_REQUEST },
        { type: actions.REMOVE_USER_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.removeUser(id),
        expectedActions
      )
    })

    test('failure', () => {
      const id = 'userId'
      const response = [{}]

      api.removeUser = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.REMOVE_USER_REQUEST },
        { type: actions.REMOVE_USER_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.removeUser(id),
        expectedActions
      )
    })
  })

  describe('createUser', () => {

    test('success', () => {
      const response = {id: 'userId'}

      api.createUser = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.CREATE_USER_REQUEST },
        { type: actions.CREATE_USER_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.createUser({name: 'name'}),
        expectedActions
      )
    })

    test('failure', () => {
      const response = {id: 'userId'}

      api.createUser = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.CREATE_USER_REQUEST },
        { type: actions.CREATE_USER_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.createUser({name: 'name'}),
        expectedActions
      )
    })
  })

  describe('updateUser', () => {

    test('success', () => {
      const userId = 'userId';
      const response = {name: 'userId'}

      api.updateUser = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.UPDATE_USER_REQUEST },
        { type: actions.UPDATE_USER_SUCCESS, response },
      ]

      return functionToTest(
        store,
        () => actions.updateUser({name: 'name'}, userId),
        expectedActions
      )
    })

    test('failure', () => {
      const userId = 'userId';
      const response = {name: 'userId'}

      api.updateUser = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.UPDATE_USER_REQUEST },
        { type: actions.UPDATE_USER_FAILURE, payload: response },
      ]

      return functionToTest(
        store,
        () => actions.updateUser({name: 'name'}, userId),
        expectedActions
      )
    })
  })
})
