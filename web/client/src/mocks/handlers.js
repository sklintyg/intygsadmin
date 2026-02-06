import { http, HttpResponse } from 'msw'

export const handlers = [
  http.get('/api/anvandare', () => {
    return HttpResponse.json({
      employeeHsaId: 'TEST123',
      name: 'Test User',
      intygsadminRole: 'FULL',
      logoutUrl: '/logout',
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
  http.get('/api/user', () => {
    return HttpResponse.json({
      content: [
        {
          id: '123e4567-e89b-12d3-a456-426614174000',
          hsaId: 'TST001',
          name: 'Test User 1',
          intygsadminRole: 'FULL',
          createdAt: '2025-01-01T10:00:00Z',
        },
        {
          id: '223e4567-e89b-12d3-a456-426614174000',
          hsaId: 'TST002',
          name: 'Test User 2',
          intygsadminRole: 'BAS',
          createdAt: '2025-01-02T10:00:00Z',
        },
      ],
      pageable: {
        pageNumber: 0,
        pageSize: 10,
        sort: {
          empty: false,
          sorted: true,
          unsorted: false,
        },
        offset: 0,
        paged: true,
        unpaged: false,
      },
      totalPages: 1,
      totalElements: 2,
      last: true,
      size: 10,
      number: 0,
      sort: {
        empty: false,
        sorted: true,
        unsorted: false,
      },
      numberOfElements: 2,
      first: true,
      empty: false,
    })
  }),
  http.put('/api/user', ({ request }) => {
    return HttpResponse.json({
      id: '123e4567-e89b-12d3-a456-426614174000',
      name: 'New User',
      employeeHsaId: 'NEWUSER',
      intygsadminRole: 'FULL',
      createdAt: '2025-01-01T10:00:00Z',
    })
  }),
  http.post('/api/user/:id', ({ request }) => {
    return HttpResponse.json({
      id: '123e4567-e89b-12d3-a456-426614174000',
      name: 'Updated User',
      employeeHsaId: 'UPDUSER',
      intygsadminRole: 'BAS',
      createdAt: '2025-01-01T10:00:00Z',
    })
  }),
  http.delete('/api/user/:id', () => {
    return HttpResponse.json({}, { status: 200 })
  }),
  http.get('/api/dataExport', () => {
    return HttpResponse.json({
      content: [
        {
          terminationId: '550e8400-e29b-41d4-a716-446655440000',
          hsaId: 'ORG123',
          organizationNumber: '12345678901',
          personId: 'SKA001',
          phoneNumber: '+46701234567',
          status: 'Kryptonyckel skickad',
          createdAt: '2025-01-15T10:00:00Z',
        },
      ],
      pageable: {
        pageNumber: 0,
        pageSize: 10,
        sort: {
          empty: false,
          sorted: true,
          unsorted: false,
        },
        offset: 0,
        paged: true,
        unpaged: false,
      },
      page: {
        number: 0,
        size: 10,
        totalElements: 1,
      },
      totalPages: 1,
      totalElements: 1,
      last: true,
      size: 10,
      number: 0,
      numberOfElements: 1,
      first: true,
      empty: false,
    })
  }),
  http.post('/api/dataExport', ({ request }) => {
    return HttpResponse.json({
      terminationId: '550e8400-e29b-41d4-a716-446655440001',
      hsaId: 'ORG124',
      organizationNumber: '12345678902',
      personId: 'SKA002',
      phoneNumber: '+46701234568',
      status: 'Skapad',
      createdAt: '2025-01-15T11:00:00Z',
    })
  }),
  http.post('/api/dataExport/update', ({ request }) => {
    return HttpResponse.json({
      terminationId: '550e8400-e29b-41d4-a716-446655440000',
      hsaId: 'ORG123',
      organizationNumber: '12345678901',
      personId: 'SKA001',
      phoneNumber: '+46701234569',
      status: 'Uppdaterad',
      createdAt: '2025-01-15T10:00:00Z',
    })
  }),
  http.post('/api/dataExport/:terminationId/erase', () => {
    return HttpResponse.json('Raderad')
  }),
  http.post('/api/dataExport/:terminationId/resendkey', () => {
    return HttpResponse.json('Nyckel skickad på nytt')
  }),
  http.get('/api/banner/activeAndFuture', () => {
    return HttpResponse.json([
      {
        id: '550e8400-e29b-41d4-a716-446655440002',
        application: 'intygsadmin',
        message: 'Test banner',
        displayFrom: '2025-01-01T00:00:00Z',
        displayTo: '2025-12-31T23:59:59Z',
      },
    ])
  }),
  http.put('/api/banner', ({ request }) => {
    return HttpResponse.json({
      id: '550e8400-e29b-41d4-a716-446655440002',
      application: 'intygsadmin',
      message: 'New banner',
      displayFrom: '2025-01-01T00:00:00Z',
      displayTo: '2025-12-31T23:59:59Z',
    })
  }),
  http.post('/api/banner/:id', ({ request }) => {
    return HttpResponse.json({
      id: '550e8400-e29b-41d4-a716-446655440002',
      application: 'intygsadmin',
      message: 'Updated banner',
      displayFrom: '2025-01-01T00:00:00Z',
      displayTo: '2025-12-31T23:59:59Z',
    })
  }),
  http.delete('/api/banner/:id', () => {
    return HttpResponse.json({}, { status: 200 })
  }),
  http.get('/api/integratedUnits', () => {
    return HttpResponse.json({
      content: [
        {
          id: '550e8400-e29b-41d4-a716-446655440003',
          hsaId: 'SE2321000016-1000',
          name: 'Test Unit 1',
          createdAt: '2025-01-01T10:00:00Z',
        },
      ],
      page: {
        number: 0,
        size: 10,
        totalElements: 1,
      },
      totalElements: 1,
      numberOfElements: 1,
      first: true,
      last: true,
    })
  }),
  http.get('/api/privatePractitioner', () => {
    return HttpResponse.json({
      content: [
        {
          id: '550e8400-e29b-41d4-a716-446655440004',
          hsaId: 'SE2321000123-1000',
          name: 'Test Practitioner 1',
          createdAt: '2025-01-01T10:00:00Z',
        },
      ],
      page: {
        number: 0,
        size: 10,
        totalElements: 1,
      },
      totalElements: 1,
      numberOfElements: 1,
      first: true,
      last: true,
    })
  }),
  http.post('/api/config/appConfig', () => {
    return HttpResponse.json({
      version: '1.0.0-TEST',
      loginUrl: '/fake-login',
    })
  }),
]
