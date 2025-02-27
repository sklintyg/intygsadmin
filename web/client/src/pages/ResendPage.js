import React, {useEffect, useState} from 'react';
import MenuBar from '../components/iaMenu/MenuBar';
import PageHeader from '../components/styles/PageHeader';
import IaColors from '../components/styles/iaColors';
import {CustomScrollingContainer, FlexColumnContainer, PageContainer} from '../components/styles/iaLayout';
import {DocIcon} from '../components/styles/iaSvgIcons';
import {IaTypo03} from '../components/styles/iaTypography';
import {RadioWrapper} from '../components/radioButton';
import DatePicker from '../components/datePicker';
import {Button, FormFeedback, Input, UncontrolledTooltip} from 'reactstrap';
import styled from 'styled-components';
import TimePicker from '../components/timePicker';
import {validateDateFormat, validateFromDateBeforeToDate, validateTimeFormat} from '../utils/validation';
import {connect} from 'react-redux';
import {compose} from 'recompose';
import * as actions from '../store/actions/intygInfo';
import {resendCaregiverStatus, resendCertificateStatus, resendUnitsStatus} from "../api/intygInfo.api";
import IaAlert, {alertType} from "../components/alert/Alert";
import ResendStatusCountError from "../components/ResendStatusCount/ResendStatusCountError";
import ResendStatusCount from "../components/ResendStatusCount/ResendStatusCount";

const resentOptions = [
  {
    id: `certificate`,
    label: 'Intygs-id',
    value: '0',
  },
  {
    id: `caregiver`,
    label: 'Vårdgivare',
    value: '1',
  },
  {
    id: `unit`,
    label: 'Vårdenhet',
    value: '2',
  },
]

const StyledInput = styled(Input)`
  width: 100% !important;
`
const FormContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 640px;
  gap: 10px;
`

const ActionsContainer = styled.div`
  display: flex;
  gap: 20px;
  padding: 40px 0 0;
`

const FlexDiv = styled.div`
  display: flex;
  flex-direction: column;
`

const DateDiv = styled.div`
  display: flex;
  align-items: top;
  margin-bottom: 8px;
  > label {
    flex: 0 0 50px;
    margin-bottom: 0;
    align-self: center;
  }
  > span {
    flex: 0 0 150px;
  }
`

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendPage = () => {
  const [preview, setPreview] = useState(false)
  const [statusFor, setStatusFor] = useState('0')
  const [status, setStatus] = useState("")
  const [caregiver, setCaregiver] = useState('')
  const [unit, setUnit] = useState('')
  const [certificates, setCertificates] = useState('')
  const [fromDate, setFromDate] = useState('')
  const [fromTime, setFromTime] = useState('')
  const [toDate, setToDate] = useState('')
  const [toTime, setToTime] = useState('')
  const [schedule, setSchedule] = useState(false)
  const [scheduleDate, setScheduleDate] = useState('')
  const [scheduleTime, setScheduleTime] = useState('')
  const [validationMessages, setValidationMessages] = useState({})
  const [showValidation, setShowValidation] = useState(false)
  const [message, setMessage] = useState('')
  const [showSend, setShowSend] = useState(true)

  useEffect(() => {
    let result = {}
    if (statusFor === '0' && certificates === '') {
      result.certificates = 'Ange intygs-id separerade med kommatecken.'
    }
    if (statusFor === '1' && caregiver === '') {
      result.caregiver = 'Ange vårdgivarens HSA-ID'
    }
    if (statusFor === '2' && unit === '') {
      result.unit = 'Ange vårdenhetens HSA-ID'
    }

    if (status === "") {
      result.status = 'Välj status att skicka'
    }

    if (statusFor !== '0') {
      if (validateDateFormat(fromDate) !== 'ok') {
        result.fromDate = validateDateFormat(fromDate)
      }
      if (validateTimeFormat(fromTime) !== 'ok') {
        result.fromTime = validateTimeFormat(fromTime)
      }
      if (validateDateFormat(toDate) !== 'ok') {
        result.toDate = validateDateFormat(toDate)
      }
      if (validateTimeFormat(toTime) !== 'ok') {
        result.toTime = validateTimeFormat(toTime)
      }

      const validFromBeforeTo = validateFromDateBeforeToDate(fromDate, toDate, fromTime, toTime)
      if (validFromBeforeTo !== 'ok') {
        if (validFromBeforeTo === 'toDateBeforeFrom') {
          result.fromDate = 'Startdatumet ska ligga före slutdatumet.'
        }
        if (validFromBeforeTo === 'toTimeBeforeFrom') {
          result.fromTime = 'Starttiden ska ligga före sluttiden.'
        }
      }
    }

    if (schedule === true) {
      if (validateDateFormat(scheduleDate) !== 'ok') {
        result.scheduleDate = validateDateFormat(scheduleDate)
      }
      if (validateTimeFormat(scheduleTime) !== 'ok') {
        result.scheduleTime = validateTimeFormat(scheduleTime)
      }
    }

    setValidationMessages(result)
  }, [
    statusFor,
    status,
    certificates,
    caregiver,
    unit,
    showValidation,
    fromDate,
    fromTime,
    toDate,
    toTime,
    schedule,
    scheduleDate,
    scheduleTime,
  ])

  const handleResend = () => {
    let request;
    if (statusFor === '0') {
      request = resendCertificateStatus({
        certificateIds: certificates.split(',').map((id) => id.trim()),
        statuses: status.split(',').map((id) => id.trim()),
      });
    } else if (statusFor === '1') {
      request = resendCaregiverStatus({
        careGiverId: caregiver,
        start: `${fromDate}T${fromTime}`,
        end: `${toDate}T${toTime}`,
        statuses: status.split(',').map((id) => id.trim()),
        activationTime: schedule ? `${scheduleDate}T${scheduleTime}` : null,
      });
    } else if (statusFor === '2') {
      request = resendUnitsStatus({
        unitIds: [unit],
        start: `${fromDate}T${fromTime}`,
        end: `${toDate}T${toTime}`,
        statuses: status.split(',').map((id) => id.trim()),
        activationTime: schedule ? `${scheduleDate}T${scheduleTime}` : null,
      });
    }

    request.then((response) => {
      if(response.count !== 0) {
        setMessage('Omsändningen lyckades.')
        setShowSend(false)

      }
      else(
      setMessage('Omsändningen misslyckades. Försök igen.')
      )
    })
    .catch(() => {
      setMessage('Omsändningen misslyckades. Försök igen.');
    });
  };

  return (
    <FlexColumnContainer>
      <MenuBar />
      <CustomScrollingContainer>
        <PageHeader
          header="Skapa omsändning"
          subHeader="Här kan du skapa en omsändning av händelser för både enskilda intygs-id och för hela vårdgivare/vårdenheter."
          icon={<DocIcon color={IaColors.IA_COLOR_02} />}
        />
        <PageContainer>
          {!preview && (
            <FormContainer>
              <IaTypo03 style={{ marginBottom: '20px' }}>Skapa omsändning</IaTypo03>

              <div>
                <label>Omsändning av status för</label>
                <RadioWrapper radioButtons={resentOptions} onChange={(event) => setStatusFor(event.target.value)} selected={statusFor} />
              </div>

              {statusFor === '0' && (
                <FlexDiv>
                  <label htmlFor="certificates" id="certificateLabel">
                    Ange ett eller flera intygs-id
                  </label>
                  <StyledInput
                    type="textarea"
                    id="certificates"
                    value={certificates}
                    onChange={(event) => setCertificates(event.target.value)}
                    invalid={Boolean(showValidation && validationMessages.certificates)}
                  />
                  <FormFeedback>{validationMessages.certificates}</FormFeedback>
                  <UncontrolledTooltip trigger="hover focus" placement="top" target="certificates">
                    Ange flera intygs-id genom att separarera med kommatecken.
                  </UncontrolledTooltip>
                </FlexDiv>
              )}
              {statusFor === '1' && (
                <FlexDiv>
                  <label htmlFor="caregiverId">Ange vårdgivarens HSA-ID</label>
                  <StyledInput
                    id="caregiverId"
                    value={caregiver}
                    onChange={(event) => setCaregiver(event.target.value)}
                    invalid={Boolean(showValidation && validationMessages.caregiver)}
                  />
                  <FormFeedback>{validationMessages.caregiver}</FormFeedback>
                </FlexDiv>
              )}
              {statusFor === '2' && (
                <FlexDiv>
                  <label htmlFor="unitId">Ange vårdenhetens HSA-ID</label>
                  <StyledInput
                    id="unitId"
                    value={unit}
                    onChange={(event) => setUnit(event.target.value)}
                    invalid={Boolean(showValidation && validationMessages.unit)}
                  />
                  <FormFeedback>{validationMessages.unit}</FormFeedback>
                </FlexDiv>
              )}

              <FlexDiv>
                <label>Välj status att skicka</label>
                <StyledInput
                  className="form-select"
                  type="select"
                  value={status}
                  onChange={(event) => setStatus(event.target.value)}
                  invalid={Boolean(showValidation && validationMessages.status)}>
                  <option value={""}>Välj</option>
                  <option value={"SUCCESS,FAILURE"}>Alla</option>
                  <option value={"FAILURE"}>Misslyckade</option>
                </StyledInput>
                <FormFeedback>{validationMessages.status}</FormFeedback>
              </FlexDiv>

              {statusFor !== '0' && (
                <>
                  <FlexDiv>
                    <label htmlFor="fromDate">Välj period</label>
                    <DateDiv>
                      <label htmlFor="fromDate">Från</label>
                      <span>
                        <DatePicker
                          inputId="fromDate"
                          date={fromDate}
                          onChange={(value) => setFromDate(value)}
                          className={showValidation && validationMessages.fromDate !== undefined ? 'error' : ''}
                        />
                        {showValidation && validationMessages.fromDate !== undefined && (
                          <FormFeedback style={{ display: 'block' }}>{validationMessages.fromDate}</FormFeedback>
                        )}
                      </span>
                      <span>
                        <TimePicker
                          inputId="fromTime"
                          value={fromTime}
                          onChange={(value) => setFromTime(value)}
                          className={showValidation && validationMessages.fromTime !== undefined ? 'error' : ''}
                        />
                        {showValidation && validationMessages.fromTime !== undefined && (
                          <FormFeedback style={{ display: 'block' }}>{validationMessages.fromTime}</FormFeedback>
                        )}
                      </span>
                    </DateDiv>

                    <DateDiv>
                      <label htmlFor="toDate">Till</label>
                      <span>
                        <DatePicker
                          inputId="toDate"
                          date={toDate}
                          onChange={(value) => setToDate(value)}
                          className={showValidation && validationMessages.toDate !== undefined ? 'error' : ''}
                        />
                        {showValidation && validationMessages.toDate !== undefined && (
                          <FormFeedback style={{ display: 'block' }}>{validationMessages.toDate}</FormFeedback>
                        )}
                      </span>
                      <span>
                        <TimePicker
                          inputId="toTime"
                          value={toTime}
                          onChange={(value) => setToTime(value)}
                          className={showValidation && validationMessages.toTime !== undefined ? 'error' : ''}
                        />
                        {showValidation && validationMessages.toTime !== undefined && (
                          <FormFeedback style={{ display: 'block' }}>{validationMessages.toTime}</FormFeedback>
                        )}
                      </span>
                    </DateDiv>
                  </FlexDiv>
                </>
              )}

              {['1', '2'].includes(statusFor) && (
                <div>
                  <label>Tid för omsändning</label>
                  <RadioWrapper
                    radioButtons={[
                      {
                        id: `now`,
                        label: 'Skicka nu',
                        value: '0',
                      },
                      {
                        id: `schedule`,
                        label: 'Schemalägg',
                        value: '1',
                      },
                    ]}
                    onChange={(event) => (event.target.value === '0' ? setSchedule(false) : setSchedule(true))}
                    selected={schedule === false ? '0' : '1'}
                  />
                </div>
              )}

              {['0'].includes(statusFor) && (
                <div>
                  <strong>Tid för omsändning: </strong><span>Skicka nu</span>
                </div>
              )}

              {['1', '2'].includes(statusFor) && schedule === true && (
                <DateDiv>
                  <label htmlFor="scheduleDate">Till</label>
                  <span>
                    <DatePicker
                      inputId="scheduleDate"
                      date={scheduleDate}
                      onChange={(value) => setScheduleDate(value)}
                      className={showValidation && validationMessages.scheduleDate !== undefined ? 'error' : ''}
                    />
                    {showValidation && validationMessages.scheduleDate !== undefined && (
                      <FormFeedback style={{ display: 'block' }}>{validationMessages.scheduleDate}</FormFeedback>
                    )}
                  </span>
                  <span>
                    <TimePicker
                      inputId="scheduleTime"
                      value={scheduleTime}
                      onChange={(value) => setScheduleTime(value)}
                      className={showValidation && validationMessages.scheduleTime !== undefined ? 'error' : ''}
                    />
                    {showValidation && validationMessages.scheduleTime !== undefined && (
                      <FormFeedback style={{ display: 'block' }}>{validationMessages.scheduleTime}</FormFeedback>
                    )}
                  </span>
                </DateDiv>
              )}

              <ActionsContainer>
                <Button
                  color={'default'}
                  onClick={() => {
                    setStatusFor('0')
                    setCaregiver('')
                    setUnit('')
                    setCertificates('')
                    setFromDate('')
                    setFromTime('')
                    setToDate('')
                    setToTime('')
                    setSchedule(false)
                    setScheduleDate('')
                    setScheduleTime('')
                  }}>
                  Rensa
                </Button>
                <Button
                  color={'primary'}
                  onClick={() => {
                    setShowValidation(true)
                    if (Object.keys(validationMessages).length === 0) {
                      setPreview(true)
                    }
                  }}>
                  Granska
                </Button>
              </ActionsContainer>
            </FormContainer>
          )}

          {preview && (
            <>
              <IaTypo03 style={{ marginBottom: '20px' }}>Granska omsändning</IaTypo03>
              <ResendStatusCountError
                statusFor={statusFor}
                certificateIds={certificates.split(',').map((id) => id.trim())}
                careGiverId={caregiver}
                unitIds={[unit]}
                statuses={status.split(',').map((id) => id.trim())}
                start={`${fromDate}T${fromTime}`}
                end={`${toDate}T${toTime}`}
              />
              <PreviewDiv>
                <strong>Omsändning av status för</strong>
                <span>{resentOptions.find(({ value }) => statusFor === value).label}</span>
              </PreviewDiv>

              {statusFor === '0' && (
                <PreviewDiv>
                  <strong>Ange ett eller flera intygs-id</strong>
                  <span>{certificates}</span>
                </PreviewDiv>
              )}
              {statusFor === '1' && (
                <PreviewDiv>
                  <strong>Ange vårdgivarens HSA-ID</strong>
                  <span>{caregiver}</span>
                </PreviewDiv>
              )}
              {statusFor === '2' && (
                <PreviewDiv>
                  <strong>Ange vårdenhetens HSA-ID</strong>
                  <span>{unit}</span>
                </PreviewDiv>
              )}

              <PreviewDiv>
                <strong>Välj status att skicka</strong>
                <span>{status === "FAILURE" ? 'Misslyckade' : 'Alla'}</span>
              </PreviewDiv>

              {statusFor !== '0' && (
                <PreviewDiv>
                  <strong>Välj period</strong>
                  <span>Från {`${fromDate}:${fromTime}`}</span>
                  <span>Till {`${toDate}:${toTime}`}</span>
                </PreviewDiv>
              )}

              {['0', '1', '2'].includes(statusFor) && (
                <PreviewDiv>
                  <strong>Tid för omsändning</strong>
                  <span>{schedule ? `Schemalägg: ${scheduleDate}, ${scheduleTime}` : "Skicka nu"}</span>
                </PreviewDiv>
              )}
              <ResendStatusCount />
              {message && (
                <IaAlert type={message === 'Omsändningen lyckades' ? alertType.CONFIRM : alertType.ERROR }>
                  {message}
                </IaAlert>
              )}

              <ActionsContainer>
                <Button
                  color={'default'}
                  onClick={() => {
                    setPreview(false)
                    setMessage('')
                    setShowSend(true)
                  }}>
                  Tillbaka
                </Button>
                {showSend && (
                <Button
                  color={'primary'}
                  onClick={handleResend}>
                  Skicka
                </Button>
                )}
              </ActionsContainer>
            </>
          )}
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default compose(
  connect(
    null,
    actions
  )
)(ResendPage)
