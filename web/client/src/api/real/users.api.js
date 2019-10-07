import * as utils from './utils'

export const fetchUsersList = ({pageIndex, sortColumn, sortDirection}) =>
  utils.makeServerRequest(utils.buildUrlFromParams('user', {
    page: pageIndex,
    size: 10,
    sort: `${sortColumn},${sortDirection}`
  }));

export const createUser = (user) => utils.makeServerPut('user', user)

export const removeUser = (userId) => utils.makeServerDelete(`user/${userId}`, {}, {emptyBody: true})

export const updateUser = (user, userId) => utils.makeServerPost(`user/${userId}`, user)
