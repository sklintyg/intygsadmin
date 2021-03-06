import React from 'react'
import styled from 'styled-components'
import IaColors from '../styles/iaColors'
import { IaTypo05 } from '../styles/iaTypography'

const RadioWrapper = styled.div`
  display: inline-block;
  padding: 0 22px 12px 22px;
`

const Label = styled(IaTypo05)`
  display: inline-block;
  position: relative;
  padding: 0 0 0 6px;
  margin-bottom: 0;
  cursor: pointer;

  .circle,
  .dot {
    position: absolute;
    top: 0;
    left: -20px;
    padding: 0;
    width: 20px;
    height: 20px;
    border-radius: 50%;
  }

  .circle {
    content: '';
    border: 1px solid ${IaColors.IA_COLOR_11};
    box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.24);
    background-color: ${IaColors.IA_COLOR_00};
  }

  .dot {
    display: none;
    border: none;
    background-color: ${IaColors.IA_COLOR_00};
    height: 8px;
    width: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
    top: 6px;
    left: -14px;
  }
`

const Input = styled.input`
  opacity: 0.01; //Important: 0.01 is not practically visible, but if we set it to 0, protractor won't find it..
  z-index: 1;

  margin: 0;
  position: absolute;
  top: 0;
  left: -20px;
  width: 20px;
  height: 20px;

  cursor: pointer;

  &:checked + ${Label} .dot {
    display: block;
  }

  &:checked + ${Label} .circle {
    background-color: ${IaColors.IA_COLOR_05};
    box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    border: none;
  }

  &:focus + ${Label} .circle {
    outline: thin dotted;
    outline-offset: 0px;
  }

  &:disabled,
  fieldset[disabled] & {
    cursor: not-allowed;

    & + ${Label} {
      cursor: not-allowed;
      color: ${IaColors.IA_COLOR_12};
    }

    & + ${Label} .circle {
      background-color: ${IaColors.IA_COLOR_03};
      color: ${IaColors.IA_COLOR_12};
      cursor: not-allowed;
    }

    & + ${Label} .dot {
      background-color: ${IaColors.IA_COLOR_12};
    }
  }
`

const RadioContainer = styled.div`
  position: relative;
`

const RadioButton = ({ selected, onChange, inputId, label, value }) => {
  return (
    <RadioWrapper>
      <RadioContainer>
        <Input type="radio" name={label} id={inputId ? inputId : label} value={value} checked={selected === value} onChange={onChange} />
        <Label as="label" htmlFor={inputId ? inputId : label}>
          <div className="circle" />
          {label}
          <div className="dot" />
        </Label>
      </RadioContainer>
    </RadioWrapper>
  )
}

export default RadioButton
