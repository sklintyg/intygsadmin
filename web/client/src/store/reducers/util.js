import {getMessage, haveMessage} from "../../messages/messages";

export const buildClientError = (errorResponse, prefix) => {

  const {error} = errorResponse;
  let titleKey, messageKey, logId;

  if (!error || !error.hasOwnProperty('errorCode')) {
    messageKey = 'error.common.unknown.message';
    titleKey = 'error.common.unknown.title';
    logId = null;
  } else {
    const errorKey = error.errorCode.toLowerCase();
    logId = error.logId || null;

    titleKey = `${prefix}.${errorKey}.title`
    messageKey = `${prefix}.${errorKey}.message`

    if (!haveMessage(messageKey)) {
      titleKey = `${prefix}.unknown.title`
      messageKey = `${prefix}.unknown.message`
      if (!haveMessage(messageKey)) {
        titleKey = `error.common.${errorKey}.title`;
        messageKey = `error.common.${errorKey}.message`;
        if (!haveMessage(messageKey)) {
          titleKey = `error.common.unknown.title`;
          messageKey = `error.common.unknown.message`;
        }
      }
    }
  }

  return {
    title: getMessage(titleKey),
    message: getMessage(messageKey),
    logId: logId
  }
}
