import React from 'react'
import { useRouteError } from 'react-router-dom'
import styled from 'styled-components'

const ErrorContainer = styled.div`
  padding: 40px;
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
`

const ErrorTitle = styled.h1`
  color: #d32f2f;
  font-size: 24px;
  margin-bottom: 16px;
`

const ErrorMessage = styled.p`
  color: #666;
  font-size: 16px;
  margin-bottom: 24px;
`

const ErrorDetails = styled.pre`
  background: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  text-align: left;
  overflow-x: auto;
  font-size: 14px;
`

export function ErrorBoundary() {
  const error = useRouteError()

  return (
    <ErrorContainer>
      <ErrorTitle>Något gick fel</ErrorTitle>
      <ErrorMessage>Ett oväntat fel har inträffat. Försök ladda om sidan.</ErrorMessage>
      {process.env.NODE_ENV === 'development' && error && (
        <ErrorDetails>
          {error.message || 'Unknown error'}
          {error.stack && `\n\n${error.stack}`}
        </ErrorDetails>
      )}
    </ErrorContainer>
  )
}

export default ErrorBoundary
