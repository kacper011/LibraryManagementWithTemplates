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

    describe('Correct login', () => {
        it('Validates login as admin', () => {
            cy.get('.login-form').should('exist');
            cy.get('#username').should('exist').type('admin')
            cy.get('#password').should('exist').type('adminPassword')
            cy.get('#btn-login').should('exist').click();

            cy.url().should('eq', 'http://localhost:8080/books_admin')

            cy.get('#logout-btn').should('exist').click();

            cy.url().should('eq', 'http://localhost:8080/login?logout')
        })

        it('Validates login as user', () => {
            cy.get('.login-form').should('exist');
            cy.get('#username').should('exist').type('Kacper11')
            cy.get('#password').should('exist').type('kacper11')
            cy.get('#btn-login').should('exist').click();

            cy.url().should('eq', 'http://localhost:8080/books_user')

            cy.get('#logout-btn').should('exist').click();

            cy.url().should('eq', 'http://localhost:8080/login?logout')
        })
    })

    describe('Incorrect login', () => {
        it('Checks incorrect login', () => {
            cy.get('.login-form').should('exist');
            cy.get('#username').should('exist').type('example')
            cy.get('#password').should('exist').type('example')
            cy.get('#btn-login').should('exist').click();

            cy.get('.text-danger').should('be.visible').and('contain.text', 'Invalid login or password.')

            cy.url().should('eq', 'http://localhost:8080/login?error')
        })
    })
})