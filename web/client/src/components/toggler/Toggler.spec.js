import React from 'react'
import { render, screen, userEvent } from '../../testUtils'
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
    const handleToggle = jest.fn()
    render(<Toggler handleToggle={handleToggle} />)
    const button = screen.getByRole('button')

    await user.click(button)

    expect(handleToggle).toHaveBeenCalledTimes(1)
  })
})
