let api = require('./real/bannerApi');

export const createBanner = (banner) => api.createBanner(banner);

export const removeBanner = (banner) => api.removeBanner(banner);
