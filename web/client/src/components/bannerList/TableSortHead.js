import React from 'react'
import { DownIcon, UpDownIcon, UpIcon } from '../styles/iaSvgIcons'
import { Button } from 'reactstrap'

const TableSortHead = ({ currentSortColumn, currentSortDirection, text, sortId, onSort }) => {
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

  if (!sortId) {
    return (
      <>
        <th>{text}</th>
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
          {text}
        </Button>{' '}
        {renderSortIcon(sortId)}
      </th>
    </>
  )
}

export default TableSortHead
