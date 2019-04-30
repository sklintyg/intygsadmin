import reducer from './modal'
import { OPEN_MODAL, CLOSE_MODAL, CLOSE_ALL_MODALS } from '../actions/modal';

describe('modal reducer', () => {
  it('should return the initial state', () => {
    const initState = {};

    expect(reducer(undefined, {})).toEqual(initState);
  });

  describe('should handle ' + OPEN_MODAL, () => {
    const id = '123';
    const action = {
      type: OPEN_MODAL,
      payload: {
        id
      }
    };

    it('empty state', () => {
      const stateBefore = {};
      const stateAfter = {[id]: true};

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    });

    it('none empty state', () => {
      const stateBefore = {
        something: 'something'
      };
      const stateAfter = {
        something: 'something',
        [id]: true
      };

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    })
  });

  describe('should handle ' + OPEN_MODAL + ' with data', () => {
    const id = '123';
    const extraData = 'modal data'
    const action = {
      type: OPEN_MODAL,
      payload: {
        id,
        extraData
      }
    };

    it('empty state', () => {
      const stateBefore = {};
      const stateAfter = {[id]: true, [id + 'Data']: {extraData}};

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    });

    it('none empty state', () => {
      const stateBefore = {
        something: 'something'
      };
      const stateAfter = {
        something: 'something',
        [id]: true,
        [id + 'Data']: {extraData}
      };

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    })
  });

  describe('should handle ' + CLOSE_MODAL, () => {
    const id = '123';
    const action = {
      type: CLOSE_MODAL,
      payload: {
        id
      }
    };

    it('empty state', () => {
      const stateBefore = {};
      const stateAfter = {[id]: false};

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    });

    it('none empty state', () => {
      const stateBefore = {
        something: 'something'
      };
      const stateAfter = {
        something: 'something',
        [id]: false
      };

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    })
  });

  describe('should handle ' + CLOSE_ALL_MODALS, () => {
    const action = {
      type: CLOSE_ALL_MODALS,
      payload: {}
    };

    it('empty state', () => {
      const stateBefore = {};
      const stateAfter = {};

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    });

    it('none empty state', () => {
      const stateBefore = {
        something: 'something',
        '123': true
      };
      const stateAfter = {};

      expect(reducer(stateBefore, action)).toEqual(stateAfter);
    })
  });
})
