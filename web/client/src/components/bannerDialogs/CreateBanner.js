import React, { Fragment, useState } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import { RadioWrapper } from '../radioButton'
import CustomTextarea from '../CustomTextarea'
import DatePicker from '../datePicker'
import * as actions from '../../store/actions/banner'
import { connect } from 'react-redux'
import TimePicker from '../timePicker'
import Toggler from '../toggler/Toggler'
import styled from 'styled-components'
import colors from '../styles/iaColors'
import { IaTypo05, IaTypo04 } from '../styles/iaTypography'

const StyledBody = styled(ModalBody)`
  h5 {
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
  }
`

const HelpDiv = styled.div`
  &.visible {
    display: block;
  }
  &.hidden {
    display: none;
  }

  border-radius: 2px;
  background-color: ${colors.IA_COLOR_15};
  max-height: 160px;
  padding: 12px 18px;
  overflow-y: scroll;
  margin-bottom: 12px;
`

const HelpRoof = styled.div`
  margin-top: -25px;
  &.visible {
    display: block;
  }
  &.hidden {
    display: none;
  }
  height: 20px;
  width: 20px;
  border: 10px solid ${colors.IA_COLOR_15};
  margin-left: 20px;
  border-top: 10px solid #fff;
  border-left: 10px solid #fff;
  border-right: 10px solid #fff;
`

const HelpHeader = styled(IaTypo04)`
  color: ${colors.IA_COLOR_06}
  padding: 4px 0;
`

const HelpText = styled(IaTypo05)`
  color: ${colors.IA_COLOR_06}
  padding: 4px 0;
`

const CreateBanner = ({ handleClose, isOpen, createBanner }) => {
  const [tjanst, setTjanst] = useState(undefined)
  const [prio, setPrio] = useState(undefined)
  const [fromDate, setFromDate] = useState(undefined)
  const [toDate, setToDate] = useState(undefined)
  const [meddelande, setMeddelande] = useState('')
  const [prioHelpExpanded, setPrioHelpExpanded] = useState(false)

  const onTjanstChange = (e) => {
    setTjanst(e.target.value)
  }
  const onPrioChange = (e) => {
    setPrio(e.target.value)
  }
  const onMeddelandeChange = (value) => {
    setMeddelande(value)
  }
  const onFromDateChange = (value) => {
    setFromDate(value)
  }
  const onToDateChange = (value) => {
    setToDate(value)
  }

  const prioHelpToggler = () => {
    setPrioHelpExpanded(!prioHelpExpanded)
  }

  const send = () => {
    createBanner({
      application: tjanst,
      message: meddelande,
      displayFrom: fromDate.toLocaleString().replace(' ', 'T'),
      displayTo: toDate.toLocaleString().replace(' ', 'T'),
      priority: prio,
    }).then(() => handleClose())
  }

  const tjanstButtons = [
    { label: 'Intygsstatistik', value: 'STATISTIK' },
    { label: 'Rehabstöd', value: 'REHABSTOD' },
    { label: 'Webcert', value: 'WEBCERT' },
  ]

  const prioButtons = [{ label: 'Låg', value: 'LOW' }, { label: 'Medel', value: 'MEDIUM' }, { label: 'Hög', value: 'HIGH' }]

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Skapa driftbanner</ModalHeader>
        <StyledBody>
          <h5>Välj tjänst</h5>
          <RadioWrapper radioButtons={tjanstButtons} onChange={onTjanstChange} selected={tjanst} />
          <h5>Skriv meddelandetext</h5>
          <CustomTextarea onChange={onMeddelandeChange} />
          <h5>Ange visningsperiod</h5>
          Från <DatePicker date={fromDate} onChange={onFromDateChange} />
          <TimePicker date={fromDate} onChange={onFromDateChange} />
          <br />
          till <DatePicker date={toDate} onChange={onToDateChange} />
          <TimePicker date={toDate} onChange={onToDateChange} />
          <h5>
            Välj prioritet<Toggler expanded={prioHelpExpanded} handleToggle={prioHelpToggler} />
          </h5>
          <HelpRoof className={prioHelpExpanded ? 'visible' : 'hidden'}></HelpRoof>
          <HelpDiv className={prioHelpExpanded ? 'visible' : 'hidden'}>
            <HelpHeader>Låg prioritet</HelpHeader>
            <HelpText>Används för information som inte påverkar användandet av tjänsten. En blå driftbanner visas.</HelpText>
            <HelpHeader>Medel prioritet</HelpHeader>
            <HelpText>
              Används för information som användaren bör uppmärksamma och för händelser som inte påverkar användandet av tjänsten, till
              exempel vid kommande driftstörningar. En gul driftbanner visas.
            </HelpText>
            <HelpHeader>Hög prioritet</HelpHeader>
            <HelpText>
              Används för information som användaren behöver uppmärksamma och för händelser som påverkar användandet av tjänsten, till
              exempel vid pågående driftstörningar. En röd driftbanner visas.
            </HelpText>
          </HelpDiv>
          <RadioWrapper radioButtons={prioButtons} onChange={onPrioChange} selected={prio} />
        </StyledBody>
        <ModalFooter>
          <Button
            disabled={!(tjanst && meddelande && fromDate && toDate && prio)}
            color={'primary'}
            onClick={() => {
              send()
            }}>
            Skapa
          </Button>
          <Button
            color={'default'}
            onClick={() => {
              handleClose()
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
