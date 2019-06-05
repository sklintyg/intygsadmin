/// <reference types="Cypress" />

context('Login', () => {
  it('login and verify name', () => {
    cy.login('TSTNMT2321000156-10KK');

    cy.get('#currentUserTitle').should('contain.text', 'Karl Nilsson');

    cy.get('#logoutBtn').click();

    cy.url().should('include', 'welcome.html')
  });
});
