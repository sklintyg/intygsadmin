import React from 'react'
import IbColors from '../styles/IbColors'

export const UpDownIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill="#000" width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M392 474h-392l194 188 198 188 194-188 195-188h-389z m-392-247l194-189 195-188 194 188 195 189h-386-392z" />
  </svg>
)

export const UpIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color ? color : '#000'} width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M0 227l194-189 195-188 194 188 195 189h-386-392z" />
  </svg>
)

export const DownIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color ? color : '#000'} width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M392 474h-392l194 188 198 188 194-188 195-188h-389z" />
  </svg>
)

export const ExpandIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_08} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,15.41L12,10.83L16.59,15.41L18,14L12,8L6,14L7.41,15.41Z" />
  </svg>
)

export const CollapseIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_08} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z" />
  </svg>
)

export const UnitIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36px" height="36px" viewBox="0 0 24 24">
    <path
      d="M5,3V21H11V17.5H13V21H19V3H5M7,5H9V7H7V5M11,5H13V7H11V5M15,5H17V7H15V5M7,9H9V11H7V9M11,9H13V11H11V9M15,9H17V11H15V9M7,13H9V15H7V13M11,13H13V15H11V13M15,13H17V15H15V13M7,17H9V19H7V17M15,17H17V19H15V17Z"
      shapeRendering="crispEdges"
    />
  </svg>
)
export const LogoutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z" />
  </svg>
)

export const ChangeUnitIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM6.5 9L10 5.5 13.5 9H11v4H9V9H6.5zm11 6L14 18.5 10.5 15H13v-4h2v4h2.5z" />
  </svg>
)

export const AboutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z" />
  </svg>
)

export const UserIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36px" height="36px" viewBox="0 0 24 24">
    <path d="M12,4A4,4 0 0,1 16,8A4,4 0 0,1 12,12A4,4 0 0,1 8,8A4,4 0 0,1 12,4M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z" />
  </svg>
)

export const InfoIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="30px" height="24px" viewBox="0 0 24 24">
    <path d="M11,9H13V7H11M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M11,17H13V11H11V17Z" />
  </svg>
)

//ib-ikon-17
export const ArrowBack = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_06} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" />
  </svg>
)

//ib-ikon-01
export const Check = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
  </svg>
)

//ib-ikon-04
export const ErrorOutline = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z" />
  </svg>
)

//ib-ikon-05
export const Security = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z" />
  </svg>
)

//ib-ikon-06
export const InfoOutline = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z" />
  </svg>
)

//ib-ikon-20
export const Warning = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z" />
  </svg>
)

//ib-ikon-48
export const Alarm = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_06} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M22 5.72l-4.6-3.86-1.29 1.53 4.6 3.86L22 5.72zM7.88 3.39L6.6 1.86 2 5.71l1.29 1.53 4.59-3.85zM12.5 8H11v6l4.75 2.85.75-1.23-4-2.37V8zM12 4c-4.97 0-9 4.03-9 9s4.02 9 9 9c4.97 0 9-4.03 9-9s-4.03-9-9-9zm0 16c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z" />
  </svg>
)

//ib-ikon-49
export const Error = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color ? color : IbColors.IB_COLOR_06} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" />
  </svg>
)

//ib-ikon-50
export const Archive = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_06} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M20.54 5.23l-1.39-1.68C18.88 3.21 18.47 3 18 3H6c-.47 0-.88.21-1.16.55L3.46 5.23C3.17 5.57 3 6.02 3 6.5V19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6.5c0-.48-.17-.93-.46-1.27zM12 17.5L6.5 12H10v-2h4v2h3.5L12 17.5zM5.12 5l.81-1h12l.94 1H5.12z" />
  </svg>
)

//ib-ikon-51
export const EventAvailableIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_06} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M16.53 11.06L15.47 10l-4.88 4.88-2.12-2.12-1.06 1.06L10.59 17l5.94-5.94zM19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11z" />
  </svg>
)

//ib-ikon-65
export const Block = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM4 12c0-4.42 3.58-8 8-8 1.85 0 3.55.63 4.9 1.69L5.69 16.9C4.63 15.55 4 13.85 4 12zm8 8c-1.85 0-3.55-.63-4.9-1.69L18.31 7.1C19.37 8.45 20 10.15 20 12c0 4.42-3.58 8-8 8z" />
  </svg>
)

//ib-ikon-71
export const Reply = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M10 9V5l-7 7 7 7v-4.1c5 0 8.5 1.6 11 5.1-1-5-4-10-11-11z" />
  </svg>
)

//ib-ikon-07
export const Print = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M19 8H5c-1.66 0-3 1.34-3 3v6h4v4h12v-4h4v-6c0-1.66-1.34-3-3-3zm-3 11H8v-5h8v5zm3-7c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1zm-1-9H6v4h12V3z" />
  </svg>
)

//fel-01.svg
export const ErrorPageIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 202 150" width="101px" height="75px">
    <defs />
    <g id="Layer_2" data-name="Layer 2">
      <g id="Layer_1-2" data-name="Layer 1">
        <path d="M24.67 9.53h163.56A14.48 14.48 0 0 1 202.72 24v126H10.19V24A14.48 14.48 0 0 1 24.67 9.53z" opacity=".5" fill="#d8d8d8" />
        <path d="M14.48 0H178a14.48 14.48 0 0 1 14.48 14.48v126H0v-126A14.48 14.48 0 0 1 14.48 0z" fill={IbColors.IB_COLOR_23} />
        <circle className="cls-3" cx="13.21" cy="11.77" r="2.25" />
        <circle className="cls-3" cx="27.74" cy="11.77" r="2.25" />
        <circle className="cls-3" cx="20.47" cy="11.77" r="2.25" />
        <path stroke="#f1f1f1" fill="none" strokeMiterlimit="10" d="M.64 20.84h190.91" />
        <path
          fill="#fff"
          d="M35.69 6.45h126.82v10.23H35.69zM96.26 93.67a3.51 3.51 0 0 1 3.63 3.47 3.63 3.63 0 0 1-7.25 0 3.54 3.54 0 0 1 3.62-3.47zm2-3h-4.14l-.77-25.74H99z"
        />
        <path
          d="M90.36 49.77l-29 50.16a6.82 6.82 0 0 0 5.91 10.23h57.92a6.82 6.82 0 0 0 5.91-10.23l-29-50.16a6.82 6.82 0 0 0-11.74 0z"
          stroke="#fff"
          strokeWidth="4"
          fill="none"
          strokeMiterlimit="10"
        />
      </g>
    </g>
  </svg>
)

//ib-ikon-33
export const Create = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z" />
  </svg>
)

//ib-ikon-26
export const NavigateNext = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
    <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" />
    <path d="M0 0h24v24H0z" fill="none" />
  </svg>
)

// ib-ikon-73
export const School = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M0 0h24v24H0z" fill="none" />
    <path d="M5 13.18v4L12 21l7-3.82v-4L12 17l-7-3.82zM12 3L1 9l11 6 9-4.91V17h2V9L12 3z" />
  </svg>
)

export const ExternalIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z" />
  </svg>
)
