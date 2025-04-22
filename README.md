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
- Cypress
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
Step 1: Copy the URL of the repository: <br>
Go to the GitHub page of the project and copy the HTTPS link: <br>
https://github.com/kacper011/LibraryManagementWithTemplates.git <br> <br>

Step 2: Open a terminal or command line <br>
Windows: use CMD, PowerShell, or terminal inside VS Code <br>
macOS/Linux: open the built-in Terminal <br> <br>

Step 3: Clone the repository <br>
Paste this command in the terminal and confirm with Enter: <br>
``git clone https://github.com/kacper011/LibraryManagementWithTemplates.git`` <br>
This will download the entire project folder to your local machine. <br> <br>

Step 4: Navigate to the project directory <br>
Once cloning is complete, go to the folder: <br>
cd LibraryManagementWithTemplates <br>
Now you are inside the project directory and ready to build or run the app. <br> <br>

## Database Setup Instructions (MySQL)

1. Install and Start MySQL
If you don’t have MySQL installed:

Windows: Use MySQL Installer

Linux: <br>
``sudo apt install mysql-server`` <br>
``sudo service mysql start`` <br>

macOS: <br>
``brew install mysql`` <br>
``brew services start mysql`` <br>

2. Create the Database and User <br>
Log into MySQL: <br> 
``mysql -u root -p`` <br>

Then run the following SQL commands: <br>
``CREATE DATABASE library2;`` <br>

``CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'haslo123';`` <br>

``GRANT ALL PRIVILEGES ON library2.* TO 'library_user'@'localhost';`` <br>

``FLUSH PRIVILEGES;`` <br>

3. Set Environment Variables <br>
The application reads the database username and password from environment variables. <br>

On Linux/macOS: <br>
``export DB_USERNAME=library_user`` <br>
``export DB_PASSWORD=haslo123`` <br>

On Windows (CMD): <br>
``set DB_USERNAME=library_user`` <br>
``set DB_PASSWORD=haslo123`` <br>

Make sure MySQL is running before starting the app. <br>




