import * as utils from './utils'

export const fetchPrivatePractitioner = (hsaId) => utils.makeServerRequest(`privatepractitioner/${hsaId}`)

export const unregisterPrivatePractitioner = (hsaId) => utils.makeServerDelete(`privatepractitioner/${hsaId}`, {}, {emptyBody: true})

export const fetchPrivatePractitionerFile = () => utils.makeServerRequest(`privatepractitioner/file`, {nonJson: true})
