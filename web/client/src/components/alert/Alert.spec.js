import {shallow} from 'enzyme';
import IaAlert from './Alert';
import {Check, ErrorOutline, InfoOutline, Security, Warning} from '../styles/iaSvgIcons';

describe('<IaAlert />', () => {
  it('Render alert and children', () => {
    const wrapper = shallow( < IaAlert
    type = {alertType.INFO} > Alert < /IaAlert>);
    expect(wrapper.find('div').text()).toEqual('Alert');
  });

  describe('icons', () => {
    it('Info icon', () => {
      const wrapper = shallow( < IaAlert
      type = {alertType.INFO} > Alert < /IaAlert>);
      expect(wrapper.find(InfoOutline)).toHaveLength(1);
    });

    it('Sekretess icon', () => {
      const wrapper = shallow( < IaAlert
      type = {alertType.SEKRETESS} > Alert < /IaAlert>);
      expect(wrapper.find(Security)).toHaveLength(1);
    });

    it('ErrorOutline icon', () => {
      const wrapper = shallow( < IaAlert
      type = {alertType.OBSERVANDUM} > Alert < /IaAlert>);
      expect(wrapper.find(ErrorOutline)).toHaveLength(1);
    });

    it('Check icon', () => {
      const wrapper = shallow( < IaAlert
      type = {alertType.CONFIRM} > Alert < /IaAlert>);
      expect(wrapper.find(Check)).toHaveLength(1);
    });

    it('Error icon', () => {
      const wrapper = shallow( < IaAlert
      type = {alertType.ERROR} > Alert < /IaAlert>);
      expect(wrapper.find(Warning)).toHaveLength(1);
    });
  });

});
