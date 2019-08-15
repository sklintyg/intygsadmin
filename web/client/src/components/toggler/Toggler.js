import styled from 'styled-components'
import * as PropTypes from "prop-types";
import {CollapseIcon, ExpandIcon} from "../styles/iaSvgIcons";

const TogglerTag = styled.button`
  padding: 4px;
  border: none;
  background: transparent;
`

const Toggler = ({expanded, handleToggle}) => (
  < TogglerTag
onClick = {handleToggle} >
  {expanded ? < ExpandIcon / >
: <
CollapseIcon / >
}
<
/TogglerTag>
)

Toggler.propTypes = {
  expanded: PropTypes.bool,
  handleToggle: PropTypes.func
}

export default Toggler;
