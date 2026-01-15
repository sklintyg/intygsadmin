import { configureStore } from '@reduxjs/toolkit'
import { createHashHistory } from 'history'
import rootReducer from './reducers'

export const history = createHashHistory()

export const configureApplicationStore = (customMiddleware = []) =>
  configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: false,
      }).concat(customMiddleware),
    devTools: process.env.NODE_ENV !== 'production',
  })

export const store = configureApplicationStore()

export default store
