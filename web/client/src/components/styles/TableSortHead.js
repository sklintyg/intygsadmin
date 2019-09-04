import React from 'react'
import { DownIcon, UpDownIcon, UpIcon } from './iaSvgIcons'
import {Button, UncontrolledTooltip} from 'reactstrap'

const TableSortHead = ({ id, currentSortColumn, currentSortDirection, text, sortId, onSort, tooltip }) => {
  const handleSort = (sortColumn) => {
    onSort(sortColumn)
  }

  const renderSortIcon = (sortColumn) => {
    if (currentSortColumn === sortColumn) {
      return currentSortDirection === 'DESC' ? <DownIcon /> : <UpIcon />
    } else {
      return <UpDownIcon />
    }
  }

  const elmId = id ? id : sortId;
  let textElm = text;

  if (tooltip) {
    textElm = (
      <>
        <span id={elmId}>{text}</span>
        <UncontrolledTooltip placement="top" target={elmId}>
          {tooltip}
        </UncontrolledTooltip>
      </>
    )
  }

  if (!sortId) {
    return (
      <>
        <th>{textElm}</th>
      </>
    )
  }

  return (
    <>
      <th>
        <Button
          color="link"
          onClick={() => {
            handleSort(sortId)
          }}>
          {textElm}
        </Button>{' '}
        {renderSortIcon(sortId)}
      </th>
    </>
  )
}

export default TableSortHead
