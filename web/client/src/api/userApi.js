let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/userApi');
} else {
  api = require('./mock/userApi')
}

export const fetchAnvandare = () => api.fetchAnvandare();

export const pollSession = () => api.pollSession();
