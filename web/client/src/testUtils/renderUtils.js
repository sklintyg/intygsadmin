import React from 'react'
import { render } from '@testing-library/react'
import { Provider } from 'react-redux'
import { HashRouter } from 'react-router-dom'
import { configureApplicationStore } from '../store/configureStore'

const createTestStore = (preloadedState = {}) => {
  return configureApplicationStore()
}

export const renderWithProviders = (ui, { preloadedState = {}, store = createTestStore(preloadedState), ...renderOptions } = {}) => {
  const Wrapper = ({ children }) => (
    <Provider store={store}>
      <HashRouter>{children}</HashRouter>
    </Provider>
  )

  return {
    store,
    ...render(ui, { wrapper: Wrapper, ...renderOptions }),
  }
}

export const renderWithRedux = (ui, { preloadedState = {}, store = createTestStore(preloadedState), ...renderOptions } = {}) => {
  const Wrapper = ({ children }) => <Provider store={store}>{children}</Provider>

  return {
    store,
    ...render(ui, { wrapper: Wrapper, ...renderOptions }),
  }
}

export * from '@testing-library/react'
