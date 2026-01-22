import { vi } from 'vitest'
import * as util from './util'
import * as messages from '../../messages/messages'

vi.spyOn(messages, 'getMessage').mockImplementation((messageKey) => messageKey)
vi.spyOn(messages, 'haveMessage').mockImplementation((messageKey) => {
  if (messageKey === 'error.common.network_error.message') {
    return true
  }
  return false
})

describe('util test', () => {
  describe('buildClientError', () => {
    it('empty error', () => {
      const errorResponse = {}

      const result = util.buildClientError(errorResponse, 'error.user')

      expect(result).toEqual({ title: 'error.common.unknown.title', message: 'error.common.unknown.message', logId: null })
    })

    it('missing errorCode', () => {
      const errorResponse = {
        error: {},
      }

      const result = util.buildClientError(errorResponse, 'error.user')

      expect(result).toEqual({ title: 'error.common.unknown.title', message: 'error.common.unknown.message', logId: null })
    })

    it('errorCode found', () => {
      const errorResponse = {
        error: {
          errorCode: 'NOT_FOUND',
        },
      }

      const result = util.buildClientError(errorResponse, 'error.user')

      expect(result).toEqual({ title: 'error.common.unknown.title', message: 'error.common.unknown.message', logId: null })
    })

    it('errorCode fallback', () => {
      const errorResponse = {
        error: {
          errorCode: 'NETWORK_ERROR',
        },
      }

      const result = util.buildClientError(errorResponse, 'error.user')

      expect(result).toEqual({ title: 'error.common.network_error.title', message: 'error.common.network_error.message', logId: null })
    })

    it('errorCode fallback common', () => {
      const errorResponse = {
        error: {
          errorCode: 'NOT_FOUND_CODE',
        },
      }

      const result = util.buildClientError(errorResponse, 'error.user')

      expect(result).toEqual({ title: 'error.common.unknown.title', message: 'error.common.unknown.message', logId: null })
    })
  })
})
