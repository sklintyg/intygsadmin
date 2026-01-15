import React from 'react'
import { renderWithRedux, waitFor } from './testUtils'
import App from './App'

const mockFetchAnvandare = jest.fn()
const mockFetchAppConfig = jest.fn()
const mockPollSession = jest.fn()

jest.mock('./api/userApi', () => ({
  fetchAnvandare: () => mockFetchAnvandare(),
  pollSession: () => mockPollSession(),
}))

jest.mock('./api/appConfigApi', () => ({
  fetchAppConfig: () => mockFetchAppConfig(),
}))

describe('<App />', () => {
  beforeEach(() => {
    mockFetchAnvandare.mockResolvedValue({
      id: 'test-user',
      name: 'Test User',
    })
    mockFetchAppConfig.mockResolvedValue({
      version: '1.0.0',
    })
    mockPollSession.mockResolvedValue({
      sessionState: {
        authenticated: true,
        secondsUntilExpire: 3600,
      },
    })
  })

  afterEach(() => {
    jest.clearAllMocks()
  })

  test('renders without crashing', async () => {
    const { container } = renderWithRedux(<App />)

    await waitFor(
      () => {
        expect(container.querySelector('div')).toBeInTheDocument()
      },
      { timeout: 3000 }
    )
  })

  test('renders app component', async () => {
    const { container } = renderWithRedux(<App />)

    await waitFor(
      () => {
        expect(container.firstChild).toBeInTheDocument()
      },
      { timeout: 3000 }
    )
  })
})
