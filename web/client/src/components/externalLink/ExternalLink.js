import React from 'react'
import colors from '../styles/iaColors'
import styled from 'styled-components'
import { ExternalIcon } from '../styles/iaSvgIcons'

const Link = styled.a`
  color: ${colors.IA_COLOR_02};

  svg {
    margin-left: 4px;
  }
  &:hover {
    color: ${colors.IA_COLOR_05};

    svg {
      fill: ${colors.IA_COLOR_05};
    }
  }
`

const ExternalLink = ({ href, children }) => (
  <Link href={href} rel="noopener noreferrer" target="_blank">
    {children}
    <ExternalIcon color={colors.IA_COLOR_02} />
  </Link>
)

export default ExternalLink
