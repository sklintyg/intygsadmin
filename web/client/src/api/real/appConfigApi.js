import * as util from './utils'

export const fetchAppConfig = () => util.makeServerRequest('public-api/appconfig', {pathComplete: true})
