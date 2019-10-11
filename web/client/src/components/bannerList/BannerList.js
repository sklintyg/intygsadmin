import React from 'react'
import {connect} from 'react-redux'
import {compose} from 'recompose'
import styled from 'styled-components'
import TableSortHead from '../styles/TableSortHead'
import {Button, Table, UncontrolledTooltip} from 'reactstrap'
import FetchError from './FetchError'
import {CreateBannerId, RemoveBannerId} from '../bannerDialogs'
import * as modalActions from '../../store/actions/modal'
import * as actions from '../../store/actions/banner'
import IaAlert, {alertType} from '../alert/Alert'
import StatusText from "./StatusText";
import AppConstants from "../../AppConstants";
import {ClearIcon, Create} from '../styles/iaSvgIcons'
import DisplayDateTime from '../displayDateTime/DisplayDateTime';
import {NoWrapTd} from "../styles/iaLayout";
import DisplayDate from "../displayDateTime/DisplayDate";

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const MessageColumn = styled.td`
  word-break: break-all;
`

const BannerList = ({ bannerList, onSort, errorMessage, openModal, removeBanner, fetchFutureBanners }) => {
  if (errorMessage) {
    return (
      <FetchError message={errorMessage} />
    )
  }

  if (bannerList.content && bannerList.content.length === 0) {
    if (bannerList.totalElements === 0) {
      return (
        <ResultLine>
          <IaAlert type={alertType.INFO}>Det finns inga driftbanners att visa ännu.</IaAlert>
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

    switch (bannerStatus) {
      case 'FUTURE':
        text =
          'Att avsluta en kommande driftbanner innebär att den inte kommer att visas i den aktuella applikationen. Den kommer också att tas bort från tabellen.'
        break
      case 'ACTIVE':
        text =
          'Att avsluta en pågående driftbanner innebär att den omedelbart tas bort från den aktuella applikationen.<br><br>Datum och tid för driftbannerns avslut sätts till nuvarande tidpunkt.'
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
      bannerStatus,
    })
  }

  const openChangeBanner = (banner) => {
    fetchFutureBanners(banner.application).finally(() => {
      openModal(CreateBannerId, {
        banner,
      })
    })
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
              tooltip="Visar vilket datum driftbannern skapades."
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Tjänst"
              sortId="application"
              tooltip="Visar vilken tjänst driftbannern visas i."
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsperiod"
              sortId="displayFrom"
              tooltip="Visar under vilken period driftbannern visas eller har visats."
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Prioritet"
              sortId="priority"
              tooltip="Visar vilken prioritet en driftbanner har."
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Meddelandetext"
              sortId="message"
              tooltip="Visar driftbannerns innehåll."
              onSort={handleSort}
            />
            <TableSortHead
              id="visningsstatus"
              currentSortColumn={bannerList.sortColumn}
              currentSortDirection={bannerList.sortDirection}
              text="Visningsstatus"
              tooltip="Visar driftbannerns aktuella status."
              onSort={handleSort}
            />
            <th />
            <th />
          </tr>
        </thead>
        <tbody id={'BannerListTable'}>
          { bannerList.content &&
            bannerList.content.map((banner) => (
              <tr key={banner.id}>
                <NoWrapTd><DisplayDate date={banner.createdAt} /></NoWrapTd>
                <td>{AppConstants.service[banner.application]}</td>
                <td>
                  <DisplayDateTime date={banner.displayFrom} />
                  <br />
                  <DisplayDateTime date={banner.displayTo} />
                </td>
                <td>{AppConstants.prio[banner.priority]}</td>
                <MessageColumn className='banner-message show-external-link' dangerouslySetInnerHTML={{ __html: banner.message }} />
                <td><StatusText status={banner.status} /></td>
                <td>
                  <Button
                    className='change-btn'
                    id={`changeBtn${banner.id}`}
                    disabled={banner.status === 'FINISHED'}
                    onClick={() => {
                      openChangeBanner(banner)
                    }}
                    color="primary">
                    <Create /> Ändra
                  </Button>
                  <UncontrolledTooltip trigger='hover' placement="top" target={`changeBtn${banner.id}`}>
                    Öppnar ett dialogfönster där du kan ändra driftbannerns innehåll.
                  </UncontrolledTooltip>
                </td>
                <td>
                  <Button
                    className='end-btn'
                    id={`endBtn${banner.id}`}
                    disabled={banner.status === 'FINISHED'}
                    onClick={() => {
                      openRemoveModal(banner.id, banner.status)
                    }}
                    color="default">
                    <ClearIcon /> Avsluta
                  </Button>
                  <UncontrolledTooltip trigger='hover' placement="top" target={`endBtn${banner.id}`}>
                    Öppnar ett dialogfönster där du kan avsluta driftbannern.
                  </UncontrolledTooltip>
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
