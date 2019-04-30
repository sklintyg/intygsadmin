import * as util from './utils'

export const fetchAnvandare = () => util.makeServerRequest('anvandare')

export const changeEnhet = (hsaId) => util.makeServerPost('anvandare/unit-context/' + hsaId)

export const pollSession = () => util.makeServerRequest('public-api/session-stat/ping', { pathComplete: true })
