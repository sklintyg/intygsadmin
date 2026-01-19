import jsonMessage from './messages.json'
import { flatten } from 'flat'

const messages = flatten(jsonMessage)

export const getMessage = (key, data) => {
  if (haveMessage(key)) {
    let message = messages[key]

    if (data) {
      Object.keys(data).forEach((key) => {
        message = message.replace(new RegExp(`{${key}}`, 'g'), data[key])
      })
    }

    message = message.replace(new RegExp('{[a-z0-9]*}', 'gi'), '-')

    return message
  }

  console.error(`Missing key ${key}`)

  return `Missing: ${key}`
}

export const haveMessage = (key) => {
  return messages.hasOwnProperty(key)
}
