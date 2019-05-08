import React, {Fragment} from 'react';
import Logo from "./logo/Logo";
import User from "./user/User";
import styled from "styled-components";
import IaColors from "../styles/iaColors2";
import {HeaderSectionContainerHoverable} from "./styles";
import ibValues from '../styles/ibValues2'
import Logout from "./logout/Logout";

const ComponentWrapper = styled.div`
  display: block;
  background-color: ${IaColors.IA_COLOR_02};
`

const StyledHeader = styled.div`
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  height: ${ibValues.headerHeight};
  background-color: ${IaColors.IA_COLOR_02};
  margin: auto;
  max-width: ${ibValues.maxContentWidth};
  color: ${IaColors.IA_COLOR_03};
`

const HeaderActionsWrapper = styled.div`
 display: flex;
  flex: 0 1 auto;
  justify-content: flex-end;

`

const Header = ({isAuthenticated, namn, userRole, logoutUrl}) => {
  return (
    <ComponentWrapper>
      <StyledHeader>
        <Logo className={(isAuthenticated ? 'd-none d-md-flex' : '')} />
        {isAuthenticated && <User id="currentUser" userName={namn} userRole={userRole} />}
        <HeaderActionsWrapper>
          {isAuthenticated &&
            <Fragment>
              <HeaderSectionContainerHoverable>
                <Logout logoutUrl={logoutUrl}/>
              </HeaderSectionContainerHoverable>
            </Fragment>
          }
        </HeaderActionsWrapper>
      </StyledHeader>
    </ComponentWrapper>
  )
};

export default Header;
