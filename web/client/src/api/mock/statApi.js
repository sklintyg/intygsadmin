import {delay} from "./util";

const test500 = false

export const fetchStat = () => {
  return delay(1000).then(() => {

    if (test500) {
      const error = {
        statusCode: 500,
        error: {
          errorCode: 'INTERNAL_SERVER_ERROR'
        }
      };
      throw error
    }

    return {
      unread: 12,
      active: 14,
      completed: 16
    };
  })
};
