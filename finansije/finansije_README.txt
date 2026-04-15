
FINANSIJE MIKROSERVIS (Finansije Service)

1. Tehničke specifikacije i opis poslovne logike

Finansije mikroservis je odgovoran za upravljanje finansijskim aspektom sistema za ekološki turizam.
Servis upravlja izdavanjem faktura, poreskim stopama, metodama plaćanja i evidencijom finansijskih transakcija.
Takođe omogućava komunikaciju sa Rezervacije mikroservisom kako bi proverio postojanje rezervacije
i ažurirao njen status nakon uspešnog plaćanja.

Tehnološki stek:
Framework: Spring Boot (Java)
Baza podataka: H2 (In-Memory relacionalna baza)
ORM: Spring Data JPA / Hibernate
Sinhrona komunikacija: Spring Cloud OpenFeign
Otpornost sistema: Resilience4j (Circuit Breaker + Retry)
Monitoring: Spring Boot Actuator
Dokumentacija API-ja: Swagger / OpenAPI 3


2. Mrežni port

Port: 8083


3. Implementirani endpointi

INVOICES (Fakture)

POST   /api/finansije/invoices/add
Kreira novu fakturu za određenu rezervaciju

PUT    /api/finansije/invoices/update/{id}
Ažurira postojeću fakturu

GET    /api/finansije/invoices/{id}
Dohvata fakturu na osnovu ID-a

GET    /api/finansije/invoices/all
Prikazuje sve fakture

DELETE /api/finansije/invoices/delete/{id}
Briše fakturu iz sistema


TRANSACTIONS (Transakcije)

POST   /api/finansije/transactions/add
Kreira novu transakciju

POST   /api/finansije/transactions/pay
Beleži uplatu za fakturu i ažurira status rezervacije

GET    /api/finansije/transactions/{id}
Dohvata jednu transakciju

GET    /api/finansije/transactions/all
Prikazuje sve transakcije

PUT    /api/finansije/transactions/update/{id}
Ažurira transakciju

DELETE /api/finansije/transactions/delete/{id}
Briše transakciju

GET    /api/finansije/transactions/search
Pretraga transakcija po vremenskom opsegu i metodi plaćanja


PAYMENT METHODS (Metode plaćanja)

POST   /api/finansije/paymentmethods/add
Dodaje novu metodu plaćanja

PUT    /api/finansije/paymentmethods/update/{id}
Ažurira postojeću metodu

GET    /api/finansije/paymentmethods/{id}
Dohvata metodu plaćanja

GET    /api/finansije/paymentmethods/all
Prikazuje sve metode

DELETE /api/finansije/paymentmethods/delete/{id}
Briše metodu plaćanja


TAX RATES (Poreske stope)

POST   /api/finansije/taxrates/add
Dodaje novu poresku stopu

PUT    /api/finansije/taxrates/update/{id}
Ažurira poresku stopu

GET    /api/finansije/taxrates/{id}
Dohvata poresku stopu

GET    /api/finansije/taxrates/all
Prikazuje sve poreske stope

DELETE /api/finansije/taxrates/delete/{id}
Briše poresku stopu


4. Instrukcije za pokretanje

Preduslovi:
Java 17 ili novija
Maven

Pokretanje aplikacije:

mvn spring-boot:run

Prvo je potrebno pokrenuti Config Service.


5. H2 baza podataka

H2 konzola:
http://localhost:8083/h2-console

JDBC URL:
jdbc:h2:mem:finansijedb

Username:
asdf

Password:
(prazno)


6. Swagger dokumentacija

http://localhost:8083/swagger-ui.html


7. Monitoring sistema

Health check:
http://localhost:8083/actuator/health

Circuit breaker status:
http://localhost:8083/actuator/circuitbreakers
