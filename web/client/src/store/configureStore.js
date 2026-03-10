import { configureStore } from '@reduxjs/toolkit'
import { createHashHistory } from 'history'
import rootReducer from './reducers'

export const history = createHashHistory()

export const configureApplicationStore = (customMiddleware = []) => {
  const isTest = import.meta.env.MODE === 'test' || (typeof global !== 'undefined' && global.process?.env?.NODE_ENV === 'test')

  return configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: false,
        thunk: true,
      }).concat(customMiddleware),
    devTools: !import.meta.env.PROD && !isTest,
  })
}

export const store = configureApplicationStore()

export default store
