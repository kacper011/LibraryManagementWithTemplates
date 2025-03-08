describe('Login Page', () => {
    it('Opens the login page', () => {
        cy.visit('http://localhost:8080/login');
        cy.url().should('eq', 'http://localhost:8080/login');
    });
});

describe('Registration Page', () => {
    before(() => {
        cy.visit('http://localhost:8080/login');
    });

    it('Clicks on the registration button', () => {
        cy.get('#btn-registration').click();
        cy.url().should('eq', 'http://localhost:8080/registration');
    });

    it('Email validation during registration', () => {
        cy.intercept('POST', '/registration', {
            statusCode: 200,
            body: { success: true, userId: 12345 }
        }).as('mockRegister');

        cy.visit('http://localhost:8080/registration');
        cy.get('#name').should('exist').type('Tomasz');
        cy.get('#email').should('exist').type('tomaszgmail.com');
        cy.get('#password').should('exist').type('tomasz123');

        cy.get('#btn-register').click();

        cy.url().should('eq', 'http://localhost:8080/registration');

        cy.get('#email').should('have.attr', 'title', 'Please enter a valid email address');
    });

});