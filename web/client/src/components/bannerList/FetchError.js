import React from 'react';
import PropTypes from 'prop-types'
import { ErrorPageIcon } from '../styles/iaSvgIcons';
import styled from 'styled-components'

const Wrapper = styled.div`text-align: center`

const FetchError = () => (
  <Wrapper>
    <ErrorPageIcon/>
    <p>Kunde inte hämta beställningar.</p>
  </Wrapper>
);

FetchError.propTypes = {
  message: PropTypes.string,
  onRetry: PropTypes.func.isRequired,
};

export default FetchError;
