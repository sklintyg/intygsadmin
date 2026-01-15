import React from 'react'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/iaLayout'
import styled from 'styled-components'
import { ErrorPageIcon } from '../components/styles/iaSvgIcons'
import ErrorMessageFormatter from '../messages/ErrorMessageFormatter'
import { Navigate, useParams } from 'react-router-dom'

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  justify-content: center;
  align-items: center;
`
const ErrorPage = () => {
  const { errorCode, logId } = useParams()

  let activeError = { title: 'Tekniskt fel', message: 'Ett tekniskt fel uppstod.', logId: logId }

  switch (errorCode) {
    case 'LOGIN_FEL001':
      return <Navigate to="/loggedout/LOGIN_FEL001" replace />
    case 'LOGIN_FEL002':
      return <Navigate to="/loggedout/LOGIN_FEL002" replace />
    case 'LOGIN_FEL003':
      return <Navigate to="/loggedout/LOGIN_FEL003" replace />
    case 'NOT_FOUND':
      activeError.title = 'Sidan du söker finns inte'
      activeError.message = 'Kontrollera att den länk du använder är korrekt.'
      break
    default:
      break
  }
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <ErrorPageIcon />
          <br />
          <ErrorMessageFormatter error={activeError} />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default ErrorPage
