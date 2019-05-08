import React from 'react';
import { shallow } from 'enzyme';
import IbAlert, { alertType } from './Alert';
import { InfoIcon, Security, ErrorOutline, Check, Warning } from '../styles/iaSvgIcons';

describe('<IBAlert />', () => {
  it('Render alert and children', () => {
    const wrapper = shallow(<IbAlert type={alertType.INFO}>Alert</IbAlert>);
    expect(wrapper.find('div').text()).toEqual('Alert');
  });

  describe('icons', () => {
    it('Info icon', () => {
      const wrapper = shallow(<IbAlert type={alertType.INFO}>Alert</IbAlert>);
      expect(wrapper.find(InfoIcon)).toHaveLength(1);
    });

    it('Sekretess icon', () => {
      const wrapper = shallow(<IbAlert type={alertType.SEKRETESS}>Alert</IbAlert>);
      expect(wrapper.find(Security)).toHaveLength(1);
    });

    it('ErrorOutline icon', () => {
      const wrapper = shallow(<IbAlert type={alertType.OBSERVANDUM}>Alert</IbAlert>);
      expect(wrapper.find(ErrorOutline)).toHaveLength(1);
    });

    it('Check icon', () => {
      const wrapper = shallow(<IbAlert type={alertType.CONFIRM}>Alert</IbAlert>);
      expect(wrapper.find(Check)).toHaveLength(1);
    });

    it('Error icon', () => {
      const wrapper = shallow(<IbAlert type={alertType.ERROR}>Alert</IbAlert>);
      expect(wrapper.find(Warning)).toHaveLength(1);
    });
  });

});
