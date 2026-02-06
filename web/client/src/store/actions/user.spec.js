import { vi } from 'vitest'
import * as actions from './user'
import * as api from '../../api/userApi'
import { functionToTest, mockStore } from '@/testUtils'

describe('actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({})
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('getUser', () => {
    it('success', () => {
      const reponse = {
        employeeHsaId: 'TEST123',
        name: 'Test User',
        intygsadminRole: 'FULL',
        logoutUrl: '/logout',
      }

      vi.spyOn(api, 'fetchAnvandare').mockResolvedValue(reponse)

      const expectedActions = [{ type: actions.GET_USER }, { type: actions.GET_USER_SUCCESS, payload: reponse }]

      return functionToTest(store, actions.getUser, expectedActions)
    })

    it('failure', () => {
      vi.spyOn(api, 'fetchAnvandare').mockRejectedValue({ message: 'failed', errorCode: 'ERRORCODE' })

      const expectedActions = [
        { type: actions.GET_USER },
        { type: actions.GET_USER_FAILURE, payload: { message: 'failed', errorCode: 'ERRORCODE' } },
      ]

      return functionToTest(store, actions.getUser, expectedActions)
    })
  })
})
