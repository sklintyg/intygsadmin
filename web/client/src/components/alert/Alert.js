import React from 'react';
import PropTypes from "prop-types";
import {InfoIcon, Security, Check, ErrorOutline, Warning} from "../styles/IaSvgIcons";
import IaColors from "../styles/IaColors";
import styled from 'styled-components'

export const alertType = {
  INFO: 'info',
  SEKRETESS: 'sekretess',
  OBSERVANDUM: 'observandum',
  CONFIRM: 'confirm',
  ERROR: 'error'
}

function getColor(type) {
  switch (type) {
  case alertType.INFO:
    return {text:IaColors.AG_COLOR_19, background:IaColors.AG_COLOR_18};
  case alertType.SEKRETESS:
    return {text:IaColors.AG_COLOR_21, background:IaColors.AG_COLOR_20};
  case alertType.OBSERVANDUM:
    return {text:IaColors.AG_COLOR_21, background:IaColors.AG_COLOR_20};
  case alertType.CONFIRM:
    return {text:IaColors.AG_COLOR_97, background:IaColors.AG_COLOR_98};
  case alertType.ERROR:
    return {text:IaColors.AG_COLOR_23, background:IaColors.AG_COLOR_22};
  default:
    return {text:'#fff', background:'#000'};
  }
}

const Alert = styled.div`
  color: ${props => getColor(props.type).text};
  background-color: ${props => getColor(props.type).background};
  position: relative;
  border-radius: 4px;
  display: inline-block;
  svg {
    position: absolute;
    top: 2px;
    width: 16px;
  }
  > div {
    margin-left: 20px;
  }
  padding: 4px 8px;
`

const IbAlert = ({type, children, className}) => {
  function getIcon(type) {
    switch (type) {
    case alertType.INFO:
      return <InfoIcon color={IaColors.AG_COLOR_19} />;
    case alertType.SEKRETESS:
      return <Security color={IaColors.AG_COLOR_21} />;
    case alertType.OBSERVANDUM:
      return <ErrorOutline color={IaColors.AG_COLOR_21} />;
    case alertType.CONFIRM:
      return <Check color={IaColors.AG_COLOR_97} />;
    case alertType.ERROR:
      return <Warning color={IaColors.AG_COLOR_23} />;
    default:
      return null;
    }
  }

  return (
    <Alert type={type} className={className}>
      {getIcon(type)}
      <div>{children}</div>
    </Alert>
  );
}

IbAlert.propTypes = {
  type: PropTypes.oneOf(Object.values(alertType)).isRequired
}

export default IbAlert;
