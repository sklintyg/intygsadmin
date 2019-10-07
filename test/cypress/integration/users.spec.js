/// <reference types="Cypress" />

context("Users", () => {

  beforeEach(() => {
    cy.server()
    cy.route({
      method: 'GET',
      url: '**/api/user**'
    }).as('apiCheck');
  });

  it("Login as basic user, menu not visible", () => {
    cy.login('TSTNMT2321000156-10KL');
    cy.get("#MenuBar-administratorer").should('not.exist');

    cy.logout();
  })

  it("Create/Update/delete user", () => {
    cy.login('TSTNMT2321000156-10KK');
    cy.get("#MenuBar-administratorer").click();
    cy.wait('@apiCheck');

    cy.get("#addUserBtn").click();
    cy.get('#saveUser').should('be.disabled')

    cy.get('#userName').type('Test Name');
    cy.get('#userHsaId').type(randomString(10));
    cy.get('#roleBAS').click();

    cy.get('#saveUser').click();

    cy.wait('@apiCheck');
    cy.wait(500);

    cy.get('#usersTable tbody tr:first').within(() => {
      cy.get('.user-name').should('have.text', 'Test Name')
    });

    // Change user
    cy.get('#usersTable tbody tr:first').within(() => {
      cy.get('button.change-btn').click() // Change banner
    });

    cy.get('#saveUser').should('be.disabled')

    cy.get('#roleFULL').click();

    cy.get('#saveUser').click();

    cy.wait('@apiCheck');
    cy.wait(500);

    // user banner
    cy.get('#usersTable tbody tr:first').within(() => {
      cy.get('button.end-btn').click() // Remove user
    });

    cy.get('#confirmBtn').click();

    cy.wait('@apiCheck');
    cy.wait(500);

    cy.get('#usersTable tbody tr:first').within(() => {
      cy.get('.user-name').should('not.have.text', 'Test Name')
    })
  })

});

function randomString(string_length) {
  let random_string = '';
  let random_ascii;
  for(let i = 0; i < string_length; i++) {
    random_ascii = Math.floor((Math.random() * 25) + 97);
    random_string += String.fromCharCode(random_ascii)
  }
  return random_string
}
