import React, { useRef, useState } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import colors from '../styles/iaColors'
import { InsertLinkIcon, RemoveLinkIcon } from '../styles/iaSvgIcons'
import useOnClickOutside from '../hooks/UseOnClickOutside'

const CustomDiv = styled.div`
  margin: 8px 0;
  padding: 8px;
  min-height: 100px;
  border: 1px solid ${colors.IA_COLOR_08};
  border-radius: 4px;
  transition: border-color 0.36s ease-in-out, box-shadow 0.36s ease-in-out;

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
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
  padding: 8px;
  &.open {
    display: flex;
  }
  &.closed {
    display: none;
  }
  > div {
    flex: 0 0 180px;
    span {
      display: inline-block;
      width: 40px;
    }
  }
  button {
    flex: 0 0 42px;
    height: 35px;
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

const CustomTextarea = ({ onChange }) => {
  const [currentRange, setCurrentRange] = useState()
  const [currentLinkElement, setCurrentLinkElement] = useState()
  const [linkText, setLinkText] = useState('')
  const [linkHref, setLinkHref] = useState('')
  const textArea = useRef(null)
  const popup = useRef()
  const [popupOpen, setPopupOpen] = useState(false)

  const replaceSelectedText = () => {
    if (currentLinkElement) {
      currentLinkElement.setAttribute('href', linkHref)
      currentLinkElement.innerText = linkText
    } else if (currentRange) {
      currentRange.deleteContents()
      let link = document.createElement('a')
      link.setAttribute('href', linkHref)
      link.innerText = linkText
      currentRange.insertNode(link)
    }
    onChange(textArea.current.innerHTML)
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
  }

  const handleKeyPress = (evt) => {
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

  const openLinkPopup = (event) => {
    setPopupOpen(true)
  }

  useOnClickOutside(popup, () => {
    if (popupOpen) {
      replaceSelectedText()
    }
    setPopupOpen(false)
  })

  return (
    <Container>
      <Button color={'default'} onClick={openLinkPopup}>
        <InsertLinkIcon />
      </Button>
      <CustomDiv
        ref={textArea}
        contentEditable="true"
        suppressContentEditableWarning="true"
        onSelect={handleSelect}
        onPaste={handlePaste}
        onKeyPress={handleKeyPress}>
      </CustomDiv>
      <Popup ref={popup} className={popupOpen ? 'open' : 'closed'}>
        <div>
          <span>Länk</span>
          <input type="text" placeholder="Länk" value={linkHref} onChange={handleHrefChange} />
          <span>Visa</span>
          <input type="text" placeholder="Länktext" value={linkText} onChange={handleTextChange} />
        </div>
        <Button color={'default'} onClick={replaceSelectedText}>
          <RemoveLinkIcon />
        </Button>
      </Popup>
    </Container>
  )
}

export default CustomTextarea
