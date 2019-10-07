import React from 'react'
import {CustomScrollingContainer, FlexColumnContainer, PageContainer} from '../components/styles/iaLayout'
import MenuBar from '../components/iaMenu/MenuBar';
import UsersPageHeader from "../components/users/UsersPageHeader";
import Users from "../components/users/Users";

const UsersPage = () => {
  return (
    <FlexColumnContainer>
      <MenuBar/>
      <CustomScrollingContainer>
        <UsersPageHeader />
        <PageContainer>
          <Users />
        </PageContainer>
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default UsersPage
