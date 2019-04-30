import React from 'react';
import { shallow } from 'enzyme';

import Vardgivare from './Vardgivare';
import SelectEnhet from './SelectEnhet';

describe('<SelectEnhet />', () => {
  const selectEnhet = () => {};

  it('empty tree', () => {
    const authoritiesTree = [];

    const wrapper = shallow(<SelectEnhet authoritiesTree={authoritiesTree} selectEnhet={selectEnhet} />);
    expect(wrapper.find(Vardgivare)).toHaveLength(0);
  });

  it('initiallyExpanded true', () => {
    const authoritiesTree = [{
      id: '123',
      name: 'name'
    }];

    const wrapper = shallow(<SelectEnhet authoritiesTree={authoritiesTree} selectEnhet={selectEnhet} />);
    expect(wrapper.find(Vardgivare)).toHaveLength(1);
  });
});
