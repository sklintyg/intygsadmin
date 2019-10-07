import React from 'react'
import IaColors from '../styles/iaColors'
import {UserIcon} from '../styles/iaSvgIcons'
import PageHeader from "../styles/PageHeader";
import UsersActionBar from "./UsersActionBar";

const UsersPageHeader = () => {
  return (
    <PageHeader
      header="Administratörer"
      subHeader="	Lägg till och hantera administratörer och deras behörigheter i Intygsadmin."
      icon={<UserIcon color={IaColors.IA_COLOR_02} />}
      actionBar={<UsersActionBar />}
    />
  )
}

export default UsersPageHeader
