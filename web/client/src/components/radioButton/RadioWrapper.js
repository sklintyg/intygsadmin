import React from 'react'
import * as PropTypes from 'prop-types'
import RadioButton from './RadioButton'

const RadioWrapper = ({ radioButtons, selected, onChange }) => {
  let numberOfRadioButtons = radioButtons.length;
  return (
    <>
      {radioButtons.map((rb, i) => {
        return (
          <span key={i}>
            <RadioButton label={rb.label} selected={selected} onChange={onChange} value={rb.value}/>
            {(numberOfRadioButtons > 2 ? <br/> : null)}
          </span>
        )
      })}
    </>
  )
}

RadioWrapper.propTypes = {
  radioButtons: PropTypes.array,
}

export default RadioWrapper
