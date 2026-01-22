import React from 'react'
import { render, screen } from '@/testUtils'
import IaAlert, { alertType } from './Alert'

describe('<IaAlert />', () => {
  it('Render alert and children', () => {
    render(<IaAlert type={alertType.INFO}>Alert</IaAlert>)
    expect(screen.getByText('Alert')).toBeInTheDocument()
  })

  describe('icons', () => {
    it('Info icon', () => {
      const { container } = render(<IaAlert type={alertType.INFO}>Alert</IaAlert>)
      expect(container.querySelector('svg')).toBeInTheDocument()
    })

    it('Sekretess icon', () => {
      const { container } = render(<IaAlert type={alertType.SEKRETESS}>Alert</IaAlert>)
      expect(container.querySelector('svg')).toBeInTheDocument()
    })

    it('ErrorOutline icon', () => {
      const { container } = render(<IaAlert type={alertType.OBSERVANDUM}>Alert</IaAlert>)
      expect(container.querySelector('svg')).toBeInTheDocument()
    })

    it('Check icon', () => {
      const { container } = render(<IaAlert type={alertType.CONFIRM}>Alert</IaAlert>)
      expect(container.querySelector('svg')).toBeInTheDocument()
    })

    it('Error icon', () => {
      const { container } = render(<IaAlert type={alertType.ERROR}>Alert</IaAlert>)
      expect(container.querySelector('svg')).toBeInTheDocument()
    })
  })
})
