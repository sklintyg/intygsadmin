import * as utils from './utils'

export const fetchPrivatePractitioner = (hsaId) => utils.makeServerRequest(`privatepractitioner/${hsaId}`)

export const fetchPrivatePractitionerFile = () => utils.makeServerRequest(`privatepractitioner/file`, {nonJson: true})
