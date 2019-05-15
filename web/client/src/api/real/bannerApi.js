import * as utils from './utils'

export const createBanner = (banner) => utils.makeServerPut('banner', banner)
