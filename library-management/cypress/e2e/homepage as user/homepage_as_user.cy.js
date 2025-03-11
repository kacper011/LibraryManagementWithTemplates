describe('Display Of Book List', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8080/login');

        cy.get('.login-form').should('exist');
        cy.get('#username').type('Kacper11');
        cy.get('#password').type('kacper11');
        cy.get('#btn-login').click();

        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Opens the book list page', () => {
        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Checking the visibility of the table', () => {
        cy.get('table.table').should('be.visible');
    });

    it('Clicking the view button', () => {
        cy.get('a[href="/books/2/view"]').click();

        cy.url().should('eq', 'http://localhost:8080/books/2/view');
    })
});

