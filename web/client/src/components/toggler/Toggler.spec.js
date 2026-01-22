import { vi } from 'vitest'
import React from 'react'
import { render, screen } from '@/testUtils'
import userEvent from '@testing-library/user-event'
import Toggler from './Toggler'

describe('<Toggler />', () => {
  it('renders collapse icon when expanded = false', () => {
    render(<Toggler expanded={false} />)
    const button = screen.getByRole('button')
    expect(button).toBeInTheDocument()
  })

  it('renders expand icon when expanded = true', () => {
    render(<Toggler expanded={true} />)
    const button = screen.getByRole('button')
    expect(button).toBeInTheDocument()
  })

  it('calls handleToggle when clicked', async () => {
    const user = userEvent.setup()
    const handleToggle = vi.fn()
    render(<Toggler handleToggle={handleToggle} />)
    const button = screen.getByRole('button')

    await user.click(button)

    expect(handleToggle).toHaveBeenCalledTimes(1)
  })
})
