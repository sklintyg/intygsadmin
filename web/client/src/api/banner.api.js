let api = require('./real/banner.api');

export const createBanner = (banner) => api.createBanner(banner);

export const removeBanner = (bannerId) => api.removeBanner(bannerId);

export const updateBanner = (banner, bannerId) => api.updateBanner(banner, bannerId);

export const fetchFutureBanners = (application) => api.fetchFutureBanners(application);
