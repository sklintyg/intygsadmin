import React from 'react';
import { connect } from 'react-redux';
import { compose } from 'recompose';
import { AddIcon } from '../styles/iaSvgIcons';
import IaColors from '../styles/iaColors';
import { Button, UncontrolledTooltip } from 'reactstrap';
import * as modalActions from '../../store/actions/modal';
import { CreateDataExportId } from './dialogs/CreateDataExport.dialog';

const DataExportActionBar = ({ openModal }) => {
  const createDataExport = () => {
    openModal(CreateDataExportId, { dataExport: undefined })
  };

  return (
    <>
      <Button id="createDataExportBtn" onClick={createDataExport} color={'success'}>
        <AddIcon color={IaColors.IA_COLOR_00} /> Skapa dataexport
      </Button>
      <UncontrolledTooltip trigger="hover" placement="auto" target="createDataExportBtn">
        Öppnar ett dialogfönster där du skapa en dataexport.
      </UncontrolledTooltip>
    </>
  )
};

export default compose(
  connect(
    null,
    { ...modalActions }
  )
)(DataExportActionBar);
