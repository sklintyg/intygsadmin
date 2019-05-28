import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import styled from 'styled-components'
import TableSortHead from './TableSortHead'
//import { Error } from '../styles/iaSvgIcons'
import { Table, Button } from 'reactstrap'
import FetchError from './FetchError'
import { RemoveBannerId, CreateBannerId} from '../bannerDialogs'
import * as modalActions from '../../store/actions/modal'
import * as actions from '../../store/actions/banner'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const BannerList = ({bannerList, onSort, errorMessage, openModal, removeBanner, onActionComplete }) => {
  if (bannerList.content && bannerList.content.length === 0) {
    if (bannerList.totalElements === 0) {
      return (
        <ResultLine>
          Det finns inga driftbanners att visa ännu.
        </ResultLine>
      )
    } else {
      return <ResultLine>Inga träffar för den valda filtreringen. Överväg att ändra filtreringen för att utöka resultatet.</ResultLine>
    }
  }

  const handleSort = (sortColumn) => {
    onSort(sortColumn)
  }

  const openRemoveModal = (bannerId, bannerStatus) => {
    let text = ''

    switch(bannerStatus){
      case 'FUTURE':
      text = 'Att avsluta en kommande driftbanner innebär att den inte kommer att visas i Webcert. Den kommer också att tas bort från tabellen.'
      break
      case 'ACTIVE':
      text = 'Att avsluta en pågående driftbanner innebär att den omedelbart tas bort från Webcert.<br><br>Datum och tid för driftbannerns avslut sätts till nuvarande tidpunkt.'
      break
      case 'FINISHED':
      break
      default:
      break
    }

    openModal(RemoveBannerId, {
      text,
      removeBanner,
      bannerId,
      bannerStatus
    })
  }

  const openChangeBanner = (banner) => {
    openModal(CreateBannerId, {
      banner
    })
  }

  const prioText = {
    'LOW': 'Låg',
    'MEDIUM': 'Medel',
    'HIGH': 'Hög',
  }

  const serviceText = {
    'STATISTIK': 'Intygsstatistik',
    'WEBCERT': 'Webcert',
    'REHABSTOD': 'Rehabstöd',
  }

  const statusText = {
    'FUTURE': 'Kommande',
    'ACTIVE': 'Pågående',
    'FINISHED': 'Avslutad',
  }

  const dateShowPeriodOptions = {year: 'numeric', month: '2-digit', day:'2-digit', hour: '2-digit', minute: '2-digit'};

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
                <td>{new Date(banner.createdAt).toLocaleDateString('sv-SE')}</td>
                <td>{serviceText[banner.application]}</td>
                <td>
                  {new Date(banner.displayFrom).toLocaleString('sv-SE', dateShowPeriodOptions)}<br/>{new Date(banner.displayTo).toLocaleString('sv-SE', dateShowPeriodOptions)}
                </td>
                <td>{prioText[banner.priority]}</td>
                <td dangerouslySetInnerHTML={{__html: banner.message}}></td>
                <td>{statusText[banner.status]}</td>
                <td>
                    <Button disabled={banner.status === 'FINISHED'} onClick={() => {openChangeBanner(banner)}} color="primary">Ändra</Button>
                </td>
                <td>
                    <Button disabled={banner.status === 'FINISHED'} onClick={() => {openRemoveModal(banner.id, banner.status)}} color="primary">Avsluta</Button>
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

export default compose(
  connect(
    null,
    { ...actions, ...modalActions }
  )
)(BannerList)
