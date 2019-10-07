import React, {useEffect, useRef, useState} from 'react'
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
`

const roleButtons = Object.entries(AppConstants.role).map(([key, value]) => {
  return {
    id: `role${key}`,
    label: value,
    value: key
  }
})

const initialUser = {
  name: undefined,
  employeeHsaId: undefined,
  intygsadminRole: undefined
}

const CreateUser = ({ handleClose, isOpen, onComplete, createUser, updateUser, data }) => {
  //const [validationMessages, setValidationMessages] = useState({})
  const [update, setUpdate] = useState(false)
  const [newUser, setNewUser] = useState(initialUser)
  const [errorActive, setErrorActive] = useState(false)

  useEffect(() => {
    if (data && data.user) {
      setUpdate(true)
      setNewUser({...data.user})
    } else {
      setUpdate(false)
    }
  }, [data])

  const previousBanner = useRef()
  useEffect(() => {
    previousBanner.current = newUser
  })

  const onChange = (value, prop) => {
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
      .catch(() => {
        setErrorActive(true)
      })
  }

  const cancel = () => {
    setErrorActive(false)
    setNewUser(initialUser)
    handleClose()
  }

  const enableSaveBtn = () => {

    const fields = ['name', 'employeeHsaId', 'intygsadminRole']

    let enable = fields.reduce((accumulator, currentValue) => {
      return accumulator && newUser[currentValue]
    }, true)

    //enable = enable && isEmpty(validationMessages)

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
        <ModalHeader toggle={cancel}>{update ? 'Ändra administratörsuppgifter' : 'Lägg till administratör'}</ModalHeader>
        <StyledBody>
          <FormGroup>
            <Label for='userName'><IaTypo04>Administratörens namn</IaTypo04></Label>
            <Input
              id='userName'
              value={newUser.name}
              onChange={(e) => onChange(e.target.value, 'name')}
            />
          </FormGroup>

          <FormGroup>
            <Label for='userHsaId'><IaTypo04>Administratörens HSA-ID</IaTypo04></Label>
            <Input
              id='userHsaId'
              placeholder='SE1234567890-1X23'
              value={newUser.employeeHsaId}
              onChange={(e) => onChange(e.target.value, 'employeeHsaId')}
            />
          </FormGroup>

          <FormGroup>
            <HelpChevron label='Behörighetsnivå'>
              <p>Administratörer med basbehörighet kan inte lägga till och hantera administratörer.</p>
              <p>Administratörer med fullständig behörighet kan lägga till och hantera administratörer.</p>
            </HelpChevron>
            <RadioWrapper
              radioButtons={roleButtons}
              onChange={(event) => onChange(event.target.value, 'intygsadminRole')}
              selected={newUser.intygsadminRole}
            />
          </FormGroup>
        </StyledBody>
        <ErrorSection>
          {errorActive && (
            <ErrorWrapper>
              <IaAlert type={alertType.ERROR}>
                {
                  update ?
                    'Kunde inte ändra administratörens uppgifter på grund av ett tekniskt fel. Prova igen om en stund.'
                    :
                    'Kunde inte lägga till administratören på grund av ett tekniskt fel. Prova igen om en stund.'
                }
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
            {update ? 'Ändra' : 'Lägg till'}
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

export const CreateUserId = 'createUser'

export default compose(
  connect(
    null,
    { ...actions }
  ),
  modalContainer(CreateUserId)
)(CreateUser)
