import React from 'react';
import styled from "styled-components";
import {HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
import IbColors from "../../styles/IbColors";
import {UnitIcon} from "../../styles/IbSvgIcons";

const UnitComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  padding: 0 16px;
  color: ${IbColors.IB_COLOR_20};
  height: 100%;
`
const VardgivarTitle = styled.div`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-left: 4px;
`

const VardenhetTitle = styled.div`
  white-space: nowrap;

`

const Unit = ({vg, ve}) => {
  return (
    <UnitComponentWrapper>
      <UnitIcon />
      <VerticalContainer>
        <SingleTextRowContainer>
          <VardgivarTitle id="currentVardgivarTitle">{vg}</VardgivarTitle>
          <VardenhetTitle id="currentVardenhetTitle">&nbsp;-{ve}</VardenhetTitle>
        </SingleTextRowContainer>
      </VerticalContainer>
    </UnitComponentWrapper>
  )
};

export default Unit;
