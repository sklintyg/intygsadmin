import * as utils from './utils'

export const fetchBannerList = ({ pageIndex, sortColumn, sortDirection }) => {
  const finalPageIndex = pageIndex ?? 0
  const finalSortColumn = sortColumn ?? 'createdAt'
  const finalSortDirection = sortDirection ?? 'DESC'

  return utils.makeServerRequest(
    utils.buildUrlFromParams('banner', {
      page: finalPageIndex,
      size: 10,
      sort: `${finalSortColumn},${finalSortDirection}`,
    })
  )
}
