import v4 from 'uuid/v4'

const bestallningDb = [
  {
    id: v4(),
    status: 'Accepterad',
    ankomstDatum: '2019-04-01',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19931230-2384',
      sekretessMarkering: false,
      name: 'Anna-Stina Marianne Rönnberg'
    }
  },
  {
    id: v4(),
    status: 'Läst',
    ankomstDatum: '2019-03-31',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19830923-9294',
      sekretessMarkering: false,
      name: 'Eric SÃ¶derman'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-30',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19671031-3195',
      sekretessMarkering: false,
      name: 'Henry Dag Hallgren'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-29',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19591016-2642',
      sekretessMarkering: false,
      name: 'Carina  Wahlström'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-29',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19660407-2667',
      sekretessMarkering: false,
      name: 'Anna-Stina  Klingberg'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-29',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19520617-2339',
      sekretessMarkering: false,
      name: 'Björn Anders Daniel Annat Pärsson'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-29',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19121212-1212',
      sekretessMarkering: false,
      name: 'Tolvan Tolvenius Tolvansson'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-28',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19900614-2385',
      sekretessMarkering: false,
      name: 'Charlotta Evelina Blomgren'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-28',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19540123-2540',
      sekretessMarkering: true,
      name: 'Maj  Pärsson'
    }
  },
  {
    id: v4(),
    status: 'Oläst',
    ankomstDatum: '2019-03-28',
    intygTyp: 'AF00213',
    invanare: {
      personId: '19550607-2882',
      sekretessMarkering: false,
      name: 'Karin Eva Svensson'
    }
  }
]

export default bestallningDb

export const bestallningsConfig = bestallning => ({
  id: bestallning.id,
  intygsTyp: bestallning.intygName,
  invanare: {
    personId: bestallning.patient.id,
    namn: bestallning.patient.name
  },
  status: bestallning.status,
  ankomstDatum: bestallning.ankomstDatum,
  fragor: [
    {
      rubrik: 'Denna förfrågan avser',
      delfragor: [
        {
          etikett: 'Avsändare',
          bild: 'AF_LOGO'
        },
        {
          etikett: 'Information',
          text:
            'Arbetsförmedlingen behöver ett medicinskt utlåtande för att klargöra förutsättningarna för arbetssökande som har ett behov av fördjupat stöd. Bland annat för att kunna:' +
            '- utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga' +
            '- göra en lämplig anpassning för en arbetssökande som deltar i ett arbetsmarknadspolitiskt program och som har blivit sjuk' +
            '- erbjuda (lämpliga) utredande, vägledande, rehabiliterande eller arbetsförberedande insatser' +
            'Utfärda Arbetsförmedlingens medicinska utlåtande via journalsystemet eller Webcert.' +
            'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms. Faktureringsuppgifter finns i slutet av beställningen.'
        },
        {
          etikett: 'Samtycke',
          text: 'Arbetsförmedlingen har erhållit samtycke från den arbetssökande om att skicka denna förfrågan.'
        }
      ]
    },
    {
      rubrik: 'Invånare',
      delfragor: [
        {
          etikett: 'Personnummer',
          svar: bestallning.patient.id
        },
        {
          etikett: 'Namn',
          svar: bestallning.patient.name
        }
      ]
    },
    {
      rubrik: 'Förfrågan',
      delfragor: [
        {
          etikett: 'Syftet med förfrågan',
          svar: bestallning.syfte
        },
        {
          etikett: 'Beskriv den arbetssökandes bakgrund och nuläge',
          svar: bestallning.patient.bakgrund
        },
        {
          etikett: 'Planerade insatser hos Arbetsförmedlingen',
          svar: bestallning.planeradeInsatser
        }
      ]
    },
    {
      rubrik: 'Kontaktuppgifter arbetsförmedlingen',
      delfragor: [
        {
          etikett: 'Arbetsförmedlare',
          svar: bestallning.handlaggare.name + '\n' + bestallning.handlaggare.epost + '\n' + bestallning.handlaggare.telefonnummer
        },
        {
          etikett: 'Arbetsförmedlingskontor',
          svar: bestallning.kontor.name + '\n' + bestallning.kontor.adress
        }
      ]
    },
    {
      rubrik: 'Fakturainformation',
      delfragor: [
        {
          etikett: 'Information',
          text:
            'Följande uppgifter ska användas för att fakturera Arbetsförmedlingen för ett utfärdat medicinskt utlåtande.' +
            'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms.'
        },
        {
          etikett: 'Utrednings-ID',
          svar: bestallning.utredning.id
        },
        {
          etikett: 'Kostnadsställe',
          svar: bestallning.kontor.kostnadsstalle
        },
        {
          etikett: 'Moms',
          text: '25%'
        },
        {
          etikett: 'Skicka fakturan till',
          text: 'Arbetsförmedlingen\n' + 'Skanningscentralen\n' + '681 85 Kristinehamn'
        }
      ]
    }
  ]
})

export const decorateForBestallning = bList => {
  return bList.map(b => {
    b.handlaggare = { name: 'Handläggarn Handläggarensson', epost: 'handlaggaren@af.se', telefonnummer: '010 - 112233' }
    b.kontor = { name: 'kontorsnamn', adress: 'kontorsadress', kostnadsstalle: '12345' }
    b.utredning = { id: '123' }
    b.planeradeInsatser = 'Planerade insatser'
    b.patient.bakgrund = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
    b.syfte =
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vel ultricies dui. Pellentesque fringilla velit rhoncus, luctus nibh in, tempor turpis.'
    return b
  })
}
