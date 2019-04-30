export const ACCEPT_COOKIE_BANNER = 'ACCEPT_COOKIE_BANNER';

export const acceptCookieBanner = (data) => {
  return {
    type: ACCEPT_COOKIE_BANNER,
    payload: data
  };
};
