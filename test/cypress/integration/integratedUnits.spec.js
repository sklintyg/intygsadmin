/// <reference types="Cypress" />

context("IntegratedUnits", () => {

  beforeEach(() => {
    cy.server()

    cy.login('TSTNMT2321000156-10KK');
    cy.get("#MenuBar-integratedUnits").click();
  });

  it("Search integrated unit", () => {

    cy.get('#validationSearchMessageId').should('be.hidden')
    cy.get("#searchBtn").click();
    cy.get('#validationSearchMessageId').should('be.visible')
    cy.get('#integratedUnitSearchResultId').should('not.exist')

    cy.get('#searchInput').type('SE4815162344-1A01')
    cy.get("#searchBtn").click();
    cy.get('#validationSearchMessageId').should('be.hidden')
    cy.get('#integratedUnitSearchResultId').should('be.visible')
    cy.get(".close").click();

  })

  xit("Export integrated units", () => {

    cy.route({
      method: 'GET',
      url: '**/api/integratedUnits/file**'
    }).as('apiCheck');

    cy.get("#exportBtn").click();
    cy.wait('@apiCheck');
    cy.get('#validationExportMessageId').should('be.hidden')

  })

});
