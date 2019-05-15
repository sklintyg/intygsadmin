import v4 from 'uuid/v4'

const bannerDb = [
  {
    id: v4(),
    createdAt: '2019-04-01',
    application: 'WEBCERT', // REHABSTOD, STATISTIK
    displayFrom: '2019-04-01',
    displayTo: '2019-04-02',
    message: 'Yoohoo',
    priority: 'LOW',
    status: 'ACTIVE',  // FUTURE, ACTIVE, FINISHED
  },
]

export default bannerDb
