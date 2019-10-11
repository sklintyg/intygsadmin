import {Input} from "reactstrap";
import React from "react";

const searchInput = {
  width: '250px'
}

const HsaInput = ({id, value, inputRef, onChange, placeholder = 'SE1234567890-1X23'}) => {
  return (
    <Input
      id={id}
      placeholder={placeholder}
      value={value}
      onChange={(e) => onChange(e.target.value)}
      innerRef={inputRef}
      style={searchInput}
      maxLength="40"
    />
  )
}

export default HsaInput
