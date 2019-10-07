import React from 'react'
import * as PropTypes from 'prop-types'
import RadioButton from './RadioButton'

const RadioWrapper = ({ radioButtons, selected, onChange }) => {
  return (
    <>
      {radioButtons.map((rb, i) => {
        const inputId = rb.id ? rb.id : rb.value;
        return (
          <div key={i}>
            <RadioButton label={rb.label} inputId={inputId} selected={selected} onChange={onChange} value={rb.value} />
          </div>
        )
      })}
    </>
  )
}

RadioWrapper.propTypes = {
  radioButtons: PropTypes.array,
}

export default RadioWrapper
