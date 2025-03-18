describe('My account', () => {
    beforeEach(() => {
        cy.login();
        cy.url().should('eq', 'http://localhost:8080/books_user');
    });

    it('My account', () => {
        cy.wait(2000);
        cy.get('a.nav-link.active[href="/my_account"]').should('be.visible').click();
        cy.url().should('eq', 'http://localhost:8080/my_account');

        cy.contains('h1', 'My Account').should('be.visible');
        cy.get('.container').should('be.visible');
        cy.get('#username').should('have.value', 'Kacper11');
        cy.get('#email').should('have.value', 'kacper-szabat@wp.pl');
        cy.contains('.input-group-text', 'Show/Hide').should('be.visible');
        cy.contains('.btn.btn-primary', 'Edit Profile').should('be.visible').click();
        cy.get('#saveChanges').should('be.visible');
    })

//    it('Name change', () => {
//
//    })
//
//    it('Email change', () => {
//
//    })
});