import React from 'react'
import IaAlert, { alertType } from '../alert/Alert'

const FetchError = ({ message }) => <IaAlert type={alertType.ERROR}>{message}</IaAlert>

export default FetchError
