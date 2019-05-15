import React from 'react'
import ReactDatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import sv from 'date-fns/locale/sv'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import { Calendar } from '../styles/iaSvgIcons'


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

class DatePickerInput extends React.Component {
  render() {
    return (
      <>
        <StyledInput type="text" value={this.props.value} onChange={this.props.onChange}/>
        <StyledButton onClick={this.props.onClick} color={'default'}><Calendar/></StyledButton>
      </>
    )
  }
}

const DatePicker = ({date, onChange}) => {
  return <ReactDatePicker customInput={<DatePickerInput />} selected={date} locale={sv} dateFormat={'yyyy-MM-dd'} onChange={onChange} placeholderText="åååå-mm-dd"/>
}

export default DatePicker
