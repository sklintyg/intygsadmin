const ROOT_URL = '/api/'

const networkError = (err) => {
  return Promise.reject({
    statusCode: -1,
    error: {
      errorCode: 'NETWORK_ERROR',
      message: err,
      logId: null,
    },
  })
}

export const handleResponse = (config) => (response) => {
  if (!response.ok) {
    return response
      .json()
      .catch(() => {
        if (response.status === 404) {
          const error = {
            statusCode: response.status,
            error: {
              errorCode: 'NOT_FOUND',
              message: 'Resource not found',
              logId: null,
            },
          }

          throw error
        }

        // We should never get these unless response is mangled
        // Or API is not properly implemented
        const error = {
          statusCode: response.status,
          error: {
            errorCode: 'UNKNOWN_INTERNAL_PROBLEM',
            message: 'Invalid or missing JSON',
            logId: null,
          },
        }

        throw error
      })
      .then((errorJson) => {
        const error = { statusCode: response.status, error: errorJson }
        throw error
      })
  }

  if (response.status === 204) {
    return {};
  }

  if (config) {
    if (config.emptyBody) {
      return {}
    }
    if (config.nonJson) {
      return response
    }
  }

  return response.json()
}

export const buildUrlFromParams = (path, parameters) => {
  let parameterList = []
  if (parameters) {
    Object.keys(parameters).forEach((key) => {
      let value = parameters[key]

      if (value) {
        parameterList.push(`${key}=${value}`)
      }
    })
  }

  let urlParameters = parameterList.join('&')

  if (urlParameters) {
    urlParameters = '?' + urlParameters
  }

  return path + urlParameters
}

const internalRequest = (path, fetchConfig, config = {}) => {
  let url = config.pathComplete ? `${path}` : `${ROOT_URL}${path}`

  return fetch(url, fetchConfig)
    .catch(networkError)
    .then(handleResponse(config))
}

const getJsonConfig = (method, body) => ({
  method,
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: "same-origin",
  body: JSON.stringify(body),
})

export const makeServerRequest = (path, config) => {
  return internalRequest(path, {credentials: "same-origin"}, config)
}

export const makeServerPost = (path, body, config = {}) => {
  const fetchConfig = getJsonConfig('POST', body)

  return internalRequest(path, fetchConfig, config)
}

export const makeServerPut = (path, body, config = {}) => {
  const fetchConfig = getJsonConfig('PUT', body)

  return internalRequest(path, fetchConfig, config)
}

export const makeServerDelete = (path, body, config = {}) => {
  const fetchConfig = getJsonConfig('DELETE', body)

  return internalRequest(path, fetchConfig, config)
}
