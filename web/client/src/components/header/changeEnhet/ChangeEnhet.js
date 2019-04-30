import React, {Fragment} from 'react';
import PropTypes from "prop-types";
import SelectEnhet from "../../selectEnhet";
import {ActionButton} from "../styles";
import {ChangeUnitIcon} from "../../styles/IbSvgIcons";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';


const ChangeEnhet = ({handleOpen, handleClose, isOpen}) => {

  return (
    <Fragment>
      <ActionButton onClick={handleOpen} id="changeUnitBtn">
        <ChangeUnitIcon />
        <br />
        Byt enhet
      </ActionButton>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Välj enhet</ModalHeader>
        <ModalBody>
          <SelectEnhet />
        </ModalBody>
        <ModalFooter>
          <Button color={'secondary'} outline={true} onClick={handleClose}>Avbryt</Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  );
};

ChangeEnhet.propTypes = {
  handleOpen: PropTypes.func.isRequired,
  handleClose: PropTypes.func.isRequired,
  isOpen: PropTypes.bool.isRequired
};

export default ChangeEnhet;
