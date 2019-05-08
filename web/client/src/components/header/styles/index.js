import styled from "styled-components";
import dividerImage from './divider-border.png'
import IaColors from "../../styles/iaColors2";

export const HeaderSectionContainer = styled.div`
  display: flex;
  align-items: center;
  height: 100%;
  border-width: 0 6px 0 0;
  border-style: solid;
  border-image-source: url(${dividerImage});
  border-image-slice: 0 50%;
  border-image-repeat: round;

`
export const HeaderSectionContainerHoverable = styled(HeaderSectionContainer)`
  &:hover {
      background-color: ${IaColors.IA_COLOR_05}
    }
`

export const VerticalContainer = styled.div`
  flex: 0 1 auto;
  display: flex;
  flex-direction: column;
  min-width: 1px;
`

export const SingleTextRowContainer = styled.div`
 display: flex;
 min-width: 1px;
`
export const ActionButton = styled.button`
  text-align: center;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 12px;
  color: ${IaColors.IA_COLOR_03};
  padding: 8px;
  height: 100%;
  min-width: 90px;
  white-space: nowrap;



`

