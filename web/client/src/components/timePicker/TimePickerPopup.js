import React, { useState, useEffect, useRef } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { CollapseIcon, ExpandIcon } from '../styles/iaSvgIcons'

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

const TimePickerPopup = ({ value, onChange }) => {
  const [internalValue, setInternalValue] = useState('')
  const popup = useRef()
  const buttonHolderRef = useRef()
  const [hours, setHours] = useState(undefined)
  const [minutes, setMinutes] = useState(undefined)
  const [popupOpen, setPopupOpen] = useState(false)

  useEffect(() => {
    if (value) {
      setInternalValue(value)
    }
  }, [value])

  useEffect(() => {
    if (hours && minutes) {
      setInternalValue(hours + ':' + minutes)
    }
  }, [hours, minutes])


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

  return (
    <>
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
    </>
  )
}

export default TimePickerPopup
