import * as util from './utils'

export const fetchAnvandare = () => util.makeServerRequest('anvandare')

export const pollSession = () => util.makeServerRequest('public-api/session-stat/ping', { pathComplete: true })
