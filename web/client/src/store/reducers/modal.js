import { CLOSE_ALL_MODALS, CLOSE_MODAL, OPEN_MODAL } from '../actions/modal'
import { isEmpty } from 'lodash'

const INITIAL_STATE = {}

const modalReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case OPEN_MODAL: {
      const { id, ...data } = action.payload
      return { ...state, [id]: true, ...(isEmpty(data) ? {} : { [id + 'Data']: data }) }
    }
    case CLOSE_MODAL: {
      const next = { ...state }
      delete next[action.payload.id + 'Data']
      next[action.payload.id] = false
      return next
    }
    case CLOSE_ALL_MODALS:
      return INITIAL_STATE
    default:
      return state
  }
}

export default modalReducer
