import React from 'react'
import {compose} from 'recompose'
import {connect} from 'react-redux'
import styled from 'styled-components'
import {getStatusCount} from '../../store/reducers/countStatus.reducer'

const PreviewDiv = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
`

const ResendStatusCount = ({ count }) => {
  if (!count  || count === 0) {
    return null;
  }

  return (
    <PreviewDiv>
      <strong>Antal händelser för omsändning</strong>
      <span>{count}</span>
    </PreviewDiv>
  )
}

const mapStateToProps = (state) => {
  return {
    count: getStatusCount(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
  ),
)(ResendStatusCount)
