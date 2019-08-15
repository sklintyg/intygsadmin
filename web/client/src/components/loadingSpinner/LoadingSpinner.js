import {useEffect, useState} from 'react'
import styled from 'styled-components'
import {Spinner} from 'reactstrap'

const SpinnerBox = styled.div`
  width: 100%;
  text-align: center;
  margin-top: 120px;
`

const SpinnerWrapper = styled.div`
  background-color: rgba(255, 255, 255, 0.5);
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  display: flex;
`

const LoadingSpinner = ({loading, message}) => {
  const [showSpinner, setShowSpinner] = useState(false)

  useEffect(() => {
    let timeout
    if (loading && !showSpinner) {
      timeout = setTimeout(() => setShowSpinner(true), 300)
    }
    return () => clearTimeout(timeout)
  })

  if (loading && showSpinner) {
    return (
      < SpinnerWrapper >
      < SpinnerBox >
      < Spinner
    color = "secondary" / >
      < div > {message} < /div>
      < /SpinnerBox>
      < /SpinnerWrapper>
  )
  } else {
    return null
  }
}

export default LoadingSpinner
