describe('Login Page', () => {
    beforeEach(() => {
       cy.visit('http://localhost:8080/login');
    })

    describe('Display of the login page', () => {
        it('Opens the login page', () => {
            cy.url().should('include', 'http://localhost:8080/login');
            cy.get('.login-form').should('be.visible');
        })
    })
})