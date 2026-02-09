import React, { useEffect, useRef, useState } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import colors from '../styles/iaColors'
import { InsertLinkIcon, RemoveLinkIcon } from '../styles/iaSvgIcons'
import { IaTypo06 } from '../styles/iaTypography'

const CustomDiv = styled.div`
  margin: 0 0 8px;
  padding: 8px;
  min-height: 100px;
  border: 1px solid ${colors.IA_COLOR_08};
  border-radius: 0 0 4px 4px;
  transition:
    border-color 0.36s ease-in-out,
    box-shadow 0.36s ease-in-out;

  &:focus {
    outline: none;
    border: 1px solid ${colors.IA_COLOR_05};
    box-shadow: 2px 2px 4px 0 rgba(0, 0, 0, 0.12);
  }
`

const Popup = styled.div`
  position: absolute;
  width: 240px;
  top: 45px;
  left: 30px;
  z-index: 1;
  border: 1px solid ${colors.IA_COLOR_08};
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 2px 2px 4px 0 rgba(0, 0, 0, 0.12);
  padding: 8px;
  display: flex;
  &.open {
    visibility: visible;
  }
  &.closed {
    visibility: hidden;
  }
  > div {
    flex: 0 0 180px;
    &.button_container {
      flex: 0 0 45px;
    }
    span {
      display: inline-block;
      width: 40px;
    }
    input:first-of-type {
      margin-bottom: 5px;
    }
  }
  button {
    flex: 0 0 42px;
    height: 35px;
    &:first-of-type {
      margin-bottom: 5px;
    }
  }
`

const Container = styled.div`
  position: relative;
  display: inline-block;
  width: 100%;
  &.error {
    > input,
    > input:focus,
    > button,
    > button:focus {
      border-color: ${colors.IA_COLOR_16};
    }
  }
`
const TextLimit = styled(IaTypo06)`
  text-align: right;
  color: ${colors.IA_COLOR_12};
`

const ActionBar = styled.div`
  width: 100%;
  height: 38px;
  border: 1px solid ${colors.IA_COLOR_08};
  border-bottom: 0;
  border-radius: 4px 4px 0 0;
  button {
    padding: 9px;
    &:hover {
      svg {
        fill: ${colors.IA_COLOR_05};
      }
    }
  }
`

const CustomTextarea = ({ onChange, className, value, limit, inputId }) => {
  const [currentRange, setCurrentRange] = useState()
  const [currentLinkElement, setCurrentLinkElement] = useState()
  const [linkText, setLinkText] = useState('')
  const [linkHref, setLinkHref] = useState('')
  const textArea = useRef()
  const popup = useRef()
  const [popupOpen, setPopupOpen] = useState(false)
  const isInitialized = useRef(false)

  useEffect(() => {
    if (textArea.current && !isInitialized.current && value) {
      textArea.current.innerHTML = value
      isInitialized.current = true
    }
  }, [])

  const onInput = () => {
    onChange(textArea.current.innerHTML)
  }

  const onBlur = () => {
    onChange(textArea.current.innerHTML)
  }

  const replaceSelectedText = () => {
    if (currentLinkElement) {
      currentLinkElement.setAttribute('href', linkHref)
      currentLinkElement.innerText = linkText
    } else if (currentRange) {
      currentRange.deleteContents()
      let link = document.createElement('a')
      link.setAttribute('href', !(linkHref.startsWith('http://') || linkHref.startsWith('https://')) ? 'http://' + linkHref : linkHref)
      link.setAttribute('target', '_blank')
      link.setAttribute('rel', 'noopener noreferrer')
      link.innerText = linkText
      currentRange.insertNode(link)
    }
    onChange(textArea.current.innerHTML)
    setPopupOpen(false)
  }

  const replaceCurrentLink = () => {
    if (currentLinkElement && currentLinkElement.parentNode) {
      let textNode = document.createTextNode(linkText)
      currentLinkElement.parentNode.replaceChild(textNode, currentLinkElement)
    }
    setPopupOpen(false)
  }

  const extractSelection = () => {
    var sel
    if (window.getSelection) {
      sel = window.getSelection()
    }
    return sel
  }

  const handleSelect = () => {
    const sel = extractSelection()
    if (sel && sel.rangeCount) {
      if (sel.focusNode.parentNode.nodeName === 'A') {
        setLinkText(sel.focusNode.parentNode.innerText)
        setLinkHref(sel.focusNode.parentNode.getAttribute('href'))
        setCurrentLinkElement(sel.focusNode.parentNode)
      } else {
        setLinkText(sel + '')
        setLinkHref('')
        setCurrentRange(sel.getRangeAt(0))
        setCurrentLinkElement(null)
      }
    }
  }

  const handlePaste = (evt) => {
    evt.preventDefault()
    let text = evt.clipboardData.getData('Text')
    const sel = extractSelection()
    let range = sel.getRangeAt(0)
    range.deleteContents()
    let textElement = document.createTextNode(text)
    range.insertNode(textElement)

    // Klipp bort allt efter limit om man klistrar in för mycket text.
    if (textArea.current.innerText.length > limit) {
      textArea.current.innerText = textArea.current.innerText.substring(0, limit)
    }
  }

  const handleKeyPress = (evt) => {
    if (textArea.current.innerText.length >= limit) {
      switch (evt.keyCode) {
        case 8:
        case 16:
        case 17:
        case 18:
        case 46:
        case 37:
        case 38:
        case 39:
        case 40:
          break
        default:
          evt.preventDefault()
      }
    }

    if (evt.key === 'Enter') {
      evt.preventDefault()
      const sel = extractSelection()
      let range = sel.getRangeAt(0)
      range.deleteContents()
      let br = document.createElement('br')
      range.insertNode(br)
      range.setStartAfter(br)
    }
  }

  const handleTextChange = (evt) => {
    setLinkText(evt.target.value)
  }

  const handleHrefChange = (evt) => {
    setLinkHref(evt.target.value)
  }

  const openLinkPopup = () => {
    if (!currentRange) {
      textArea.current.focus()
    }
    setPopupOpen(true)
  }

  useEffect(() => {
    const listener = (event) => {
      if (!popup.current || popup.current.contains(event.target)) {
        return
      }
      setPopupOpen(false)
    }
    document.addEventListener('mousedown', listener)

    return () => {
      document.removeEventListener('mousedown', listener)
    }
  }, [])

  return (
    <Container>
      <ActionBar>
        <Button color={'link'} onClick={openLinkPopup}>
          <InsertLinkIcon />
        </Button>
      </ActionBar>
      <CustomDiv
        className={className}
        ref={textArea}
        id={inputId}
        contentEditable="true"
        suppressContentEditableWarning="true"
        onSelect={handleSelect}
        onPaste={handlePaste}
        onKeyPress={handleKeyPress}
        onInput={onInput}
        onBlur={onBlur}
      />
      {textArea.current && limit ? <TextLimit>Tecken kvar: {limit - textArea.current.innerText.length}</TextLimit> : null}
      <Popup ref={popup} className={popupOpen ? 'open' : 'closed'}>
        <div>
          <span>Länk</span>
          <input type="text" placeholder="Länk" value={linkHref} onChange={handleHrefChange} />
          <span>Visa</span>
          <input type="text" placeholder="Länktext" value={linkText} onChange={handleTextChange} />
        </div>
        <div className="button_container">
          <Button color={'default'} onClick={replaceCurrentLink} title={'Ta bort aktuell länk'}>
            <RemoveLinkIcon />
          </Button>
          <Button color={'default'} onClick={replaceSelectedText} title={'Infoga länk'}>
            <InsertLinkIcon />
          </Button>
        </div>
      </Popup>
    </Container>
  )
}

export default CustomTextarea
