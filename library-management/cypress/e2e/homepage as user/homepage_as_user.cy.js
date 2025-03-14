describe('Display Of Book List', () => {
    beforeEach(() => {
        cy.login();
        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Opens the book list page', () => {
        cy.url().should('eq', 'http://localhost:8080/books_user');
        cy.wait(2000);
    });

    it('Checking the visibility of the table', () => {
        cy.get('table.table').should('be.visible');
        cy.wait(2000);

    });

    it('Clicking the view button', () => {
        cy.wait(2000);
        cy.get('a[href="/books/2/view"]').click();

        cy.url().should('eq', 'http://localhost:8080/books/2/view');
    })
});

