import React, { Fragment } from 'react'
import styled from 'styled-components'
import ExternalLink from '../components/externalLink/ExternalLink'

const LogIdFooter = styled.div`
  padding-top: 8px;
`

const ErrorMessageFormatter = (props) => {
  const { title, message, logId } = props.error
  console.log(title)
  return (
    <Fragment>
      {title && <h5>{title}</h5>}
      {message && <div>{message}</div>}
      {logId && (
        <LogIdFooter>
          Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i andra hand{' '}
          <ExternalLink href="https://www.inera.se/felanmalan">Inera kundservice</ExternalLink> och uppge då följande logg id: {logId}
        </LogIdFooter>
      )}
    </Fragment>
  )
}

export default ErrorMessageFormatter
