import styled from 'styled-components'
import colors from './IaColors';

/*
100 = Roboto Thin
300 = Roboto Light
400 = Roboto Regular
500 = Roboto Medium
700 = Roboto Bold

You can either use these directly or use them as a base for refinements.
To just change tag use the "as" attribute i.e  <IbTypo01 as="h1">
To customize more use construct like:

const SpecialRubrik = Styled(Typo.IbTypo02)`
  border-bottom: 1px solid ${Colors.AG_COLOR_08};
  border-radius: 4px 4px 0 0;
  color: ${Colors.AG_COLOR_06};
`;

in bootstrap-overrides, the base font of the body is IbTypo07 with color IbColor08 which will be inherited
if not specified as something else.
 */

const Div = styled.div`
  color: ${props => props.color || colors.AG_COLOR_99};
`

export const IbTypo01 = styled(Div)`
  font-weight: 300;
  font-size: 25px;
`

export const IbTypo02 = styled(Div)`
  font-weight: 500;
  font-size: 18px;
`

export const IbTypo03 = styled(Div)`
  font-weight: 300;
  font-size: 18px;
`

export const IbTypo04 = styled(Div)`
  font-weight: 700;
  font-size: 18px;
`

export const IbTypo05 = styled(Div)`
  font-weight: 500;
  font-size: 16px;
`

export const IbTypo06 = styled(Div)`
  font-weight: 500;
  font-size: 14px;
`

export const IbTypo07 = styled(Div)`
  font-weight: 400;
  font-size: 14px;
`

export const IbTypo08 = styled(Div)`
  font-weight: 400;
  font-size: 14px;
  text-decoration: underline;
`

export const IbTypo09 = styled(Div)`
  font-weight: 400;
  font-size: 12px;
`
export const IbTypo10 = styled(Div)`
  font-weight: 400;
  font-size: 16px;
`
export const IbTypo11 = styled(Div)`
  font-weight: 400;
  font-size: 12px;
  font-style: italic;
`
export const IbTypo12 = styled(Div)`
  font-weight: 400;
  font-size: 14px;
  font-style: italic;
`
export const IbTypo13 = styled(Div)`
  font-weight: 700;
  font-size: 12px;
`
export const IbTypo14 = styled(Div)`
  font-weight: 700;
  font-size: 14px;
`





