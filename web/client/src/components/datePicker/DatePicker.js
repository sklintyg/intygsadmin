import React, { useState, useEffect, useRef } from 'react'
import ReactDatePicker from 'react-datepicker'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { Calendar } from '../styles/iaSvgIcons'
import colors from '../styles/iaColors'
import sv from 'date-fns/locale/sv'

const StyledButton = styled(Button)`
  margin-left: 0px !important
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
    > button,
    > button:focus {
      border-color: ${colors.IA_COLOR_16};
    }
  }
`

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  button {
    flex: 0 0 70px;
    margin-right: 12px;
    margin-bottom: 12px;
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

const DatePicker = ({ date, onChange, className }) => {
  const [datePickerPopupOpen, setDatePickerPopupOpen] = useState(false)
  const [internalValue, setInternalValue] = useState('')
  const popupRef = useRef(null)
  const buttonHolderRef = useRef(null)
  const [hasLostFocus, setHasLostFocus] = useState(false)

  const change = (event) => {
    setInternalValue(event.target.value)
    if (hasLostFocus){
      onChange(event.target.value)
    }
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
    setHasLostFocus(true)
    onChange(event.target.value)
  }

  return (
    <DatePickerContainer>
      <Container className={className}>
        <StyledInput type="text" value={internalValue} onChange={change} placeholder={'åååå-mm-dd'} onBlur={handleBlur} />
        <span ref={buttonHolderRef}>
          <StyledButton onClick={onClick} color={'default'}>
            <Calendar />
          </StyledButton>
        </span>
      </Container>
      <PopUp ref={popupRef} className={datePickerPopupOpen ? 'open' : 'closed'}>
        <DatePickerPopup onChange={onChange} date={internalValue} open={datePickerPopupOpen} onSelect={onClick} />
      </PopUp>
    </DatePickerContainer>
  )
}

const DatePickerPopup = ({ onChange, date, open, onSelect }) => {
  const [internalDate, setInternalDate] = useState(undefined)

  useEffect(() => {
    if (date && open && (date.match(/(\d{4}-(\d{2})-(\d{2}))/) && date.length === 10)) {
      let newDate = new Date(date)
      setInternalDate(newDate)
    } else {
      setInternalDate(new Date())
    }
  }, [date, open])

  const clear = () => {
    onSelect()
    onChange('')
    setInternalDate(undefined)
  }

  const handleChange = (value) => {
    onChange(value.toLocaleDateString('sv-SE'))
  }

  const handleOk = () => {
    onChange(internalDate.toLocaleDateString('sv-SE'))
    onSelect()
  }

  return (
    <>
      <ReactDatePicker
        selected={internalDate}
        dateFormat={'yyyy-MM-dd'}
        onChange={handleChange}
        locale={sv}
        showWeekNumbers
        inline
        onSelect={onSelect}
      />
      <ButtonContainer>
        <Button color={'default'} onClick={clear}>
          Rensa
        </Button>
        <Button color={'success'} onClick={handleOk}>
          OK
        </Button>
      </ButtonContainer>
    </>
  )
}

export default DatePicker
