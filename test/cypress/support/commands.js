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

Cypress.Commands.add("loadWelcome", () => {
  cy.server()
  cy.route({
    method: 'GET',
    url: '**/fake-api/users**'
  }).as('fake-users');

  cy.removeFetch();

  cy.visit("/welcome.html");
  cy.wait('@fake-users');
  cy.wait(200);
});

Cypress.Commands.add("login", loginId => {

  cy.loadWelcome();
  cy.get(`#${loginId}`);
  cy.get("#loginBtn").click();
});

Cypress.Commands.add("loginJson", json => {
  cy.loadWelcome();
  cy.get("#userJsonDisplay").clear().type(json);

  cy.get("#loginBtn").click();
});

Cypress.Commands.add('removeFetch', () => {
  cy.visit('/', {
    onBeforeLoad: win => {
      win.fetch = null;
    }
  });
});
