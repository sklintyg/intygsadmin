import React, { Fragment } from 'react'
import colors from '../styles/IaColors'
import styled from 'styled-components'
import { ExternalIcon } from '../styles/IaSvgIcons'

const Link = styled.a`
  color: ${colors.AG_COLOR_02};

  svg {
    margin-left: 4px;
  }
  &:hover {
    color: ${colors.AG_COLOR_05};

    svg {
      fill: ${colors.AG_COLOR_05};
    }
  }
`

export default ({ href, children }) => (
  <Fragment>
    <Link href={href} rel="noopener noreferrer" target="_blank">
      {children}
      <ExternalIcon color={colors.AG_COLOR_02} />
    </Link>
  </Fragment>
)
