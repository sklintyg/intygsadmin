import React, { Fragment } from 'react'
import colors from '../styles/IbColors'
import styled from 'styled-components'
import { ExternalIcon } from '../styles/IbSvgIcons'

const Link = styled.a`
  color: ${colors.IB_COLOR_17};

  svg {
    margin-left: 4px;
  }
  &:hover {
    color: ${colors.IB_COLOR_21};

    svg {
      fill: ${colors.IB_COLOR_21};
    }
  }
`

export default ({ href, children }) => (
  <Fragment>
    <Link href={href} rel="noopener noreferrer" target="_blank">
      {children}
      <ExternalIcon color={colors.IB_COLOR_17} />
    </Link>
  </Fragment>
)
