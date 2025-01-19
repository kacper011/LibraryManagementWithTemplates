<h1 align="center">Library Management System</h1>

A web application for managing a library, allowing users to manage books, borrow and return them, and handle user login.
## Features

- User Login: Allows users to log in to the system.
- Book Management: Administrators can add, edit, and delete books.
- Borrowing and Returning Books: Users can borrow and then return books.
- Viewing Book Availability: Users have access to the entire list of books, including those currently borrowed and available.
- New Account Creation: When a new account is created, the user's email address is sent to a dedicated email-receiver application via a RabbitMQ queue. This process ensures efficient management and processing of messages.
- Email-Receiver Application: This application runs and retrieves messages from the queue, then saves them in a separate database. This ensures reliable storage and access to information about newly created user accounts. The application sends a welcome e-mail to the user's e-mail address.
- New Book Notification: When an administrator adds a new book, a "New Arrival" status is displayed next to the title for 7 days from the date of addition.
- Automated Testing: Selenium is used to automate UI tests for key functionalities such as user login, book borrowing, and returning.

## Technologies

- Java
- JavaScript
- HTML5
- CSS3
- Bootstrap
- MySQL
- Docker
- RabbitMQ
- Selenium (for automated UI testing)
<br><br><br><br>

<h1 align="center">Application overview photos</h1>
<br><br><br>

Login panel

<img src="images/login panel.png" alt="login panel">

<br><br><br>

Registration panel

<img src="images/new user registration.png" alt="new user registration">

<br><br><br>

View from the admin account

<img src="images/view from the admin account.png" alt="view from the admin account">

<br><br><br>

Adding a book as admin

<img src="images/adding a book as admin.png" alt="adding a book as admin">

<br><br><br>

Display of user accounts as admin

<img src="images/display of user accounts.png" alt="display of user accounts">

<br><br><br>

Newly added book has a novelty status

<img src="images/newly added book has a novelty status.png" alt="newly added book has a novelty status">

<br><br><br>

View from the user account

<img src="images/view from the user account.png" alt="view from the user account">

<br><br><br>

User account information

<img src="images/user account information.png" alt="user account information">

<br><br><br>

Book details

<img src="images/book details.png" alt="book details">

<br><br><br>

Books borrowed by the user

<img src="images/books borrowed by the user.png" alt="books borrowed by the user">

<br><br><br>




<br><br><br><br>

## Project Download
First, obtain the URL of the repository you want to download. You can do this by visiting the GitHub repository page, clicking the "Code" button (usually a green button), and copying the HTTPS URL. Here's the direct link to the repository: https://github.com/kacper011/LibraryManagementWithTemplates.git.

Open your terminal or command prompt on your computer.

Use the git clone command in the terminal to clone the repository from GitHub. The command syntax is as follows:
git clone https://github.com/kacper011/LibraryManagementWithTemplates.git

After the git clone process is completed, you can navigate to the repository directory on your computer and verify that all files have been successfully downloaded.

Once the application is launched, go to your browser and enter the address localhost:8080/books. You will be immediately redirected to localhost:8080/login. You must register if you don't already have an account.

