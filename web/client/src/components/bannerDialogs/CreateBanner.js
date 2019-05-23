import React, { Fragment, useState, useEffect, useRef } from 'react'
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
  { label: 'Intygsstatistik', value: 'STATISTIK' },
  { label: 'Rehabstöd', value: 'REHABSTOD' },
  { label: 'Webcert', value: 'WEBCERT' },
]

const prioButtons = [{ label: 'Låg', value: 'LOW' }, { label: 'Medel', value: 'MEDIUM' }, { label: 'Hög', value: 'HIGH' }]

const CreateBanner = ({ handleClose, isOpen, createBanner }) => {
  const [validationMessages, setValidationMessages] = useState({})
  const [banner, setBanner] = useState(initialBanner)

  useEffect(() => {
    if (isEqual(previousBanner.current, banner)) {
      return
    }
    setValidationMessages(validateBanner(banner))
  }, [banner])

  const previousBanner = useRef()
  useEffect(() => {
    previousBanner.current = banner
  })

  const onChange = (value, prop) => {
    setBanner({ ...banner, [prop]: value })
  }

  const send = () => {
    createBanner({
      application: banner.application,
      message: banner.message,
      displayFrom: banner.displayFrom.toLocaleDateString('sv-SE') + 'T' + banner.displayFromTime,
      displayTo: banner.displayTo.toLocaleDateString('sv-SE') + 'T' + banner.displayToTime,
      priority: banner.prio,
    }).then(() => cancel())
  }

  const cancel = () => {
    setBanner(initialBanner)
    handleClose()
  }

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
        <ModalHeader toggle={cancel}>Skapa driftbanner</ModalHeader>
        <StyledBody>
          <h5>Välj tjänst</h5>
          <RadioWrapper
            radioButtons={tjanstButtons}
            onChange={(event) => onChange(event.target.value, 'application')}
            selected={banner.application}
          />
          <h5>Skriv meddelandetext</h5>
          <CustomTextarea onChange={(value) => onChange(value, 'message')} value={banner.message}></CustomTextarea>
          <h5>Ange visningsperiod</h5>
          <FlexDiv>
            <span>Från</span>
            <span>
              <DatePicker
                date={banner.displayFrom}
                onChange={(value) => onChange(value, 'displayFrom')}
                className={validationMessages.displayFrom ? 'error' : ''}
              />
            </span>
            <span>
              <TimePicker
                date={banner.displayFromTime}
                onChange={(value) => onChange(value, 'displayFromTime')}
                className={validationMessages.displayFromTime ? 'error' : ''}
              />
            </span>
          </FlexDiv>
          <ValidationMessage>{validationMessages.displayFrom}</ValidationMessage>
          <ValidationMessage>{validationMessages.displayFromTime}</ValidationMessage>
          <FlexDiv>
            <span>till</span>
            <span>
              <DatePicker
                date={banner.displayTo}
                onChange={(value) => onChange(value, 'displayTo')}
                className={validationMessages.displayTo ? 'error' : ''}
              />
            </span>
            <span>
              <TimePicker
                date={banner.displayToTime}
                onChange={(value) => onChange(value, 'displayToTime')}
                className={validationMessages.displayToTime ? 'error' : ''}
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
          <RadioWrapper radioButtons={prioButtons} onChange={(event) => onChange(event.target.value, 'prio')} selected={banner.prio} />
        </StyledBody>
        <ModalFooter>
          <Button
            disabled={
              !(
                banner.application &&
                banner.message &&
                banner.displayFrom &&
                banner.displayTo &&
                banner.prio &&
                isEmpty(validationMessages)
              )
            }
            color={'primary'}
            onClick={() => {
              send()
            }}>
            Skapa
          </Button>
          <Button
            color={'default'}
            onClick={() => {
              cancel()
            }}>
            Avbryt
          </Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

export const CreateBannerId = 'createBanner'

export default compose(
  connect(
    null,
    { ...actions }
  ),
  modalContainer(CreateBannerId)
)(CreateBanner)
