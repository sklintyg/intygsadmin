import * as utils from './utils'

export const fetchIntegratedUnit = (hsaId) => utils.makeServerRequest(`integratedUnits/${hsaId}`)

export const fetchIntegratedUnitsFile = () => utils.makeServerRequest(`integratedUnits/file`, {nonJson: true})
