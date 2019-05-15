import React from 'react'
import * as PropTypes from 'prop-types'
import RadioButton from './RadioButton'

const RadioWrapper = ({ radioButtons, selected, onChange }) => {
  let numberOfRadioButtons = radioButtons.length;
  return (
    <>
      {radioButtons.map((rb, i) => {
        return (
        <>
          <RadioButton key={i} label={rb.label} selected={selected} onChange={onChange} value={rb.value}/>
          {(numberOfRadioButtons > 2 ? <br/> : null)}
          </>
        )
      })}
    </>
  )
}

RadioWrapper.propTypes = {
  radioButtons: PropTypes.array,
}

export default RadioWrapper
