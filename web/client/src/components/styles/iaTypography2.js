import styled from 'styled-components'
import colors from './iaColors2';

/*
100 = Roboto Thin
300 = Roboto Light
400 = Roboto Regular
500 = Roboto Medium
700 = Roboto Bold

You can either use these directly or use them as a base for refinements.
To just change tag use the "as" attribute i.e  <IaTypo01 as="h1">
To customize more use construct like:

const SpecialRubrik = Styled(Typo.IaTypo02)`
  border-bottom: 1px solid ${Colors.IA_COLOR_08};
  border-radius: 4px 4px 0 0;
  color: ${Colors.IA_COLOR_06};
`;

in bootstrap-overrides, the base font of the body is IaTypo07 with color IbColor08 which will be inherited
if not specified as something else.
 */

const Div = styled.div`
  color: ${props => props.color || colors.IA_COLOR_99};
`

export const IaTypo01 = styled(Div)`
  font-weight: 500;
  font-size: 18px;
`

export const IaTypo02 = styled(Div)`
  font-weight: 400;
  font-size: 16px;
`

export const IaTypo03 = styled(Div)`
  font-weight: 500;
  font-size: 16px;
`

export const IaTypo04 = styled(Div)`
  font-weight: 500;
  font-size: 14px;
`

export const IaTypo05 = styled(Div)`
  font-weight: 400;
  font-size: 14px;
`

export const IaTypo06 = styled(Div)`
  font-weight: 400;
  font-size: 12px;
`

export const IaTypo07 = styled(Div)`
  font-weight: 700;
  font-size: 14px;
`

export const IaTypo08 = styled(Div)`
  font-weight: 400;
  font-size: 14px;
  text-style: italic;
`

export const IaTypo09 = styled(Div)`
  font-weight: 700;
  font-size: 20px;
`
