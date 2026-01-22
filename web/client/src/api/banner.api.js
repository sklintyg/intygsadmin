import * as utils from './utils'

export let createBanner = (banner) => utils.makeServerPut('banner', banner)

export let removeBanner = (bannerId) => utils.makeServerDelete(`banner/${bannerId}`, {}, { emptyBody: true })

export let updateBanner = (banner, bannerId) => utils.makeServerPost(`banner/${bannerId}`, banner)

export let fetchFutureBanners = (application) => utils.makeServerRequest(`banner/activeAndFuture?application=${application}`)
