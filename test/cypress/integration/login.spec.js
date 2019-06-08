/// <reference types="Cypress" />

context('Login', () => {
  it('login and verify name', () => {
    cy.login('TSTNMT2321000156-10KK');

    cy.get('#currentUserTitle').should('contain.text', 'Karl Nilsson');

    cy.get('#logoutBtn').click();

    cy.url().should('include', 'welcome.html')
  });

  it('Saknar behörighet', () => {
    cy.loginJson('{}');

    cy.get('#indexTitle').should('contain.text', 'Inloggningen misslyckades');
    cy.get('#indexAlertMessage').should('contain.text', 'Du saknar behörighet för att logga in.');

  });

  it('Fel vid inlogging', () => {
    cy.loginJson('-');

    cy.get('#indexTitle').should('contain.text', 'Inloggningen misslyckades');
    cy.get('#indexAlertMessage').should('contain.text', 'Autentiseringen misslyckades.');
  });
});
