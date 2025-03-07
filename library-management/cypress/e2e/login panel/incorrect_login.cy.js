describe('Login Tests', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');
    });

    it('Opens the login page', () => {
        cy.url().should('eq', 'http://localhost:8080/login');
        cy.get('.login-form').should('be.visible');
    });

    it('Checks incorrect login', () => {
        cy.get('.login-form').should('exist');
        cy.get('#username').should('exist').type('example')
        cy.get('#password').should('exist').type('example')
        cy.get('#btn-login').should('exist').click();

        cy.get('.text-danger').should('be.visible').and('contain.text', 'Invalid login or password.')

        cy.url().should('eq', 'http://localhost:8080/login?error')
    })
});