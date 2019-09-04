import * as utils from './utils'

export const fetchIntygInfo = (intygsId) => utils.makeServerRequest(`intygInfo/${intygsId}`)
