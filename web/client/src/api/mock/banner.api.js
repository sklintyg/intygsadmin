import { delay } from "./util";

const test500 = false

export const removeBanner = (bannerId) => {
  return delay(500).then(() => {

    if(test500){
      const error =  {
        statusCode: 500,
        error: {
          errorCode: 'INTERNAL_SERVER_ERROR'
        }
      };
      throw error
    }

    return {
      result: 'OK'
    }
  });
};
