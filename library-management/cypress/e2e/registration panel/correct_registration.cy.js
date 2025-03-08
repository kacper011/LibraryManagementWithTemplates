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

    it('Registration of a new account without saving to the database', () => {
        cy.intercept('POST', '/registration', {
            statusCode: 200,
            body: { success: true, userId: 12345 }
        }).as('mockRegister');

        cy.visit('http://localhost:8080/registration');
        cy.get('#name').should('exist').type('Tomasz');
        cy.get('#email').should('exist').type('tomasz@gmail.com');
        cy.get('#password').should('exist').type('tomasz123');

        cy.get('#btn-register').click();

        // Waiting for an intercepted request
        cy.wait('@mockRegister').its('response.statusCode').should('eq', 200);
    });

});
