# System Zarządzania Biblioteką

Aplikacja webowa do zarządzania biblioteką, umożliwiająca zarządzanie książkami, wypożyczanie i zwracanie oraz logowaniem użytkowników.

## Funkcje

- Logowanie użytkowników.
- Zarządzanie książkami: administrator może dodawać książki, edytować i usuwać.
- Wypożyczanie i zwracanie książek: użytkownik ma opcję wypożyczenia, a następnie oddania książki.
- Przeglądanie dostępności książek: użytkownik ma dostęp do całej listy książek, do dostępnych jak i do tych, które są w tej chwili wypożyczone.
- W momencie utworzenia nowego konta przez użytkownika, adres e-mail jest przesyłany do dedykowanej aplikacji email-receiver za pośrednictwem kolejki RabbitMQ. Proces ten zapewnia efektywne zarządzanie i przetwarzanie wiadomości.
- Aplikacja email-receiver uruchamia się i odbiera z kolejki, a następnie zapisuje je w oddzielnej bazie danych. Dzięki temu system zapewnia niezawodne przechowywanie i dostęp do informacji o utworzonych kontach użytkowników.

## Technologie

- Java
- JavaScript
- HTML5
- CSS3
- Bootstrap
- MySQL
- Docker
- RabbitMQ

## Pobranie projektu
Najpierw musisz uzyskać adres URL repozytorium, które chcesz pobrać. Możesz to zrobić na stronie repozytorium GitHuba, klikając przycisk "Code" (zazwyczaj zielony przycisk) i kopiując adres URL HTTPS.
https://github.com/kacper011/LibraryManagementWithTemplates.git - bezpośredni link do repozytorium.

Otwórz terminal lub wiersz poleceń na swoim komputerze.

Użyj komendy git clone w terminalu, aby sklonować repozytorium z GitHuba. Składnia komendy jest następująca:
git clone https://github.com/kacper011/LibraryManagementWithTemplates.git

Po zakończeniu procesu git clone, możesz przejść do katalogu repozytorium na swoim komputerze i sprawdzić, czy wszystkie pliki zostały pomyślnie pobrane.

Po włączeniu aplikacji, należy przejść do przeglądarki i wpisać adres localhost:8080/books. Od razu przekieruje nas na stronę localhost:8080/login, musimy zarejestrować się, jeśli nie mamy jeszcze konta.
Istnieje już konto z rolą admina: 
Login: admin
Hasło: adminPassword

