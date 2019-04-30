// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add("login", loginId => {
  cy.visit("/welcome.html");
  cy.get("#jsonSelect").select(loginId);
  cy.get("#loginBtn").click();
});

Cypress.Commands.add("addBestallning", () => {
  cy.fixture("bestallning.json").then(bestallning => {
    bestallning.ankomstDatum = new Date().toISOString();
    cy.request("POST", "/api/test/bestallningar", bestallning);
  });
});

Cypress.Commands.add("removeBestallning", id => {
  cy.request({
    method: "DELETE",
    url: "/api/test/bestallningar/" + id,
    failOnStatusCode: false
  });
});
