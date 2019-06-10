/// <reference types="Cypress" />
const format = require('date-fns/format')
const addYears = require('date-fns/addYears')

context("Banners", () => {

  beforeEach(() => {
    cy.server()
    cy.route({
      method: 'GET',
      url: '**/api/banner**'
    }).as('apiCheck');


    cy.login('TSTNMT2321000156-10KK');
  });

  it("Create banner", () => {
    cy.removeFetch();
    cy.wait('@apiCheck');

    const date = addYears(new Date(), 3)

    const fromDate = format(date, 'yyyy-MM-dd')
    const toDate = format(date, 'yyyy-MM-dd')

    cy.get("#addBannerBtn").click();

    cy.get('#tjanstRehabstod').click();
    cy.get('#bannerMessage').type('test message');
    cy.get('#displayFromDate').type(fromDate);
    cy.get('#displayFromTime').type('10:00');
    cy.get('#displayToDate').type(toDate);
    cy.get('#displayToTime').type('23:00');
    cy.get('#prioHIGH').click();

    cy.get('#saveBanner').click();

    cy.wait('@apiCheck');
    cy.wait(500);

    cy.get('#BannerListTable tr:first').within(() => {
      cy.get('.banner-message').should('have.text', 'test message')

      cy.get('button.end-btn').click() // Remove banner
    });

    cy.get('#confirmBtn').click();

    cy.wait('@apiCheck');
    cy.wait(500);

    cy.get('#BannerListTable tr:first').within(() => {
      cy.get('.banner-message').should('not.have.text', 'test message')
    })
  })


});
