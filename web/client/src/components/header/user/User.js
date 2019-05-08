import React from 'react'
import styled from 'styled-components'
import { HeaderSectionContainer, SingleTextRowContainer, VerticalContainer } from '../styles'
import IaColors from '../../styles/IaColors'
import { UserIcon } from '../../styles/IaSvgIcons'

const UserComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  padding: 0 16px;
  color: ${IaColors.AG_COLOR_03};
`
const UserTitle = styled.div`
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-left: 4px;
`

const User = ({ userName }) => {
  return (
    <UserComponentWrapper>
      <UserIcon />
      <VerticalContainer>
        <SingleTextRowContainer>
          <UserTitle id="currentUserTitle">{userName}</UserTitle>
        </SingleTextRowContainer>
      </VerticalContainer>
    </UserComponentWrapper>
  )
}

export default User
