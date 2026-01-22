import React, { useEffect, useRef, useState } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { Calendar } from '../styles/iaSvgIcons'
import colors from '../styles/iaColors'
import DatePickerPopup from './DatePickerPopup'

const StyledButton = styled(Button)`
  margin-left: 0 !important;
  border-radius: 0 4px 4px 0 !important;
`

const StyledInput = styled.input`
  margin-right: 0px !important;
  border-right: 0 !important;
  border-radius: 4px 0 0 4px !important;
  width: 100px !important;
`

const Container = styled.div`
  &.error {
    > input,
    > input:focus,
    > span button,
    > span button:focus {
      border-color: ${colors.IA_COLOR_16};
    }
  }
`

const PopUp = styled.div`
  position: absolute;
  width: 365px;
  top: 40px;
  left: 0;
  z-index: 1;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 1px 1px 4px 4px rgba(0, 0, 0, 0.12);
  &.open {
    display: block;
  }
  &.closed {
    display: none;
  }
`

const DatePickerContainer = styled.div`
  position: relative;
  display: inline-block;
`

const DatePicker = ({ date, onChange, className, inputId }) => {
  const [datePickerPopupOpen, setDatePickerPopupOpen] = useState(false)
  const [internalValue, setInternalValue] = useState('')
  const popupRef = useRef(null)
  const buttonHolderRef = useRef(null)
  const [hasLostFocus, setHasLostFocus] = useState(false)

  const change = (event) => {
    const value = event.target.value

    if (!value) {
      setHasLostFocus(false)
    }

    setInternalValue(value)
    if (hasLostFocus) {
      onChange(value)
    }
  }

  const onChangeDatePicker = (value) => {
    setHasLostFocus(true)
    onChange(value)
  }

  const onClick = () => {
    setDatePickerPopupOpen(!datePickerPopupOpen)
  }

  useEffect(() => {
    if (date !== undefined) {
      setInternalValue(date)
    }
  }, [date])

  useEffect(() => {
    const listener = (event) => {
      if (
        !popupRef.current ||
        popupRef.current.contains(event.target) ||
        !buttonHolderRef.current ||
        buttonHolderRef.current.contains(event.target)
      ) {
        return
      }
      setDatePickerPopupOpen(false)
    }
    if (datePickerPopupOpen) {
      document.addEventListener('mousedown', listener)
    }

    return () => {
      document.removeEventListener('mousedown', listener)
    }
  }, [datePickerPopupOpen])

  const handleBlur = (event) => {
    if (event.target.value) {
      setHasLostFocus(true)
    }
    onChange(event.target.value)
  }

  return (
    <DatePickerContainer>
      <Container className={className}>
        <StyledInput id={inputId} type="text" value={internalValue} onChange={change} placeholder={'åååå-mm-dd'} onBlur={handleBlur} />
        <span ref={buttonHolderRef}>
          <StyledButton onClick={onClick} color={'default'}>
            <Calendar />
          </StyledButton>
        </span>
      </Container>
      <PopUp ref={popupRef} className={datePickerPopupOpen ? 'open' : 'closed'}>
        <DatePickerPopup onChange={onChangeDatePicker} date={internalValue} open={datePickerPopupOpen} onSelect={onClick} />
      </PopUp>
    </DatePickerContainer>
  )
}

export default DatePicker
