describe('Rent a book', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');

        cy.get('.login-form').should('exist');
        cy.get('#username').type('Kacper11');
        cy.get('#password').type('kacper11');
        cy.get('#btn-login').click();

        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Rent a book', () => {
        cy.wait(2000);
        cy.get('a[href="/books/1/rent"]').click();
        cy.url().should('eq', 'http://localhost:8080/my_books');

        cy.get('a[href="/books_user"]').click();

        cy.get('a[href="/books/1/rent"]').should('have.class', 'btn-disabled');
    })
});