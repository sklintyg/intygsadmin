# Intygsadmin

## Komma igång med lokal installation
Den här sektionen beskriver hur man bygger applikationen för att kunna köras helt fristående.

Vi använder Gradle för att bygga applikationerna.

Börja med att skapa en lokal klon av källkodsrepositoryt:

```
$ git clone https://github.com/sklintyg/intygsadmin.git
```

Läs vidare i gemensam dokumentation [devops/develop README-filen](https://github.com/sklintyg/devops/tree/release/2021-1/develop/README.md)
    
### Logga in med OpenID Connect
Inloggning med OIDC är påslaget som default, men kräver konfiguration och anmälan hos Inera IdP för att fungera.
När dessa delar är på plats så kan inloggning ske via "Logga in knappen" i GUI. Inloggning kräver SITHS-kort samt
att användaren finns upplagd i Intygsadmins databas. Se "Administrera användare". 

#### Beställa anslutning
För att ansluta till Inera IDP krävs godkänd förstudie. Då denna är klar kan beställning av ny klient göras
och då skall följande uppgifter anges:
- Klient ID: valfritt, men kan också erhållas av IdPn.
- Klient Secret: Erhålls från IdPn.
- Autentiseringstyp: Secret post
- Scopes/Claims: employeeHsaID, given_name, family_name
- Inloggningsmetod: NetID Enterprise
- Redirect URI: \<IAhost\>/login/inera
- Logout Redirect URI: \<IAhost\>/#/loggedout/m
- Kontakt: Kontaktperson/funktionsbrevlåda


#### Konfigurera Intygsadmin
När klienten är upplagd hos Inera IDP, så skall följande uppgifter konfigureras i Intygsadmin:
- inera.idp.client-id: Det som angavs/erhölls till/från IdPn. 
- inera.idp.client-secret: Det som erhölls från IdPn.
- inera.idp.redirect-uri: \<IAhost\>/login/inera.
- inera.idp.logout-redirect-uri: \<IAhost\>/#/loggedout/m 
- inera.idp.issuer-uri: Issuer ID för IdP. Vanligtvis dess hostname följt av /oidc 


### Administrera användare
För att en användare skall ges behörighet till tjänsten (via normal inloggning) krävs att användaren
finns upplagd i databasen med sitt hsa-id samt en tillhörande roll (ADMIN/BASIC).

Detta kan ske direkt via databasen, eller via guit i intygsadmin.

## Licens
Copyright (C) 2021 Inera AB (http://www.inera.se)

Intygsadmin is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Intygsadmin is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

Se även [LICENSE.md](LICENSE.md). 