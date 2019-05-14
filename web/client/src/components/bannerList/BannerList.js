import React from 'react'
import { Link } from 'react-router-dom'
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
              sortId="ID"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Tjänst"
              sortId="INTYG_TYP"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsperiod"
              sortId="INVANARE_PERSON_ID"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Prioritet"
              sortId="STATUS"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Meddelandetext"
              sortId="ANKOMST_DATUM"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsstatus"
              sortId="ANKOMST_DATUM"
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
           bannerList.bannerList &&
           bannerList.bannerList.map((banner) => (
              <tr key={banner.id}>
                <td>{banner.id}</td>
                <td>{banner.intygTyp}</td>
                <td>{banner.invanare.personId}</td>
                <td>
                  {banner.status === 'Oläst' ? 'Fel' : null} {banner.status}
                </td>
                <td>{banner.ankomstDatum}</td>
                <td>{banner.ankomstDatum}</td>
                <td>{banner.ankomstDatum}</td>
                <td>
                  <Link
                    to={{
                      pathname: '/banner/' +banner.id,
                      search: '',
                      hash: '',
                    }}
                    id={'BannerListButton-' +banner.id}>
                    <Button color="primary">Visa</Button>
                  </Link>
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

export default BannerList
