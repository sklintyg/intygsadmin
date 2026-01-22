import { vi } from 'vitest'
import React from 'react'
import { renderWithRedux } from './testUtils'
import { waitFor } from '@testing-library/react'
import App from './App'

const mockFetchAnvandare = vi.fn()
const mockFetchAppConfig = vi.fn()
const mockPollSession = vi.fn()

vi.mock('./api/userApi', () => ({
  fetchAnvandare: () => mockFetchAnvandare(),
  pollSession: () => mockPollSession(),
}))

vi.mock('./api/appConfigApi', () => ({
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
    vi.clearAllMocks()
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
