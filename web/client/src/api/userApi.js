import * as util from './utils'

export const fetchAnvandare = () => util.makeServerRequest('anvandare')

export const pollSession = () => util.makeServerRequest('/public-api/session-stat/ping', { pathComplete: true })

export const fakeLogin = (jsonUser) => util.makeServerPost('/fake-api/login', jsonUser)
