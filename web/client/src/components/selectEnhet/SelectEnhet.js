import React from 'react';
import styled from 'styled-components'
import PropTypes from "prop-types";
import Vardgivare from "./Vardgivare";
import IbAlert, {alertType} from "../alert/Alert";
import ErrorMessageFormatter from '../../messages/ErrorMessageFormatter'


const ComponentWrapper = styled.div`
  padding: 8px;
  max-width: 500px;
`

function SelectEnhet({authoritiesTree, unitContext, activeError, selectEnhet}) {
  return (
    <ComponentWrapper>
      {activeError &&
      <IbAlert type={alertType.ERROR}><ErrorMessageFormatter error={activeError}/></IbAlert>}
      {authoritiesTree.map(vg => {
          return (<Vardgivare key={vg.id}
                              vg={vg}
                              initiallyExpanded={true}
                              unitContext={unitContext}
                              handleSelect={selectEnhet} />
          );
        }
      )}
    </ComponentWrapper>
  )
}

SelectEnhet.propTypes = {
  authoritiesTree: PropTypes.array.isRequired,
  selectEnhet: PropTypes.func.isRequired,
  unitContext: PropTypes.object,
  activeError: PropTypes.object
}

export default SelectEnhet;
