import {delay} from './util'

export const fetchAppConfig = () =>
  delay(500).then(() => {
    /*const error =  {
      statusCode: 500,
      error: {
        errorCode: 'UNKNOWN_INTERNAL_PROBLEM'
      }
    };
    throw error*/
    return {
      versionInfo: {applicationName: 'ia', buildVersion: '0', buildTimestamp: 'now', activeProfiles: 'some,profiles'},
      loginUrl: 'LoginUrlFromServerConfig',
    }
  })

