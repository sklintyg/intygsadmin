import React from 'react'
import { Section } from '../styles/iaLayout'
import IaButton4 from '../styles/iaButton4'
import { Spinner } from 'reactstrap'
import ErrorMessageFormatter from '../../messages/ErrorMessageFormatter'
import IaAlert, { alertType } from '../alert/Alert'

const LoginOptions = ({ settings, isFetching, errorMessage }) => {
  const doLogin = (loginUrl) => () => {
    window.location.href = loginUrl
  }
  return (
    <Section>
      {isFetching && (
        <span>
          <Spinner size="sm" color="secondary" /> Hämtar konfiguration...{' '}
        </span>
      )}
      {!isFetching && !errorMessage && <IaButton4 onClick={doLogin(settings.loginUrl)} label="Logga in med SITHS" />}
      {errorMessage && (
        <IaAlert type={alertType.ERROR}>
          {' '}
          <ErrorMessageFormatter error={errorMessage} />
        </IaAlert>
      )}
    </Section>
  )
}

export default LoginOptions
