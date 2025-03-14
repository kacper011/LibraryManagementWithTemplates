Cypress.Commands.add('login', () => {
    cy.visit('http://localhost:8080/login');

    cy.get('.login-form').should('exist');
    cy.get('#username').type('Kacper11');
    cy.get('#password').type('kacper11');
    cy.get('#btn-login').click();

    cy.url().should('eq', 'http://localhost:8080/books_user');
});