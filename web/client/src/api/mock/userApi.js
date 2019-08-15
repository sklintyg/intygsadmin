import userDb from './userDb'
import {delay} from './util'

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  anvandare: userDb,
}

// should actually return  return makeServerRequest('anvandare')
export const fetchAnvandare = () =>
  delay(500).then(() => {
    return fakeDatabase.anvandare
  })

export const pollSession = () =>
  delay(500).then(() => {
    return {
      hasSession: true,
      secondsUntilExpire: 99,
      authenticated: true,
    }
  })
