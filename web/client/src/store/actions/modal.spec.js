import * as actions from './modal'

describe('modal', () => {
  it('openModal', () => {
    const id = '123';
    const expectedAction = {
      type: actions.OPEN_MODAL,
      payload: {
        id
      }
    };
    expect(actions.openModal(id)).toEqual(expectedAction);
  });

  it('closeModal', () => {
    const id = '123';
    const expectedAction = {
      type: actions.CLOSE_MODAL,
      payload: {
        id
      }
    };
    expect(actions.closeModal(id)).toEqual(expectedAction);
  });

  it('closeAllModals', () => {
    const expectedAction = {
      type: actions.CLOSE_ALL_MODALS,
      payload: {}
    };
    expect(actions.closeAllModals()).toEqual(expectedAction);
  });
})
