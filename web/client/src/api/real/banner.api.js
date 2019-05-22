import * as utils from './utils'

export const createBanner = (banner) => utils.makeServerPut('banner', banner)

export const removeBanner = (bannerId) => utils.makeServerDelete(`banner/${bannerId}`)
