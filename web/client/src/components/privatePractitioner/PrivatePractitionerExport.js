import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Button } from 'reactstrap'
import { IaTypo03 } from '../styles/iaTypography'
import styled from 'styled-components'
import * as actions from '../../store/actions/privatePractitioner'
import { getErrorMessagePrivatePractitionerFile, getIsFetchingPrivatePractitionerFile } from '../../store/reducers/privatePractitioner'
import colors from '../styles/iaColors'
import LoadingSpinner from '../loadingSpinner'
import { validatePrivatePractitionerFile } from './PrivatePractitionerValidator'

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

const PrivatePractitionerSearchAndExportPage = () => {
  const dispatch = useDispatch()
  const isFetchingPrivatePractitionerFile = useSelector(getIsFetchingPrivatePractitionerFile)
  const errorMessagePrivatePractitionerFile = useSelector(getErrorMessagePrivatePractitionerFile)

  const fetchPrivatePractitionerFile = () => dispatch(actions.fetchPrivatePractitionerFile())

  const [validationExportMessage, setValidationExportMessage] = useState(undefined)

  const exportPrivatePractitioner = () => {
    fetchPrivatePractitionerFile().then((response) => {
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
    setValidationExportMessage(validatePrivatePractitionerFile(errorMessagePrivatePractitionerFile))
  }, [errorMessagePrivatePractitionerFile])

  return (
    <>
      <SpinnerWrapper>
        <LoadingSpinner loading={isFetchingPrivatePractitionerFile} message={'Söker'} />
      </SpinnerWrapper>
      <PageExportRow>
        <PageHeader>
          <IaTypo03>Ladda ner fil med alla privatläkare</IaTypo03>
        </PageHeader>
        <FlexDiv>
          <Button id={'exportBtn'} onClick={() => exportPrivatePractitioner()} color={'success'}>
            Ladda ner
          </Button>
        </FlexDiv>
        <ValidationMessage id={'validationExportMessageId'}>{validationExportMessage}</ValidationMessage>
      </PageExportRow>
    </>
  )
}

export default PrivatePractitionerSearchAndExportPage
