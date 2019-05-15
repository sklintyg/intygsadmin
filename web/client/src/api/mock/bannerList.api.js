import bannerListDb from "./bannerList.fakedb";
import { delay } from "./util";

const test500 = false

export const fetchBannerList = ({pageIndex, sortColumn, sortDirection}) => {
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
      listData: bannerListDb,
      pageIndex,
      sortColumn,
      sortDirection,
      start: 1,
      end: 1,
      limit: 10,
      totalPages: bannerListDb.length / 10,
      numberOfElements: bannerListDb.length,
      totalElements: bannerListDb.length,
    }
  });
};
