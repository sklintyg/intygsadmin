import React, { useState, useEffect } from 'react'
import ReactDatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { Calendar } from '../styles/iaSvgIcons'
import colors from '../styles/iaColors'

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

class DatePickerInput extends React.Component {
  render() {
    return (
      <Container className={this.props.className}>
        <StyledInput
          type="text"
          value={this.props.value}
          onChange={this.props.onChange}
          placeholder={'åååå-mm-dd'}
        />
        <StyledButton onClick={this.props.onClick} color={'default'}>
          <Calendar />
        </StyledButton>
      </Container>
    )
  }
}

const DatePicker = ({ date, onChange, className}) => {
  const [internalDate, setInternalDate] = useState(undefined)
  const change = (event) => {
    onChange(event.target.value)
  }

  useEffect(() => {
    console.log(date)
    if (date && date.getHours) {
      setInternalDate(date)
    }
  }, [date])
  return (
    <ReactDatePicker
      customInput={<DatePickerInput />}
      selected={internalDate}
      dateFormat={'yyyy-MM-dd'}
      onChange={onChange}
      onChangeRaw={change}
      className={className}
    />
  )
}

export default DatePicker
