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

const CreateBanner = ({ handleClose, isOpen, createBanner }) => {
  const [tjanst, setTjanst] = useState(undefined)
  const [prio, setPrio] = useState(undefined)
  const [fromDate, setFromDate] = useState(undefined)
  const [toDate, setToDate] = useState(undefined)
  const [meddelande, setMeddelande] = useState('')

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
        <ModalBody>
          <h5>Välj tjänst</h5>
          <RadioWrapper radioButtons={tjanstButtons} onChange={onTjanstChange} selected={tjanst} />
          <h5>Skriv meddelandetext</h5>
          <CustomTextarea onChange={onMeddelandeChange} />
          <h5>Ange visningsperiod</h5>
          Från:
          <DatePicker date={fromDate} onChange={onFromDateChange} />
          <TimePicker date={fromDate} onChange={onFromDateChange} />
          <br />
          Till:
          <DatePicker date={toDate} onChange={onToDateChange} />
          <TimePicker date={toDate} onChange={onToDateChange} />
          <h5>Välj prioritet</h5>
          <RadioWrapper radioButtons={prioButtons} onChange={onPrioChange} selected={prio} />
        </ModalBody>
        <ModalFooter>
          <Button
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
