GATEWAY MIKROSERVIS (Gateway Service)

1. Tehničke specifikacije i opis poslovne logike

Gateway mikroservis predstavlja centralnu ulaznu tačku sistema.
Zadužen je za rutiranje zahteva ka ostalim mikroservisima, validaciju korisnika preko
Auth servisa, kontrolu pristupa rutama na osnovu uloga, dodelu Trace ID vrednosti,
dodavanje zaglavlja sa vremenom obrade i zaštitu kritičnih endpointa putem rate limiting mehanizma.

Gateway koristi WebClient za komunikaciju sa Auth servisom i globalne filtere za:
- autentifikaciju i autorizaciju
- Trace ID propagaciju
- rate limiting
- merenje vremena obrade zahteva

Tehnološki stek:
Framework: Spring Boot (Java)
Gateway: Spring Cloud Gateway
Reaktivna komunikacija: Spring WebFlux / WebClient
Centralizovana konfiguracija: Spring Cloud Config Client
Otpornost: Resilience4j dependency
Monitoring: Spring Boot Actuator

2. Mrežni port

Port: 8084

3. Prikaz implementiranih ruta i funkcionalnosti

Rutiranje prema servisima:

/api/guides/**
/api/languages/**
/api/guide-languages/**
/api/certifications/**
-> Guide Service

/api/adventures/**
/api/reviews/**
/api/categories/**
/api/gear/**
/api/time-slots/**
-> Adventure Service

/api/bookings/**
/api/addon-items/**
/api/booking-status-logs/**
/api/cancellation-policies/**
/api/guests/**
-> Rezervacije Service

/api/invoices/**
/api/payment-methods/**
/api/taxrates/**
/api/transactions/**
-> Finansije Service

/api/auth/**
-> Auth Service

Implementirane gateway funkcionalnosti:
- validacija X-API-KEY preko Auth servisa
- prosleđivanje X-Authenticated-User i X-User-Role zaglavlja
- generisanje i propagacija X-Trace-Id zaglavlja
- dodavanje X-Processing-Time-ms zaglavlja
- in-memory rate limiting za kritične POST zahteve

4. Instrukcije za lokalno pokretanje i testiranje

Preduslovi:
Java 17 ili novija
Maven

Pokretanje:
Prvo pokrenuti Config Service, zatim Auth Service i ostale mikroservise, pa onda Gateway.

mvn spring-boot:run

5. Health i monitoring

Health check:
http://localhost:8084/actuator/health

Gateway actuator:
http://localhost:8084/actuator/gateway
