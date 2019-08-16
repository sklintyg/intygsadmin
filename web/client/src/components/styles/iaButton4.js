import React from 'react'
import * as PropTypes from 'prop-types'
import styled from 'styled-components'
import IaColors from './iaColors'
import { Button } from 'reactstrap'

/**
 * This button implements IB Buttontype 6,
 * see https://inera-certificate.atlassian.net/wiki/spaces/IT/pages/900727150/D+-+IB+Knappar
 */
const ButtonType4 = styled(Button)`
  color: ${IaColors.IA_COLOR_02};
  svg {
    fill: ${IaColors.IA_COLOR_02};
  }
  &:hover {
    svg {
      fill: ${IaColors.IA_COLOR_00};
    }
  }
`
const IaButton4 = ({ label, ...rest }) => {
  return (
    <ButtonType4 color="default" {...rest}>
      {label}{' '}
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" />
        <path d="M0 0h24v24H0z" fill="none" />
      </svg>
    </ButtonType4>
  )
}

IaButton4.propTypes = {
  label: PropTypes.string,
}

export default IaButton4
