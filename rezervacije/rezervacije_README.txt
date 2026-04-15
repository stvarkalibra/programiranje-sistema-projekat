
REZERVACIJE MIKROSERVIS (Rezervacije Service)

1. Tehničke specifikacije i opis poslovne logike

Rezervacije mikroservis upravlja procesom rezervacije smeštajnih jedinica.
Servis upravlja rezervacijama, gostima, dodatnim uslugama i politikama otkazivanja.

Servis takođe omogućava praćenje promena statusa rezervacije
i izračunavanje refundacije prilikom otkazivanja rezervacije.

Tehnološki stek:

Framework: Spring Boot (Java)
Baza podataka: H2 (In-Memory relacionalna baza)
ORM: Spring Data JPA / Hibernate
Arhitekturni paterni: Factory Method i Strategy Pattern
Dokumentacija API-ja: Swagger / OpenAPI 3


2. Mrežni port

Port: 8082


3. Implementirani endpointi

BOOKINGS (Rezervacije)

POST   /api/rezervacije/bookings/add
Kreira novu rezervaciju

PUT    /api/rezervacije/bookings/update/{id}
Ažurira postojeću rezervaciju

POST   /api/rezervacije/bookings/{id}/cancel
Otkazuje rezervaciju i računa refund

PUT    /api/rezervacije/bookings/{id}/status
Menja status rezervacije

PUT    /api/rezervacije/bookings/{id}/cancel-unpaid
Otkazuje rezervaciju zbog neplaćanja

GET    /api/rezervacije/bookings/{id}
Dohvata rezervaciju

GET    /api/rezervacije/bookings/all
Prikazuje sve rezervacije

GET    /api/rezervacije/bookings/search
Pretraga rezervacija po gostu ili statusu

DELETE /api/rezervacije/bookings/delete/{id}
Briše rezervaciju


GUESTS (Gosti)

POST   /api/rezervacije/guests/add
Dodaje novog gosta

PUT    /api/rezervacije/guests/update/{id}
Ažurira podatke gosta

GET    /api/rezervacije/guests/{id}
Dohvata gosta

GET    /api/rezervacije/guests/all
Prikazuje sve goste

DELETE /api/rezervacije/guests/delete/{id}
Briše gosta


CANCELLATION POLICIES (Politike otkazivanja)

POST   /api/rezervacije/cancellationpolicies/add
Dodaje novu politiku otkazivanja

PUT    /api/rezervacije/cancellationpolicies/update/{id}
Ažurira politiku

GET    /api/rezervacije/cancellationpolicies/{id}
Dohvata politiku

GET    /api/rezervacije/cancellationpolicies/all
Prikazuje sve politike

DELETE /api/rezervacije/cancellationpolicies/delete/{id}
Briše politiku


4. Instrukcije za pokretanje

Preduslovi:
Java 17 ili novija
Maven

Pokretanje aplikacije:

mvn spring-boot:run

Pre pokretanja potrebno je pokrenuti Config Service.


5. H2 baza podataka

H2 konzola:
http://localhost:8082/h2-console

JDBC URL:
jdbc:h2:mem:rezervacijedb

Username:
asdf

Password:
(prazno)


6. Swagger dokumentacija

http://localhost:8082/swagger-ui.html
