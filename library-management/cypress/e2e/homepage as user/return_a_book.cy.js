describe('Return a book', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');

        cy.get('.login-form').should('exist');
        cy.get('#username').type('Kacper11');
        cy.get('#password').type('kacper11');
        cy.get('#btn-login').click();

        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Return a book', () => {
        cy.wait(2000);
        cy.get('a.nav-link.active[href="/my_books"]').should('be.visible').click();

        cy.get('form[action="/books/1/return"] button[type="submit"]').should('be.visible').click();
        cy.url().should('eq', 'http://localhost:8080/my_books');

        cy.get('a[href="/books_user"]').click();

        cy.get('a.btn.btn-primary.btn-sm[href="/books/1/rent"]').should('be.visible');
    })
});