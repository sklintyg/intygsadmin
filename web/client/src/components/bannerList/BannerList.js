import React from 'react'
import styled from 'styled-components'
import TableSortHead from './TableSortHead'
//import { Error } from '../styles/iaSvgIcons'
import { Table, Button } from 'reactstrap'
import FetchError from './FetchError'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const BannerList = ({bannerList, onSort, errorMessage }) => {
  if (bannerList.banners && bannerList.banners.length === 0) {
    if (bannerList.ingaRegistreradeBanners) {
      return (
        <ResultLine>
          Det finns inga driftbanners
        </ResultLine>
      )
    } else {
      return <ResultLine>Inga träffar för den valda filtreringen. Överväg att ändra filtreringen för att utöka resultatet.</ResultLine>
    }
  }

  const handleSort = (sortColumn) => {
    onSort(sortColumn)
  }

  return (
    <Wrapper>
      <ResultLine>
        Visar {bannerList.start}-{bannerList.end} av {bannerList.totalElements} träffar
      </ResultLine>
      <Table striped>
        <thead>
          <tr>
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Skapat"
              sortId="createdAt"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Tjänst"
              sortId="application"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsperiod"
              sortId="displayFrom"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Prioritet"
              sortId="priority"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Meddelandetext"
              sortId="message"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsstatus"
              sortId="status"
              onSort={handleSort}
            />
            <th />
            <th />
          </tr>
        </thead>
        <tbody id={'BannerListTable'}>
          {errorMessage && (
            <tr>
              <td colSpan={8}>
                <FetchError message={errorMessage} />
              </td>
            </tr>
          )}
          {!errorMessage &&
           bannerList.content &&
           bannerList.content.map((banner) => (
              <tr key={banner.id}>
                <td>{banner.createdAt}</td>
                <td>{banner.application}</td>
                <td>
                  {banner.displayFrom}<br/>{banner.displayTo}
                </td>
                <td>{banner.priority}</td>
                <td>{banner.message}</td>
                <td>{banner.status}</td>
                <td>
                    <Button color="primary">Ändra</Button>
                </td>
                <td>
                    <Button color="primary">Avsluta</Button>
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

export default BannerList
