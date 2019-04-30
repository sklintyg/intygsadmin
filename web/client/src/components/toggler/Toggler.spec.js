import React from 'react';
import { shallow } from 'enzyme';
import sinon from 'sinon';

import Toggler from './Toggler';
import {CollapseIcon, ExpandIcon} from "../styles/IbSvgIcons";

describe('<Toggler />', () => {
  it('renders a CollapseIcon when expanded = false', () => {
    const wrapper = shallow(<Toggler expanded={false} />).shallow();
    expect(wrapper.find(CollapseIcon)).toHaveLength(1);
  });

  it('renders a ExpandIcon when expanded = true', () => {
    const wrapper = shallow(<Toggler expanded={true} />).shallow();
    expect(wrapper.find(ExpandIcon)).toHaveLength(1);
  });

  it('simulates click events', () => {
    const onButtonClick = sinon.spy();
    const wrapper = shallow(<Toggler handleToggle={onButtonClick} />);
    wrapper.simulate('click');
    expect(onButtonClick).toHaveProperty('callCount', 1);
  });
});
