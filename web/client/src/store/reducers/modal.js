import { CLOSE_ALL_MODALS, CLOSE_MODAL, OPEN_MODAL } from '../actions/modal'
import { isEmpty } from 'lodash'

const INITIAL_STATE = {}

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case OPEN_MODAL:
      const { id, ...data } = action.payload
      return { ...state, [action.payload.id]: true, ...(isEmpty(data) ? {} : { [id + 'Data']: data }) }
    case CLOSE_MODAL:
      return { ...state, [action.payload.id]: false }
    case CLOSE_ALL_MODALS:
      return INITIAL_STATE
    default:
      return state
  }
}
