import { cleanup } from '@testing-library/react'
import store from '../store/configureStore'

afterEach(() => {
  cleanup()
  const state = store.getState()
  if (state && typeof state === 'object') {
    Object.keys(state).forEach((key) => {
      if (state[key] && typeof state[key] === 'object' && 'data' in state[key]) {
        state[key].data = null
      }
    })
  }
})
