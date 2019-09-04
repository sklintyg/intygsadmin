import styled from 'styled-components'
import ibValues from './iaValues'

/**
 *
 * Typical usage is to nest
 * <FlexColumnContainer>
 *     <ScrollingContainer>
 *         <WorkareaContainer>
 * They can also be extendend  e.g to have different background etc.
 */
export const FlexColumnContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`

export const PageHeaderContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`

export const ScrollingContainer = styled.div`
  overflow-y: auto;
  background: #fff;
  height: 100%;
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`
export const WorkareaContainer = styled.div`
  padding: 30px;
`

export const Section = styled.div`
  padding-bottom: 16px;
`

export const ErrorSection = styled.div`
  border-top: 1px solid #dee2e6;
`

export const ErrorWrapper = styled.div`
  margin: 15px 15px 0 15px;
`

export const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`

export const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;

  img {
    width: 100%;
    padding-bottom: 20px;
  }
`
