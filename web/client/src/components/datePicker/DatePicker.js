import React, { useState } from 'react'
import { Button } from 'reactstrap'
import ReactDatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import sv from 'date-fns/locale/sv';

class DatePickerInput extends React.Component {
  render() {
    return (
      <>
        <input type="text" value={this.props.value} onChange={this.props.onChange}/>
        <Button onClick={this.props.onClick}>click</Button>
      </>
    )
  }
}

const DatePicker = () => {
  const [date, setDate] = useState(new Date())
  const handleChange = (date) => {
    setDate(date)
  }
  return <ReactDatePicker customInput={<DatePickerInput />} selected={date} locale={sv} dateFormat={'yyyy-MM-dd'} onChange={handleChange} />
}

export default DatePicker
