describe('Login Page', () => {
    beforeEach(() => {
       cy.visit('http://localhost:8080/login');
    });

    it('Validates login page visibility and form header content', () => {
        cy.url().should('eq', 'http://localhost:8080/login');
        cy.get('.login-form').should('be.visible');

        cy.get('#form-header').should('exist');
        cy.get('#form-header').should('have.text', 'Library Login');
    });
});
