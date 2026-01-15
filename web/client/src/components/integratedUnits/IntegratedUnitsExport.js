import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Button } from 'reactstrap'
import { IaTypo03 } from '../styles/iaTypography'
import styled from 'styled-components'
import * as actions from '../../store/actions/integratedUnits'
import { getErrorMessageIntegratedUnitsFile, getIsFetchingIntegratedUnitsFile } from '../../store/reducers/integratedUnits'
import colors from '../styles/iaColors'
import LoadingSpinner from '../loadingSpinner'
import { validateIntegratedUnitsFile } from './IntegratedUnitsValidator'

const SpinnerWrapper = styled.div`
  position: relative;
`

const PageHeader = styled.div`
  padding: 12px 0 4px;
  &:first-of-type {
    padding: 4px 0;
  }
`

const PageExportRow = styled.div`
  padding: 45px 0 10px 0;
`

const FlexDiv = styled.div`
  display: flex;
  margin-bottom: 8px;
  > span {
    flex: 0 0 150px;
    &:first-of-type {
      flex: 0 0 50px;
    }
  }
`

const ValidationMessage = styled.div`
  color: ${colors.IA_COLOR_16};
`

const IntegratedUnitsSearchAndExportPage = () => {
  const dispatch = useDispatch()
  const isFetchingIntegratedUnitsFile = useSelector(getIsFetchingIntegratedUnitsFile)
  const errorMessageIntegratedUnitsFile = useSelector(getErrorMessageIntegratedUnitsFile)

  const fetchIntegratedUnitsFile = () => dispatch(actions.fetchIntegratedUnitsFile())

  const [validationExportMessage, setValidationExportMessage] = useState(undefined)

  const exportIntegratedUnits = () => {
    fetchIntegratedUnitsFile().then((response) => {
      if (response !== undefined) {
        const filename = response.headers.get('Content-Disposition').split('filename=')[1]
        response.blob().then((blob) => {
          let url = window.URL.createObjectURL(blob)
          let a = document.createElement('a')
          a.href = url
          a.download = filename
          a.click()
        })
      }
    })
  }

  useEffect(() => {
    setValidationExportMessage(validateIntegratedUnitsFile(errorMessageIntegratedUnitsFile))
  }, [errorMessageIntegratedUnitsFile])

  return (
    <>
      <SpinnerWrapper>
        <LoadingSpinner loading={isFetchingIntegratedUnitsFile} message={'Söker'} />
      </SpinnerWrapper>
      <PageExportRow>
        <PageHeader>
          <IaTypo03>Ladda ner fil med alla integrerade enheter</IaTypo03>
        </PageHeader>
        <FlexDiv>
          <Button id={'exportBtn'} onClick={() => exportIntegratedUnits()} color={'success'}>
            Ladda ner
          </Button>
        </FlexDiv>
        <ValidationMessage id={'validationExportMessageId'}>{validationExportMessage}</ValidationMessage>
      </PageExportRow>
    </>
  )
}

export default IntegratedUnitsSearchAndExportPage
