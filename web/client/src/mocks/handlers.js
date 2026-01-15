import { http, HttpResponse } from 'msw'

export const handlers = [
  http.get('/api/user', () => {
    return HttpResponse.json({
      hsaId: 'TEST123',
      name: 'Test User',
      role: 'ADMIN',
      isAuthenticated: true,
    })
  }),
  http.get('/api/config', () => {
    return HttpResponse.json({
      version: '1.0.0-TEST',
      loginUrl: '/fake-login',
    })
  }),
  http.get('/api/session/ping', () => {
    return HttpResponse.json({
      hasSession: true,
      secondsUntilExpire: 3600,
    })
  }),
]
