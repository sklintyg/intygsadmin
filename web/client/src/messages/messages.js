import jsonMessage from './messages.json';

const flatten = require('flat');
const messages = flatten(jsonMessage);

export const getMessage = (key) => {
  if (haveMessage(key)) {
    return messages[key];
  }

  console.error(`Missing key ${key}`)

  return `Missing: ${key}`;
}

export const haveMessage = (key) => {
  return messages.hasOwnProperty(key);
}
