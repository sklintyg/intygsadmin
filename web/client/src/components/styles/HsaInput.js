import {Input} from "reactstrap";
import React from "react";

const searchInput = {
  width: '250px'
}

const HsaInput = ({id, value, inputRef, onChange}) => {
  return (
    <Input
      id={id}
      placeholder='SE1234567890-1X23'
      value={value}
      onChange={(e) => onChange(e.target.value)}
      innerRef={inputRef}
      style={searchInput}
      maxLength="40"
    />
  )
}

export default HsaInput
