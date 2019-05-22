let api = require('./real/banner.api');

export const createBanner = (banner) => api.createBanner(banner);

export const removeBanner = (banner) => api.removeBanner(banner);
