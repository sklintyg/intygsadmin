/// <reference types="Cypress" />

context("IntygInfo", () => {

  before(() => {
    cy.server()

    cy.login('TSTNMT2321000156-10KK');
    cy.removeFetch();
    cy.get("#MenuBar-intyginfo").click();
  });

  it("Search intyginfo", () => {

    cy.get('#validationSearchMessageId').should('be.hidden')
    cy.get("#searchBtn").click();
    cy.get('#validationSearchMessageId').should('be.visible')
    cy.get('#intygInfoDialogId').should('not.exist')

    cy.get('#searchInput').type('f63c813d-a13a-4b4b-965f-419dfe98fffe')
    cy.get("#searchBtn").click();
    cy.get('#validationSearchMessageId').should('be.hidden')
    cy.get('#intygInfoDialogId').should('be.visible')
    cy.get("#closeBtn").click();

    cy.get('#intygInfoDialogId').should('not.exist')

  })

  it("Search history", () => {

    cy.get('#intygInfoHistoryTable').should('be.visible')

  })

});
