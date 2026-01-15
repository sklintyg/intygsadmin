import * as utils from './utils'

export const fetchUsersList = ({ pageIndex, sortColumn, sortDirection }) => {
  const finalPageIndex = pageIndex ?? 0
  const finalSortColumn = sortColumn ?? 'createdAt'
  const finalSortDirection = sortDirection ?? 'DESC'

  return utils.makeServerRequest(
    utils.buildUrlFromParams('user', {
      page: finalPageIndex,
      size: 10,
      sort: `${finalSortColumn},${finalSortDirection}`,
    })
  )
}

export const createUser = (user) => utils.makeServerPut('user', user)

export const removeUser = (userId) => utils.makeServerDelete(`user/${userId}`, {}, { emptyBody: true })

export const updateUser = (user, userId) => utils.makeServerPost(`user/${userId}`, user)
