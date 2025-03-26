describe('Login Tests', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');
    });

    it('Validates login page visibility and admin login/logout process', () => {
        cy.url().should('eq', 'http://localhost:8080/login');
        cy.get('.login-form').should('be.visible');

        cy.get('.login-form').should('exist');
        cy.get('#username').should('exist').type('admin');
        cy.get('#password').should('exist').type('adminPassword');
        cy.get('#btn-login').should('exist').click();

        cy.url().should('eq', 'http://localhost:8080/books_admin');

        cy.get('#logout-btn').should('exist').click();

        cy.url().should('eq', 'http://localhost:8080/login?logout');
    });
});
