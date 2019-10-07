import * as api from './real/users.api'

export const fetchUsersList = (requestParams) => {

  let {pageIndex, sortColumn, sortDirection} = requestParams

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'createdAt'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return api.fetchUsersList({...requestParams, pageIndex, sortColumn, sortDirection})
}

export const createUser = (user) => api.createUser(user);

export const removeUser = (userId) => api.removeUser(userId);

export const updateUser = (user, userId) => api.updateUser(user, userId);
