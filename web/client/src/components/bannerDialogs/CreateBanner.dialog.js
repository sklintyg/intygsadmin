import React, { useState, useEffect, useRef } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import { RadioWrapper } from '../radioButton'
import CustomTextarea from '../CustomTextarea'
import DatePicker from '../datePicker'
import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'
import TimePicker from '../timePicker'
import styled from 'styled-components'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import { validateBanner } from './BannerValidator'
import HelpChevron from '../helpChevron'
import colors from '../styles/iaColors'
import { ErrorSection, ErrorWrapper } from '../styles/iaLayout'
import IaAlert, { alertType } from '../alert/Alert'
import { getFutureBanners } from '../../store/reducers/banner'

const StyledBody = styled(ModalBody)`
  h5 {
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
  }
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

const initialBanner = {
  application: undefined,
  message: '',
  displayFrom: undefined,
  displayTo: undefined,
  displayFromTime: undefined,
  displayToTime: undefined,
  priority: undefined,
}

const tjanstButtons = [
  { id: 'tjanstIntygsstatistik', label: 'Intygsstatistik', value: 'INTYGSSTATISTIK' },
  { id: 'tjanstRehabstod', label: 'Rehabstöd', value: 'REHABSTOD' },
  { id: 'tjanstWebcert', label: 'Webcert', value: 'WEBCERT' },
]

const prioButtons = [
  { id: 'prioLOW', label: 'Låg', value: 'LOW' },
  { id: 'prioMEDIUM', label: 'Medel', value: 'MEDIUM' },
  { id: 'prioHIGH', label: 'Hög', value: 'HIGH' }
]

const CreateBanner = ({ handleClose, isOpen, onComplete, createBanner, updateBanner, data, fetchFutureBanners, futureBanners }) => {
  const [validationMessages, setValidationMessages] = useState({})
  const [update, setUpdate] = useState(false)
  const [newBanner, setNewBanner] = useState(initialBanner)
  const [errorActive, setErrorActive] = useState(false)
  const [initialMessageValue, setInitialMessageValue] = useState('')

  const setApplicationAndCheckFuture = (application) => {
    fetchFutureBanners(application).finally(() => {
      setNewBanner({ ...newBanner, application })
    })
  }

  useEffect(() => {
    if (data && data.banner) {
      setUpdate(true)
      setInitialMessageValue(data.banner.message)
      let displayFrom = new Date(data.banner.displayFrom)
      let displayTo = new Date(data.banner.displayTo)
      setNewBanner({
        ...data.banner,
        displayFrom: displayFrom.toLocaleDateString('sv-SE').replace(/[^ -~]/g, ''),
        displayTo: displayTo.toLocaleDateString('sv-SE').replace(/[^ -~]/g, ''),
        displayFromTime: displayFrom.toLocaleTimeString('sv-SE', { hour: '2-digit', minute: '2-digit' }).replace(/[^ -~]/g, ''),
        displayToTime: displayTo.toLocaleTimeString('sv-SE', { hour: '2-digit', minute: '2-digit' }).replace(/[^ -~]/g, ''),
      })
    } else {
      setUpdate(false)
    }
  }, [data])

  useEffect(() => {
    if (isEqual(previousBanner.current, newBanner)) {
      return
    }
    setValidationMessages(validateBanner(newBanner, futureBanners))
  }, [newBanner, futureBanners])

  const previousBanner = useRef()
  useEffect(() => {
    previousBanner.current = newBanner
  })

  const onChange = (value, prop) => {
    setNewBanner({ ...newBanner, [prop]: value })
  }

  const createSendObject = () => {
    return {
      application: newBanner.application,
      message: newBanner.message,
      displayFrom: newBanner.displayFrom + 'T' + newBanner.displayFromTime,
      displayTo: newBanner.displayTo + 'T' + newBanner.displayToTime,
      priority: newBanner.priority,
    }
  }

  const send = () => {
    const func = update ? updateBanner(createSendObject(), data.banner.id) : createBanner(createSendObject());

    func
      .then(() => {
        cancel()
        onComplete()
      })
      .catch((data) => {
        setErrorActive(true)
      })
  }

  const cancel = () => {
    setErrorActive(false)
    setNewBanner(initialBanner)
    setInitialMessageValue('')
    handleClose()
  }

  const enableSaveBtn = () => {

    const fields = ['application', 'message', 'displayFrom', 'displayTo', 'priority']
    const fieldsEdit = fields.concat(['displayFromTime','displayToTime'])

    let enable = fieldsEdit.reduce((accumulator, currentValue) => {
      return accumulator && newBanner[currentValue]
    }, true)

    enable = enable &&
      isEmpty(validationMessages)

    if (update) {
      let changed = fields.reduce((accumulator, currentValue) => {
        let compareValue = newBanner[currentValue]
        if (currentValue === 'displayFrom') {
          compareValue = newBanner[currentValue] + 'T' + newBanner['displayFromTime'] + ':00'
        } else if (currentValue === 'displayTo') {
          compareValue = newBanner[currentValue] + 'T' + newBanner['displayToTime'] + ':00'
        }

        return accumulator || (data.banner && data.banner[currentValue] !== compareValue)
      }, false)

      enable = enable && changed
    }

    return enable
  }

  return (
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
        <ModalHeader toggle={cancel}>{update ? 'Ändra driftbannerns innehåll' : 'Skapa driftbanner'}</ModalHeader>
        <StyledBody>
          <h5>Välj tjänst</h5>
          <RadioWrapper
            radioButtons={tjanstButtons}
            onChange={(event) => setApplicationAndCheckFuture(event.target.value)}
            selected={newBanner.application}
          />
          <h5>Skriv meddelandetext</h5>
          <CustomTextarea inputId='bannerMessage' className='show-external-link' onChange={(value) => onChange(value, 'message')} value={initialMessageValue} limit={200} />
          <h5>Ange visningsperiod</h5>
          <FlexDiv>
            <span>Från</span>
            <span>
              <DatePicker
                inputId='displayFromDate'
                date={newBanner.displayFrom}
                onChange={(value) => onChange(value, 'displayFrom')}
                className={validationMessages.displayFrom !== undefined ? 'error' : ''}
              />
            </span>
            <span>
              <TimePicker
                inputId='displayFromTime'
                value={newBanner.displayFromTime}
                onChange={(value) => onChange(value, 'displayFromTime')}
                className={validationMessages.displayFromTime !== undefined ? 'error' : ''}
              />
            </span>
          </FlexDiv>
          <ValidationMessage>{validationMessages.displayFrom}</ValidationMessage>
          <ValidationMessage>{validationMessages.displayFromTime}</ValidationMessage>
          <FlexDiv>
            <span>till</span>
            <span>
              <DatePicker
                inputId='displayToDate'
                date={newBanner.displayTo}
                onChange={(value) => onChange(value, 'displayTo')}
                className={validationMessages.displayTo !== undefined ? 'error' : ''}
              />
            </span>
            <span>
              <TimePicker
                inputId='displayToTime'
                value={newBanner.displayToTime}
                onChange={(value) => onChange(value, 'displayToTime')}
                className={validationMessages.displayToTime !== undefined ? 'error' : ''}
              />
            </span>
          </FlexDiv>
          <ValidationMessage>{validationMessages.displayTo}</ValidationMessage>
          <ValidationMessage>{validationMessages.displayToTime}</ValidationMessage>
          <HelpChevron label={'Välj prioritet'}>
            <h5>Låg prioritet</h5>
            <p>Används för information som inte påverkar användandet av tjänsten. En blå driftbanner visas.</p>
            <h5>Medel prioritet</h5>
            <p>
              Används för information som användaren bör uppmärksamma och för händelser som inte påverkar användandet av tjänsten, till
              exempel vid kommande driftstörningar. En gul driftbanner visas.
            </p>
            <h5>Hög prioritet</h5>
            <p>
              Används för information som användaren behöver uppmärksamma och för händelser som påverkar användandet av tjänsten, till
              exempel vid pågående driftstörningar. En röd driftbanner visas.
            </p>
          </HelpChevron>
          <RadioWrapper
            radioButtons={prioButtons}
            onChange={(event) => onChange(event.target.value, 'priority')}
            selected={newBanner.priority}
          />
        </StyledBody>
        <ErrorSection>
          {errorActive && (
            <ErrorWrapper>
              <IaAlert type={alertType.ERROR}>
                Driftbannern kunde inte {update ? 'ändras' : 'skapas'} på grund av ett tekniskt fel. Prova igen om en stund.
              </IaAlert>
            </ErrorWrapper>
          )}
        </ErrorSection>
        <ModalFooter className="no-border">
          <Button
            id="saveBanner"
            disabled={!enableSaveBtn()}
            color={'primary'}
            onClick={() => {
              send()
            }}>
            {update ? 'Ändra' : 'Skapa'}
          </Button>
          <Button
            id="closeBanner"
            color={'default'}
            onClick={() => {
              cancel()
            }}>
            Avbryt
          </Button>
        </ModalFooter>
      </Modal>
  )
}

export const CreateBannerId = 'createBanner'

const mapStateToProps = (state) => {
  return {
    futureBanners: getFutureBanners(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(CreateBannerId)
)(CreateBanner)
