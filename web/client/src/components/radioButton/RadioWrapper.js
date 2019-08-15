import {Fragment} from 'react'
import * as PropTypes from 'prop-types'
import RadioButton from './RadioButton'

const RadioWrapper = ({radioButtons, selected, onChange}) => {
  let numberOfRadioButtons = radioButtons.length;
  return (
    < Fragment >
    {
      radioButtons.map((rb, i) => {
        const inputId = rb.id ? rb.id : rb.value;
        return (
          < span
        key = {i} >
          < RadioButton
        label = {rb.label}
        inputId = {inputId}
        selected = {selected}
        onChange = {onChange}
        value = {rb.value}
        />
        {
          (numberOfRadioButtons > 2 ? < br / >
        :
          null
        )
        }
      <
        /span>
      )
      })
    }
    < /Fragment>
)
}

RadioWrapper.propTypes = {
  radioButtons: PropTypes.array,
}

export default RadioWrapper
