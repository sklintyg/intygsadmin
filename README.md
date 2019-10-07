# Intygsadmin

## Komma igång med lokal installation
Den här sektionen beskriver hur man bygger applikationen för att kunna köras helt fristående.

Vi använder Gradle för att bygga applikationerna.

Börja med att skapa en lokal klon av källkodsrepositoryt:

    $ git clone https://github.com/sklintyg/intygsadmin.git

Efter att man har klonat repository navigera till den klonade katalogen intygsadmin och kör följande kommando:

    $ cd intygsadmin
    $ ./gradlew clean build

Det här kommandot kommer att bygga samtliga moduler i systemet. 

När applikationen har byggt klart, kan man gå till `/` och köra kommandot

    $ ./gradlew bootRun

för att starta applikationen lokalt.

För att starta applikationen i debugläge används:

    $ ./gradlew bootRun --debug-jvm
    
Applikationen kommer då att starta upp med debugPort = **5005**. Det är denna port du ska använda när du sätter upp din 
debug-konfiguration i din utvecklingsmiljö.

För att starta applicationen och även bygga frontend-klienten med alla statiska filer, kör kommandot

    $ ./gradlew bootRun -P client

för att starta applikationen lokalt.

Applikationen kan köras med profil **fake** för att ge åtkomst till allt via inloggningssida. 


### H2
Du hittar databasen lokalt på `http://localhost:8680/h2-console`

### Frontend
För att köra med live-reload.

    cd web/client
    npm install
    npm start
    
Läs mer på i [README-filen](web/client/README.md) för frontend

### Köra integrationstester
Vi har integrationstester skrivna med [REST-Assured](https://github.com/jayway/rest-assured)

De körs inte automatiskt vid bygge av applikationen utan man behöver köra dem med kommandot

    $ ./gradlew restassured
    
Man kan exekvera enskilda tester genom exempelvis:

    $ ./gradlew restassured --tests *BestallningControllerIT.testGetBestallningar
    
För att debugga själva testet, lägg på --debug-jvm dvs:

    $ ./gradlew restassured --debug-jvm
    ... loggar ...
    Listening for transport dt_socket at address: 5005

Anslut nu remote debugging i IDEA.

För att logga från testet till stdout:

    $ ./gradlew restassured -info
    
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
