AUTH MIKROSERVIS (Auth Service)

1. Tehničke specifikacije i opis poslovne logike

Auth mikroservis služi kao centralni servis za identifikaciju korisnika unutar sistema.
Korisnik se identifikuje preko X-API-KEY zaglavlja. Servis proverava da li korisnik postoji
i vraća podatke o korisniku i njegovim ulogama.

Servis koristi H2 bazu i JPA mapiranje za User i Role entitete, sa many-to-many relacijom.
API ključ je jedinstven za svakog korisnika i povezan je sa njegovim ulogama.

Tehnološki stek:
Framework: Spring Boot (Java)
Baza podataka: H2 (In-Memory)
ORM: Spring Data JPA / Hibernate
Validacija i obrada grešaka: Spring Web + GlobalExceptionHandler
Monitoring: Spring Boot Actuator
Dokumentacija API-ja: Swagger / OpenAPI 3

2. Mrežni port

Port: 8085

3. Prikaz implementiranih endpointa

AUTH

GET /api/auth/validate
Validira X-API-KEY i vraća podatke o korisniku i njegove uloge.

4. Instrukcije za lokalno pokretanje i testiranje

Preduslovi:
Java 17 ili novija
Maven

Pokretanje:
Prvo pokrenuti Config Service, pa zatim Auth Service.

mvn spring-boot:run

5. H2 baza podataka

H2 konzola:
http://localhost:8085/h2-console

JDBC URL:
jdbc:h2:mem:authdb

Username:
kimi

Password:
(prazno)

6. Swagger dokumentacija

http://localhost:8085/swagger-ui.html

7. Monitoring

Health check:
http://localhost:8085/actuator/health
