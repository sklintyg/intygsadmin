import React, {useEffect, useState} from 'react'
import {Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import {compose} from 'recompose'
import * as actions from '../../../store/actions/users'
import {connect} from 'react-redux'
import styled from 'styled-components'
import {ErrorSection, ErrorWrapper} from '../../styles/iaLayout'
import IaAlert, {alertType} from '../../alert/Alert'
import {IaTypo04} from "../../styles/iaTypography";
import HelpChevron from "../../helpChevron";
import {RadioWrapper} from "../../radioButton";
import AppConstants from "../../../AppConstants";
import HsaInput from "../../styles/HsaInput";
import {getErrorMessageModal} from "../../../store/reducers/users";
import {getMessage} from "../../../messages/messages";

const StyledBody = styled(ModalBody)`
  
  .form-control {
    width: 100%;
  }
  
  .form-group {
    margin-bottom: 15px;
  }

  h5 {
    padding: 12px 0 4px;
    &:first-of-type {
      padding: 4px 0;
    }
  }
  
  label {
    display: block;
  }
`

const roleButtons = Object.entries(AppConstants.role).map(([key, value]) => {
  return {
    id: `role${key}`,
    label: value,
    value: key
  }
})

const initialUser = {
  name: "",
  employeeHsaId: "",
  intygsadminRole: undefined
}

const CreateUser = ({ handleClose, isOpen, onComplete, createUser, updateUser, data, errorMessage, clearError }) => {
  const [update, setUpdate] = useState(false)
  const [messagePrefix, setMessagePrefix] = useState('user.create')
  const [newUser, setNewUser] = useState(initialUser)

  useEffect(() => {
    if (data && data.user) {
      setMessagePrefix('user.update')
      setUpdate(true)
      setNewUser({...data.user})
    } else {
      setMessagePrefix('user.create')
      setUpdate(false)
    }
  }, [data])

  const onChange = (prop) => (value) => {
    setNewUser({ ...newUser, [prop]: value })
  }

  const createSendObject = () => {
    return {
      name: newUser.name,
      employeeHsaId: newUser.employeeHsaId,
      intygsadminRole: newUser.intygsadminRole
    }
  }

  const send = () => {
    const func = update ? updateUser(createSendObject(), data.user.id) : createUser(createSendObject());

    func
      .then(() => {
        cancel()
        onComplete()
      })
      .catch(() => {})
  }

  const cancel = () => {
    clearError()
    setNewUser(initialUser)
    handleClose()
  }

  const enableSaveBtn = () => {

    const fields = ['name', 'employeeHsaId', 'intygsadminRole']

    let enable = fields.reduce((accumulator, currentValue) => {
      return accumulator && newUser[currentValue]
    }, true)

    if (update) {
      let changed = fields.reduce((accumulator, currentValue) => {
        let compareValue = newUser[currentValue]

        return accumulator || (data.user && data.user[currentValue] !== compareValue)
      }, false)

      enable = enable && changed
    }

    return enable
  }

  return (
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={cancel}>
        <ModalHeader toggle={cancel}> {getMessage(`${messagePrefix}.title`)}</ModalHeader>
        <StyledBody>
          <FormGroup>
            <Label for='userName'><IaTypo04>{getMessage(`${messagePrefix}.name`)}</IaTypo04></Label>
            <Input
              id='userName'
              value={newUser.name}
              size={200}
              onChange={(e) => onChange('name')(e.target.value)}
            />
          </FormGroup>

          <FormGroup>
            <Label for='userHsaId'><IaTypo04>{getMessage(`${messagePrefix}.hsaId`)}</IaTypo04></Label>
            <HsaInput id='userHsaId' value={newUser.employeeHsaId} onChange={onChange('employeeHsaId')} />
          </FormGroup>

          <FormGroup>
            <HelpChevron label={getMessage(`${messagePrefix}.role`)}>
              <p>Administratörer med basbehörighet kan inte lägga till och hantera administratörer.</p>
              <p>Administratörer med fullständig behörighet kan lägga till och hantera administratörer.</p>
            </HelpChevron>
            <RadioWrapper
              radioButtons={roleButtons}
              onChange={(event) => onChange('intygsadminRole')(event.target.value)}
              selected={newUser.intygsadminRole}
            />
          </FormGroup>
        </StyledBody>
        <ErrorSection>
          {errorMessage && (
            <ErrorWrapper>
              <IaAlert type={alertType.ERROR}>
                {errorMessage}
              </IaAlert>
            </ErrorWrapper>
          )}
        </ErrorSection>
        <ModalFooter className="no-border">
          <Button
            id="saveUser"
            disabled={!enableSaveBtn()}
            color={'primary'}
            onClick={() => {
              send()
            }}>
            {getMessage(`${messagePrefix}.save`)}
          </Button>
          <Button
            id="closeModal"
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

const mapStateToProps = (state) => {
  return {
    errorMessage: getErrorMessageModal(state)
  }
}

export const CreateUserId = 'createUser'

export default compose(
  connect(
    mapStateToProps,
    { ...actions }
  ),
  modalContainer(CreateUserId)
)(CreateUser)
