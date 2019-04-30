import React from 'react';
import { shallow, mount } from 'enzyme';
import sinon from 'sinon';

import Vardgivare, {VeButton} from './Vardgivare';
import Toggler from '../toggler/Toggler';

describe('<Vardgivare />', () => {
  const vg = {
    name: 'vg',
    vardenheter: [{
      id: '123',
      name: 've'
    }]
  };

  it('initiallyExpanded false', () => {
    const wrapper = shallow(<Vardgivare initiallyExpanded={false} vg={vg} handleSelect={() => {}} />);
    expect(wrapper.find(VeButton)).toHaveLength(0);
  });

  it('initiallyExpanded true', () => {
    const wrapper = shallow(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={() => {}} />);
    expect(wrapper.find(VeButton)).toHaveLength(1);
  });

  it('Toggle expanded', () => {
    const wrapper = mount(<Vardgivare initiallyExpanded={false} vg={vg} handleSelect={() => {}} />);

    expect(wrapper.find(VeButton)).toHaveLength(0);

    wrapper.find(Toggler).simulate('click');

    expect(wrapper.find(VeButton)).toHaveLength(1);
  });

  it('active Unit true', () => {
    const unitContext = {
      id: '123'
    };

    const wrapper = mount(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={() => {}} unitContext={unitContext} />);

    expect(wrapper.find(VeButton).text()).toContain('(nuvarande enhet)');
  });

  it('select unit', () => {
    const handleSelect = sinon.spy();

    const wrapper = mount(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={handleSelect} />);
    wrapper.find(VeButton).simulate('click');

    expect(handleSelect).toHaveProperty('callCount', 1);
  });
});
