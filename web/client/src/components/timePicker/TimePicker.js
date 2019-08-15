import {useEffect, useRef, useState} from 'react'
import styled from 'styled-components'
import {Button} from 'reactstrap'
import {TimeIcon} from '../styles/iaSvgIcons'
import colors from '../styles/iaColors'
import TimePickerPopup from "./TimePickerPopup";

const StyledButton = styled(Button)`
  margin-left: 0px !important
  border-radius: 0 4px 4px 0 !important;
`

const StyledInput = styled.input`
  margin-right: 0px !important;
  border-right: 0 !important;
  border-radius: 4px 0 0 4px !important;
  width: 60px !important;
`

const Container = styled.div`
  position: relative;
  display: inline-block;
  &.error {
    > input,
    > input:focus,
    > span button,
    > span button:focus {
      border-color: ${colors.IA_COLOR_16};
    }
  }
`

const Popup = styled.div`
  position: absolute;
  width: 120px;
  top: 40px;
  left: 0;
  z-index: 1;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
  &.open {
    display: block;
  }
  &.closed {
    display: none;
  }
`

const TimePicker = ({value, onChange, className, inputId}) => {
  const [internalValue, setInternalValue] = useState('')
  const popup = useRef()
  const buttonHolderRef = useRef()
  const [popupOpen, setPopupOpen] = useState(false)
  const [hasLostFocus, setHasLostFocus] = useState(false)

  useEffect(() => {
    if (value) {
      setInternalValue(value)
    }
  }, [value])

  const inputOnChange = (event) => {
    let inputValue = event.target.value

    if (!inputValue) {
      setHasLostFocus(false);
    }

    setInternalValue(inputValue)
    if (hasLostFocus) {
      onChange(inputValue)
    }
  }

  const toggleTimePopup = () => {
    if (popupOpen) {
      closeTimePopup()
    } else {
      openTimePopup()
    }
  }

  const openTimePopup = () => {
    setPopupOpen(true)
  }

  const closeTimePopup = () => {
    setPopupOpen(false)
    onChange(internalValue)
  }

  useEffect(() => {
    const listener = (event) => {
      if (
        !popup.current ||
        popup.current.contains(event.target) ||
        !buttonHolderRef.current ||
        buttonHolderRef.current.contains(event.target)
      ) {
        return
      }
      setPopupOpen(false)
      onChange(internalValue)
    }
    if (popupOpen) {
      document.addEventListener('mousedown', listener)
    }

    return () => {
      document.removeEventListener('mousedown', listener)
    }
  }, [popupOpen, onChange, internalValue])

  const handleBlur = (event) => {
    if (event.target.value) {
      setHasLostFocus(true)
    }
    onChange(event.target.value)
  }

  return (
    < Container
  className = {className} >
    < StyledInput
  id = {inputId}
  type = "text"
  value = {internalValue}
  onChange = {inputOnChange}
  placeholder = {'hh:mm'}
  onBlur = {handleBlur}
  />
  < span
  ref = {buttonHolderRef} >
    < StyledButton
  onClick = {toggleTimePopup}
  color = {'default'} >
    < TimeIcon / >
    < /StyledButton>
    < /span>
    < Popup
  ref = {popup}
  className = {popupOpen ? 'open' : 'closed'} >
    < TimePickerPopup
  open = {popupOpen}
  value = {internalValue}
  onChange = {setInternalValue}
  />
  < /Popup>
  < /Container>
)
}

export default TimePicker
