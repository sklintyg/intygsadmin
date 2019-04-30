import React from 'react';
import { mount, shallow } from 'enzyme';
import sinon from 'sinon';
import { Button, ModalBody } from 'reactstrap';
import { ActionButton } from '../styles';
import ChangeEnhet from './ChangeEnhet';

jest.mock('../../selectEnhet', () => ()=> <div id="mockUserCom">selectEnhet</div>)

describe('<ChangeEnhet />', () => {
  let handleOpen;
  let handleClose;

  beforeEach(() => {
    handleOpen = sinon.spy();
    handleClose = sinon.spy();
  })

  it('default closed', () => {
    const wrapper = mount(<ChangeEnhet handleOpen={handleOpen} handleClose={handleClose} isOpen={false} />);
    expect(wrapper.find(ActionButton)).toHaveLength(1);
    expect(wrapper.find(ModalBody)).toHaveLength(0);
  });

  it('default open', () => {
    const wrapper = mount(<ChangeEnhet handleOpen={handleOpen} handleClose={handleClose} isOpen={true} />);
    expect(wrapper.find(ActionButton)).toHaveLength(1);
    expect(wrapper.find(ModalBody)).toHaveLength(1);
  });

  it('onClick', () => {
    const wrapper = shallow(<ChangeEnhet handleOpen={handleOpen} handleClose={handleClose} isOpen={false} />);
    wrapper.find(ActionButton).simulate('click');

    expect(handleOpen).toHaveProperty('callCount', 1);
  });

  it('onClose', () => {
    const wrapper = shallow(<ChangeEnhet handleOpen={handleOpen} handleClose={handleClose} isOpen={true} />);
    wrapper.find(Button).simulate('click');

    expect(handleClose).toHaveProperty('callCount', 1);
  });
});
