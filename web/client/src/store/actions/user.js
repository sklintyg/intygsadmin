import {fetchAnvandare} from "../../api/userApi";

export const GET_USER = 'GET_USER';
export const GET_USER_SUCCESS = 'GET_USER_SUCCESS';
export const GET_USER_FAILURE = 'GET_USER_FAILURE';
export const SET_ENHET = 'SET_ENHET';
export const SET_ENHET_SUCCESS = 'SET_ENHET_SUCCESS';
export const SET_ENHET_FAILURE = 'SET_ENHET_FAILURE';

export const getUser = () => {
  return (dispatch) => {

    dispatch({
      type: GET_USER
    });

    return fetchAnvandare().then(
      json => dispatch({
        type: GET_USER_SUCCESS,
        payload: json
      })
    ).catch(
      errorResponse => dispatch({
        type: GET_USER_FAILURE,
        payload: errorResponse
      })
    );
  }
};
