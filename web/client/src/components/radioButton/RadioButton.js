import React, { Fragment } from 'react'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import { IbTypo07 } from '../styles/IbTypography'

const RadioWrapper = styled.div`
  display: inline-block;
  padding: 0 22px 12px 22px;
`

const Label = styled(IbTypo07)`
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
    border: 1px solid ${IbColors.IB_COLOR_23};
    box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.24);
    background-color: ${IbColors.IB_COLOR_00};
  }

  .dot {
    display: none;
    border: none;
    background-color: ${IbColors.IB_COLOR_00};
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
    background-color: ${IbColors.IB_COLOR_21};
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
      color: ${IbColors.IB_COLOR_09};
    }

    & + ${Label} .circle {
      background-color: ${IbColors.IB_COLOR_20};
      color: ${IbColors.IB_COLOR_09};
      cursor: not-allowed;
    }

    & + ${Label} .dot {
      background-color: ${IbColors.IB_COLOR_09};
    }
  }
`

const RadioContainer = styled.div`
  position: relative;
`
const RadioButton = ({ selected, onChange, label, value }) => {
  return (
    <Fragment>
      <RadioWrapper>
        <RadioContainer>
          <Input type="radio" name={label} id={label} value={value} checked={selected === value} onChange={onChange} />
          <Label as="label" htmlFor={label}>
            <div className="circle" />
            {label}
            <div className="dot" />
          </Label>
        </RadioContainer>
      </RadioWrapper>
    </Fragment>
  )
}

export default RadioButton
