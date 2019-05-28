import React from 'react'
import IaColors from './iaColors'

// ia-ikon-01
export const UserIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IaColors.IA_COLOR_03} width="36px" height="36px" viewBox="0 0 24 24">
    <path d="M12,4A4,4 0 0,1 16,8A4,4 0 0,1 12,12A4,4 0 0,1 8,8A4,4 0 0,1 12,4M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z" />
  </svg>
)

// ia-ikon-02 info (not info alert)
export const InfoIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="35px" height="35px" viewBox="0 0 24 24">
    <path d="M11,9H13V7H11M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M11,17H13V11H11V17Z" />
  </svg>
)

// ia-ikon-03 add
export const AddIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/><path d="M0 0h24v24H0z" fill="none"/>
  </svg>
)

// ia-ikon-04
export const DownIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color ? color : '#000'} width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M392 474h-392l194 188 198 188 194-188 195-188h-389z" />
  </svg>
)

// ia-ikon-05
export const UpIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color ? color : '#000'} width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M0 227l194-189 195-188 194 188 195 189h-386-392z" />
  </svg>
)

export const UpDownIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill="#000" width="12px" height="12px" viewBox="0 -150 1000 1000">
    <path d="M392 474h-392l194 188 198 188 194-188 195-188h-389z m-392-247l194-189 195-188 194 188 195 189h-386-392z" />
  </svg>
)

// ia-ikon-06
export const ArrowBack = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IaColors.IA_COLOR_19} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" />
  </svg>
)

// ia-ikon-07
export const NavigateNext = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
    <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z" />
    <path d="M0 0h24v24H0z" fill="none" />
  </svg>
)

// ia-ikon-08
export const ExpandIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IaColors.IA_COLOR_99} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,15.41L12,10.83L16.59,15.41L18,14L12,8L6,14L7.41,15.41Z" />
  </svg>
)

// ia-ikon-09
export const CollapseIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IaColors.IA_COLOR_99} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z" />
  </svg>
)

// ia-ikon-10
export const TimeIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24">
    <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/><path d="M0 0h24v24H0z" fill="none"/><path d="M12.5 7H11v6l5.25 3.15.75-1.23-4.5-2.67z"/>
  </svg>
)

// ia-ikon-11
export const InsertLinkIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24">
    <path d="M0 0h24v24H0z" fill="none"/><path d="M3.9 12c0-1.71 1.39-3.1 3.1-3.1h4V7H7c-2.76 0-5 2.24-5 5s2.24 5 5 5h4v-1.9H7c-1.71 0-3.1-1.39-3.1-3.1zM8 13h8v-2H8v2zm9-6h-4v1.9h4c1.71 0 3.1 1.39 3.1 3.1s-1.39 3.1-3.1 3.1h-4V17h4c2.76 0 5-2.24 5-5s-2.24-5-5-5z"/>
  </svg>
)

// ia-ikon-12
export const RemoveLinkIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24">
    <path fill="none" d="M0 0h24v24H0V0z"/><path d="M17 7h-4v1.9h4c1.71 0 3.1 1.39 3.1 3.1 0 1.43-.98 2.63-2.31 2.98l1.46 1.46C20.88 15.61 22 13.95 22 12c0-2.76-2.24-5-5-5zm-1 4h-2.19l2 2H16zM2 4.27l3.11 3.11C3.29 8.12 2 9.91 2 12c0 2.76 2.24 5 5 5h4v-1.9H7c-1.71 0-3.1-1.39-3.1-3.1 0-1.59 1.21-2.9 2.76-3.07L8.73 11H8v2h2.73L13 15.27V17h1.73l4.01 4L20 19.74 3.27 3 2 4.27z"/><path fill="none" d="M0 24V0"/>
  </svg>
)

// ia-ikon-13
export const InfoOutline = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z" />
  </svg>
)

// ia-ikon-14
export const ErrorOutline = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z" />
  </svg>
)

// ia-ikon-15
export const Warning = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z" />
  </svg>
)

// ia-ikon-16 external link
export const ExternalIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z" />
  </svg>
)

// ia-ikon-17 refresh
export const LoadIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
    <path d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
    <path d="M0 0h24v24H0z" fill="none"/>
  </svg>
)

// ia-ikon-18 logo build
export const LogoIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24">
    <path clipRule="evenodd" fill="none" d="M0 0h24v24H0z"/>
    <path d="M22.7 19l-9.1-9.1c.9-2.3.4-5-1.5-6.9-2-2-5-2.4-7.4-1.3L9 6 6 9 1.6 4.7C.4 7.1.9 10.1 2.9 12.1c1.9 1.9 4.6 2.4 6.9 1.5l9.1 9.1c.4.4 1 .4 1.4 0l2.3-2.3c.5-.4.5-1.1.1-1.4z"/>
  </svg>
)

// ia-ikon-19 clear
export const ClearIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
    <path d="M0 0h24v24H0z" fill="none"/>
  </svg>
)

// ia-ikon-20 create
export const Create = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="16px" height="16px" viewBox="0 0 24 24">
    <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z" />
  </svg>
)

// ia-ikon-21 description
export const DocIcon = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M0 0h24v24H0z" fill="none"/>
    <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
  </svg>
)

export const LogoutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IaColors.IA_COLOR_03} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z" />
  </svg>
)

// alert confirm
export const Check = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
  </svg>
)

// alert sekretess
export const Security = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="24" height="24" viewBox="0 0 24 24">
    <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z" />
  </svg>
)

//fel-01.svg
export const ErrorPageIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 202 150" width="101px" height="75px">
    <defs />
    <g id="Layer_2" data-name="Layer 2">
      <g id="Layer_1-2" data-name="Layer 1">
        <path d="M24.67 9.53h163.56A14.48 14.48 0 0 1 202.72 24v126H10.19V24A14.48 14.48 0 0 1 24.67 9.53z" opacity=".5" fill="#d8d8d8" />
        <path d="M14.48 0H178a14.48 14.48 0 0 1 14.48 14.48v126H0v-126A14.48 14.48 0 0 1 14.48 0z" fill={IaColors.IA_COLOR_11} />
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
export const Calendar = ({ color }) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="16" height="16" viewBox="0 0 24 24">
    <path d="M20 3h-1V1h-2v2H7V1H5v2H4c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 18H4V8h16v13z"/>
  </svg>
)

