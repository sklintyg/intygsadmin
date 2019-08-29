let api = require('./real/integratedUnits.api');

export const fetchIntegratedUnit = (hsaId) => api.fetchIntegratedUnit(hsaId);

export const fetchIntegratedUnitsFile = () => api.fetchIntegratedUnitsFile();
