import React from 'react'
import styled from 'styled-components'
import IAColors from '../styles/iaColors'


const ActiveFutureStatus = styled.span`
  color: ${IAColors.IA_COLOR_05}
`

const FinishedStatus = styled.span`
  color: ${IAColors.IA_COLOR_12}
`

const StatusText = ({ status }) => {

  const statusText = {
    FUTURE: 'Kommande',
    ACTIVE: 'Pågående',
    FINISHED: 'Avslutad',
  }


  const text = statusText[status]

  switch (status) {

  case 'FINISHED':
    return (
      <FinishedStatus>
        {text}
      </FinishedStatus>
    )
  default:
    return (
      <ActiveFutureStatus>
        {text}
      </ActiveFutureStatus>
    )
  }
}

export default StatusText
