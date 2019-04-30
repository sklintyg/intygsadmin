export const OPEN_MODAL = 'OPEN_MODAL';
export const CLOSE_MODAL = 'CLOSE_MODAL';
export const CLOSE_ALL_MODALS = 'CLOSE_ALL_MODAL';

export const openModal = (id, data) => {
  return {
    type: OPEN_MODAL,
    payload: {
      id,
      ...data
    }
  };
};

export const closeModal = (id) => {
  return {
    type: CLOSE_MODAL,
    payload: {
      id
    }
  }
};

export const closeAllModals = () => {
  return {
    type: CLOSE_ALL_MODALS,
    payload: {}
  }
};

export const displayErrorModal = (data) => (dispatch) => {
  dispatch(openModal('errorModal', data))
}
