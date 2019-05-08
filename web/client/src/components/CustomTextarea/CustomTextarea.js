import React, { useRef, useState } from 'react'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import colors from '../styles/iaColors'

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

const CustomTextarea = ({ children }) => {
  const [currentHTML, setCurrentHTML] = useState()
  const [currentRange, setCurrentRange] = useState()
  const [currentLinkElement, setCurrentLinkElement] = useState()
  const [linkText, setLinkText] = useState('')
  const [linkHref, setLinkHref] = useState('')
  const textArea = useRef(null)

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
    setCurrentHTML(textArea.current.innerHTML)
  }

  const extractSelection = () => {
    var sel
    if (window.getSelection) {
      sel = window.getSelection()
    }
    return sel
  }

  const handleSelect = (evt) => {
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

  const handleBlur = (evt) => {
    setCurrentHTML(textArea.current.innerHTML)
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


  return (
    <>
      <input type="text" placeholder="Länktext" value={linkText} onChange={handleTextChange}/>
      <input type="text" placeholder="Länk" value={linkHref} onChange={handleHrefChange}/>
      <Button onClick={replaceSelectedText}>Infoga länk</Button>
      <CustomDiv
        ref={textArea}
        contentEditable="true"
        suppressContentEditableWarning="true"
        onSelect={handleSelect}
        onBlur={handleBlur}
        onPaste={handlePaste}
        onKeyPress={handleKeyPress}>
        {children}
      </CustomDiv>

      <div>{currentHTML}</div>
    </>
  )
}

export default CustomTextarea
