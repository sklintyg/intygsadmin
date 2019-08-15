import {useState} from 'react'
import styled from 'styled-components'
import {Button, Spinner} from 'reactstrap'
import PropTypes from 'prop-types'

const StyledButton = styled(Button)`
  position: relative;
`

const StyledSpan = styled.span`
  visibility: ${(props) => props.hide};
`

const StyledSpinner = styled(Spinner)`
  position: absolute;
  left: calc(50% - 0.5rem);
  top: calc(50% - 0.5rem);
  visibility: ${(props) => props.show};
`

const SpinnerButton = ({accept, color, children, disabled}) => {
  const [showSpinner, setShowSpinner] = useState(false)

  const onClickHandler = () => {
    setShowSpinner(true)
    accept().then(() => {
      setShowSpinner(false)
    })
  }

  return (
    < StyledButton
  color = {color}
  disabled = {disabled || showSpinner
}
  onClick = {()
=>
  {
    onClickHandler()
  }
}>
<
  StyledSpinner
  size = "sm"
  show = {showSpinner ? 'visible' : 'hidden'}
  />
  < StyledSpan
  hide = {showSpinner ? 'hidden' : 'visible'} > {children} < /StyledSpan>
    < /StyledButton>
)
}

SpinnerButton.propTypes = {
  accept: PropTypes.func.isRequired,
  color: PropTypes.string.isRequired,
  children: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  disabled: PropTypes.bool,
}

export default SpinnerButton
