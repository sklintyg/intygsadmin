import React from 'react'
import {connect} from 'react-redux'
import {compose, lifecycle} from 'recompose'
import styled from 'styled-components'
import TableSortHead from '../styles/TableSortHead'
import {Button, Table, UncontrolledTooltip} from 'reactstrap'
import * as actions from '../../store/actions/users'
import * as modalActions from '../../store/actions/modal'
import IaAlert, {alertType} from "../alert/Alert";
import {getErrorMessage, getIsFetching, getUsersList} from "../../store/reducers/users";
import AppConstants from "../../AppConstants";
import {ClearIcon, Create} from "../styles/iaSvgIcons";
import {RemoveUserId} from "./dialogs/RemoveUser.dialog";
import {CreateUserId} from "./dialogs/CreateUser.dialog";
import {getUserHsaId} from "../../store/reducers/user";
import DisplayDate from "../displayDateTime/DisplayDate";

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const DisabledButton = styled(Button)`
  pointer-events: none;
`

const DisabledTaBort = ({user}) => (
  <div id={`endBtnDiv${user.id}`}>
    <DisabledButton className='end-btn' id={`endBtn${user.id}`} disabled={true} color="default">
      <ClearIcon /> Ta bort
    </DisabledButton>
    <UncontrolledTooltip placement="top" target={`endBtnDiv${user.id}`}>
      Det måste finnas minst en administratör med fullständig behörighet.
    </UncontrolledTooltip>
  </div>
)

const ActiveTaBort = ({user, openRemoveModal}) => (
  <>
    <Button className='end-btn' id={`endBtn${user.id}`} onClick={() => {
      openRemoveModal(user.id)
    }} color="default">
      <ClearIcon /> Ta bort
    </Button>
    <UncontrolledTooltip placement="top" target={`endBtn${user.id}`}>
      Öppnar ett dialogfönster där du bekräftar att du vill ta bort vald administratör.
    </UncontrolledTooltip>
  </>
)


const UsersList = ({ usersList, errorMessage, openModal, currentUserHsaId, ...otherProps}) => {
  if (errorMessage) {
    return (
      <ResultLine>
        <IaAlert type={alertType.ERROR}>{errorMessage}</IaAlert>
      </ResultLine>
    )
  }

  if (usersList.content && usersList.content.length === 0) {
    if (usersList.totalElements === 0) {
      return (
        <ResultLine />
      )
    }
  }

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = usersList
    if (sortColumn === newSortColumn) {
      sortDirection = usersList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({ ...otherProps, sortColumn, sortDirection })
  }

  const openRemoveModal = (userId) => {
    openModal(RemoveUserId, {
      userId
    })
  }

  const openChangeModal = (user) => {
    openModal(CreateUserId, {
      user
    })
  }

  return (
    <Wrapper>
      <ResultLine>
        Visar {usersList.start}-{usersList.end} av {usersList.totalElements} administratörer
      </ResultLine>
      <Table striped id='usersTable'>
        <thead>
        <tr>
          <TableSortHead
            currentSortColumn={usersList.sortColumn}
            currentSortDirection={usersList.sortDirection}
            text="Tillagd"
            tooltip="Visar vilket datum administratören lades till."
            sortId="createdAt"
            onSort={handleSort}
          />
          <TableSortHead
            currentSortColumn={usersList.sortColumn}
            currentSortDirection={usersList.sortDirection}
            text="Behörighetsnivå"
            tooltip="Visar vilken behörighet administratören har."
            sortId="intygsadminRole"
            onSort={handleSort}
          />
          <TableSortHead
            currentSortColumn={usersList.sortColumn}
            currentSortDirection={usersList.sortDirection}
            text="Namn"
            tooltip="Visar administratörens namn."
            sortId="intygId"
            onSort={handleSort}
          />
          <TableSortHead
            currentSortColumn={usersList.sortColumn}
            currentSortDirection={usersList.sortDirection}
            text="HSA-ID"
            tooltip="Visar administratörens HSA-ID."
            sortId="employeeHsaId"
            onSort={handleSort}
          />
          <th />
          <th />
        </tr>
        </thead>
        <tbody>
        { usersList.content && usersList.content.map((user) => (
          <tr key={user.id}>
            <td><DisplayDate date={user.createdAt} /></td>
            <td>{AppConstants.role[user.intygsadminRole]}</td>
            <td className="user-name">{user.name}</td>
            <td>{user.employeeHsaId}</td>
            <td>
              <Button
                className='change-btn'
                id={`changeBtn${user.id}`}
                disabled={currentUserHsaId === user.employeeHsaId}
                onClick={() => {
                  openChangeModal(user)
                }}
                color="primary">
                <Create /> Ändra
              </Button>
              {
                currentUserHsaId !== user.employeeHsaId ?
                <UncontrolledTooltip placement="top" target={`changeBtn${user.id}`}>
                  Öppnar ett dialogfönster där du kan ändra en administratörs behörighet.
                </UncontrolledTooltip> : ''
              }
            </td>
            <td>
              {
                currentUserHsaId === user.employeeHsaId ?
                <DisabledTaBort user={user} /> : <ActiveTaBort user={user} openRemoveModal={openRemoveModal} />
              }
            </td>
          </tr>
        ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

const fetchData = ({ fetchUsersList, sortColumn, sortDirection }) => {
  fetchUsersList({ sortColumn, sortDirection })
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
}

const mapStateToProps = (state) => {
  return {
    usersList: getUsersList(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
    currentUserHsaId: getUserHsaId(state)
  }
}

export default compose(
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  ),
  lifecycle(lifeCycleValues)
)(UsersList)
