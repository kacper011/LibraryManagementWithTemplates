describe('Login and Registration Pages', () => {
    it('Opens the login page', () => {
        cy.visit('http://localhost:8080/login');
        cy.url().should('eq', 'http://localhost:8080/login');
    });

    it('Navigates to the registration page and performs a mock registration', () => {
        cy.visit('http://localhost:8080/login');
        cy.get('#btn-registration').click();
        cy.url().should('eq', 'http://localhost:8080/registration');

        cy.intercept('POST', '/registration', {
            statusCode: 200,
            body: { success: true, userId: 12345 }
        }).as('mockRegister');

        cy.get('#name').should('exist').type('Tomasz');
        cy.get('#email').should('exist').type('tomasz@gmail.com');
        cy.get('#password').should('exist').type('tomasz123');
        cy.get('#btn-register').click();

        // Waiting for an intercepted request
        cy.wait('@mockRegister').its('response.statusCode').should('eq', 200);
    });
});
