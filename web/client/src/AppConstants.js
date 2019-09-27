
const prio = {
  LAG: 'Låg',
  MEDEL: 'Medel',
  HOG: 'Hög',
}

const service = {
  INTYGSSTATISTIK: 'Intygsstatistik',
  WEBCERT: 'Webcert',
  REHABSTOD: 'Rehabstöd',
}

const AppConstants = {
  DEFAULT_PAGE: '/intygInfo',
  POLL_SESSION_INTERVAL_MS: 30000,
  TIMEOUT_REDIRECT_URL: '/#/loggedout/LOGIN_FEL003',
  service,
  prio
}
export default AppConstants
