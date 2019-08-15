import {useEffect, useState} from 'react'
import ReactDatePicker from 'react-datepicker'
import styled from 'styled-components'
import {Button} from 'reactstrap'
import sv from 'date-fns/locale/sv'

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  button {
    flex: 0 0 70px;
    margin-right: 12px;
    margin-bottom: 12px;
  }
`

const DatePickerPopup = ({onChange, date, open, onSelect}) => {
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

  const handleSelect = (value) => {
    // .replace(/[^ -~]/g, '') är en fix för att IE 11 lägger till extra tecken (LTR, RTL)
    onChange(value.toLocaleDateString('sv-SE').replace(/[^ -~]/g, ''))
    onSelect()
  }

  const handleOk = () => {
    onChange(internalDate.toLocaleDateString('sv-SE').replace(/[^ -~]/g, ''))
    onSelect()
  }

  return (
    < >
    < ReactDatePicker
  selected = {internalDate}
  dateFormat = {'yyyy-MM-dd'}
  locale = {sv}
  showWeekNumbers
  inline
  onSelect = {handleSelect}
  />
  < ButtonContainer >
  < Button
  color = {'default'}
  onClick = {clear} >
    Rensa
    < /Button>
    < Button
  color = {'success'}
  onClick = {handleOk} >
    OK
    < /Button>
    < /ButtonContainer>
    < />
)
}

export default DatePickerPopup
