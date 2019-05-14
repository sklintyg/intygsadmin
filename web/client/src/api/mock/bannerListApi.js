import bestallningarDb from "./bestallningarFakeDb";
import { delay } from "./util";

const test500 = false

export const fetchBestallningList = ({categoryFilter, textFilter, pageIndex, sortColumn, sortDirection}) => {
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

    const simpleTextFilter = t =>
      t.patient.id.indexOf(textFilter) >= 0 ||
      t.patient.name.toLowerCase().indexOf(textFilter.toLowerCase()) >= 0;
    const isAccepted = t =>
      t.status === "ACCEPTERAD" ||
      t.status === "OLAST" ||
      t.status === "LAST";
    const isRejected = t =>
      t.status === "AVVISAD" || t.status === "AVVISAD_RADERAD";

    switch (categoryFilter) {
      case 'AKTUELLA':
        const active = bestallningarDb.filter(t => isAccepted(t));
        const filtered =
          textFilter && textFilter !== ""
            ? active.filter(t => simpleTextFilter(t))
            : active;
        return {
          bestallningar: filtered,
          "pageIndex" : 0,
          "start" : 1,
          "end" : 10,
          "totalPages" : 2,
          "numberOfElements" : active.length,
          "totalElements" : 15,
          "sortColumn" : "ANKOMST_DATUM",
          "sortDirection" : "DESC"
        };
      case 'KLARA':
        const completed = bestallningarDb.filter(
          t => t.status === "KLARMARKERAD"
        );
        return {
          bestallningar: completed.filter(t => simpleTextFilter(t)),
          "pageIndex" : 0,
          "start" : 1,
          "end" : 10,
          "totalPages" : 2,
          "numberOfElements" : bestallningarDb.length,
          "totalElements" : 15,
          "sortColumn" : "ANKOMST_DATUM",
          "sortDirection" : "DESC"
        };
      case 'AVVISADE':
        const rejected = bestallningarDb.filter(t => isRejected(t));
        const filteredRejected = rejected.filter(t => simpleTextFilter(t));
        return {
          bestallningar: filteredRejected,
          "pageIndex" : 0,
          "start" : 1,
          "end" : 10,
          "totalPages" : 2,
          "numberOfElements" : bestallningarDb.length,
          "totalElements" : 15,
          "sortColumn" : "ANKOMST_DATUM",
          "sortDirection" : "DESC"
        };
      default:
        throw new Error(`Unknown filter: ${categoryFilter}`);
    }
  });
};
