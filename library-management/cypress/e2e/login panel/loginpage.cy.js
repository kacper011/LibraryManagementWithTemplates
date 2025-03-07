describe('Login Page', () => {
    beforeEach(() => {
       cy.visit('http://localhost:8080/login');
    })

    describe('Display of the login page', () => {
        it('Opens the login page', () => {
            cy.url().should('eq', 'http://localhost:8080/login');
            cy.get('.login-form').should('be.visible');
        })
    })

    describe('Form header test', () => {
        it('Checks the content of the form header', () => {
            cy.get('#form-header').should('exist');
            cy.get('#form-header').should('have.text', 'Library Login')
        })
    })
})