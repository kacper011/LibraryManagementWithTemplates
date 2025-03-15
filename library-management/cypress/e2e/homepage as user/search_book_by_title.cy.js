describe('Search a book by title', () => {
    beforeEach(() => {
        cy.login();
        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('Search a book by title', () => {
        cy.get('input.form-control.me-2').should('be.visible').and('have.value', '');

        cy.get('input.form-control.me-2').type('Updated Book Title').should('have.value', 'Updated Book Title');

        cy.get('.btn-outline-success').should('be.visible').click();

        cy.url().should('eq', 'http://localhost:8080/books_user?title=Updated+Book+Title')

        cy.get('h1.text-center span').should('have.text', "Search Results for 'Updated Book Title'");
    })
});