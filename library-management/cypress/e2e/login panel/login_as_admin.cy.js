describe('Login Tests', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');
    });

    it('Opens the login page', () => {
        cy.url().should('eq', 'http://localhost:8080/login');
        cy.get('.login-form').should('be.visible');
    });

    it('Validates login as admin', () => {
        cy.get('.login-form').should('exist');
        cy.get('#username').should('exist').type('admin');
        cy.get('#password').should('exist').type('adminPassword');
        cy.get('#btn-login').should('exist').click();

        cy.url().should('eq', 'http://localhost:8080/books_admin');

        cy.get('#logout-btn').should('exist').click();

        cy.url().should('eq', 'http://localhost:8080/login?logout');
    });
});