describe('Display Of Book List', () => {
    beforeEach(() => {
        cy.login();
        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Checks book list page, table visibility, and view button', () => {
        cy.url().should('eq', 'http://localhost:8080/books_user');
        cy.wait(2000);

        cy.get('table.table').should('be.visible');
        cy.wait(2000);

        cy.get('a[href="/books/2/view"]').click();
        cy.url().should('eq', 'http://localhost:8080/books/2/view');
    });
});

