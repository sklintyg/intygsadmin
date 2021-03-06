import * as utils from './utils'

export const createBanner = (banner) => utils.makeServerPut('banner', banner)

export const removeBanner = (bannerId) => utils.makeServerDelete(`banner/${bannerId}`, {}, {emptyBody: true})

export const updateBanner = (banner, bannerId) => utils.makeServerPost(`banner/${bannerId}`, banner)

export const fetchFutureBanners = (application) => utils.makeServerRequest(`banner/activeAndFuture?application=${application}`)
