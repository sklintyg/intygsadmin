const userDb = {

  "hsaId": "ib-user-3",
  "namn": "Harald Alltsson",
  "authenticationScheme": "urn:inera:intygsadmin:siths:fake",
  "features": {},
  "authoritiesTree": [{
    "id": "vastmanland",
    "name": "Landstinget Västmanland",
    "vardenheter": [{
      "id": "centrum-ost",
      "name": "Vårdcentrum i Öst",
      "parentHsaId": "vastmanland",
      "parentHsaName": "Landstinget Västmanland",
      "orgNrVardgivare": "2-orgnr-vastmanland"
    }]
  }, {
    "id": "ostergotland",
    "name": "Landstinget Östergötland",
    "vardenheter": [{
      "id": "linkoping",
      "name": "Linköpings Universitetssjukhus",
      "parentHsaId": "ostergotland",
      "parentHsaName": "Landstinget Östergötland",
      "orgNrVardgivare": "2-orgnr-ostergotland"
    }]
  }, {
    "id": "IFV1239877878-1041",
    "name": "WebCert-Vårdgivare1",
    "vardenheter": [{
      "id": "IFV1239877878-1042",
      "name": "WebCert-Enhet1",
      "parentHsaId": "IFV1239877878-1041",
      "parentHsaName": "WebCert-Vårdgivare1",
      "orgNrVardgivare": "2-orgnr-1041"
    }]
  }, {
    "id": "IFV1239877878-1043",
    "name": "WebCert-Vårdgivare2",
    "vardenheter": [{
      "id": "IFV1239877878-1045",
      "name": "WebCert-Enhet2",
      "parentHsaId": "IFV1239877878-1043",
      "parentHsaName": "WebCert-Vårdgivare2",
      "orgNrVardgivare": "2-orgnr-1043"
    }, {
      "id": "IFV1239877878-104D",
      "name": "WebCert-Enhet3",
      "parentHsaId": "IFV1239877878-1043",
      "parentHsaName": "WebCert-Vårdgivare2",
      "orgNrVardgivare": "2-orgnr-1041"
    }]
  }],
  "currentRole" : {
    "name" : "VARDADMIN",
    "desc" : "Vårdadministratör",
    "privileges" : [ {
      "name" : "VISA_BESTALLNING",
      "desc" : "Visa beställning",
      "intygstyper" : [ ],
      "requestOrigins" : [ ]
    }, {
      "name" : "RADERA_BESTALLNING",
      "desc" : "Radera beställning",
      "intygstyper" : [ ],
      "requestOrigins" : [ ]
    }, {
      "name" : "LISTA_BESTALLNINGAR",
      "desc" : "Lista beställningar",
      "intygstyper" : [ ],
      "requestOrigins" : [ ]
    }, {
      "name" : "ANDRA_STATUS_PA_BESTALLNING",
      "desc" : "Ändra status på beställning",
      "intygstyper" : [ ],
      "requestOrigins" : [ ]
    }, {
      "name" : "SKRIVA_UT",
      "desc" : "Skriva ut",
      "intygstyper" : [ ],
      "requestOrigins" : [ ]
    } ]
  },
  "unitContext" : {
    "id" : "IFV1239877878-1042",
    "name" : "WebCert-Enhet1",
    "parentHsaId" : "IFV1239877878-1041",
    "parentHsaName" : "WebCert-Vårdgivare1",
    "orgNrVardgivare" : "2-orgnr-1041"
  },
  "totaltAntalVardenheter": 5
}
export default userDb;
