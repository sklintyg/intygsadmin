import React from 'react'
import { useAppSelector } from '../../store/hooks'
import Header from './Header'

const HeaderContainer = () => {
  const user = useAppSelector((state) => state.user)
  return <Header {...user} />
}

export default HeaderContainer
