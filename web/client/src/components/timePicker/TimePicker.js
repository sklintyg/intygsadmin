import React, { useState, useEffect, useRef } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { TimeIcon, CollapseIcon, ExpandIcon } from '../styles/iaSvgIcons'
import colors from '../styles/iaColors'

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

const TimeDiv = styled.div`
  display: flex;
  text-align: center;
  justify-content: center;
  span {
    flex: 0 1 20px;
    &.col {
      flex: 0 1 10px;
    }
  }
`

const ArrowDiv = styled.div`
  text-align: center;
`

const TimePicker = ({ value, onChange, className, inputId }) => {
  const [internalValue, setInternalValue] = useState('')
  const popup = useRef()
  const buttonHolderRef = useRef()
  const [hours, setHours] = useState(undefined)
  const [minutes, setMinutes] = useState(undefined)
  const [popupOpen, setPopupOpen] = useState(false)
  const [hasLostFocus, setHasLostFocus] = useState(false)

  useEffect(() => {
    if (value) {
      setInternalValue(value)
    }
  }, [value])

  const parseTimeFromValue = () => {
    if (internalValue.match('^([0-1][0-9]|2[0-3]):([0-5][0-9])$')) {
      setHours(internalValue.split(':')[0])
      setMinutes(internalValue.split(':')[1])
    } else {
      setHours('00')
      setMinutes('00')
    }
  }

  useEffect(() => {
    if (hours && minutes) {
      setInternalValue(hours + ':' + minutes)
    }
  }, [hours, minutes])

  const inputOnChange = (event) => {
    let inputValue = event.target.value
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
    parseTimeFromValue()
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

  const hoursUp = () => {
    let newHours = parseInt(hours) + 1
    if (newHours === 24) newHours = 0
    setHours(newHours < 10 ? '0' + newHours : newHours)
  }

  const minutesUp = () => {
    let newMinutes = parseInt(minutes) + 10
    if (newMinutes > 59) newMinutes -= 60
    setMinutes(newMinutes < 10 ? '0' + newMinutes : newMinutes)
  }

  const hoursDown = () => {
    let newHours = parseInt(hours) - 1
    if (newHours === -1) newHours = 23
    setHours(newHours < 10 ? '0' + newHours : newHours)
  }

  const minutesDown = () => {
    let newMinutes = parseInt(minutes) - 10
    if (newMinutes < 0) newMinutes += 60
    setMinutes(newMinutes < 10 ? '0' + newMinutes : newMinutes)
  }

  const handleBlur = (event) => {
    setHasLostFocus(true)
    onChange(event.target.value)
  }

  return (
    <Container className={className}>
      <StyledInput id={inputId} type="text" value={internalValue} onChange={inputOnChange} placeholder={'hh:mm'} onBlur={handleBlur} />
      <span ref={buttonHolderRef}>
        <StyledButton onClick={toggleTimePopup} color={'default'}>
          <TimeIcon />
        </StyledButton>
      </span>
      <Popup ref={popup} className={popupOpen ? 'open' : 'closed'}>
        <ArrowDiv>
          <Button color={'Link'} onClick={hoursUp}>
            <ExpandIcon />
          </Button>{' '}
          <Button color={'Link'} onClick={minutesUp}>
            <ExpandIcon />
          </Button>
        </ArrowDiv>
        <TimeDiv>
          <span>{hours}</span>
          <span className={'col'}>:</span>
          <span>{minutes}</span>
        </TimeDiv>
        <ArrowDiv>
          <Button color={'Link'} onClick={hoursDown}>
            <CollapseIcon />
          </Button>{' '}
          <Button color={'Link'} onClick={minutesDown}>
            <CollapseIcon />
          </Button>
        </ArrowDiv>
      </Popup>
    </Container>
  )
}

export default TimePicker
