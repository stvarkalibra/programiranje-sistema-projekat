CONFIG MIKROSERVIS (Config Service)

1. Tehničke specifikacije i opis poslovne logike

Config mikroservis služi za centralizovano upravljanje konfiguracijom svih mikroservisa u sistemu.
Konfiguracije se čuvaju u internom config-repo direktorijumu i učitavaju se preko Spring Cloud Config Server mehanizma.

Servis obezbeđuje centralizovano čuvanje:
- portova servisa
- konekcija ka bazama
- resilience i retry parametara
- gateway ruta i rate limit podešavanja
- logging i actuator konfiguracije

Tehnološki stek:
Framework: Spring Boot (Java)
Config Server: Spring Cloud Config Server
Monitoring: Spring Boot Actuator

2. Mrežni port

Port: 8086

3. Konfiguracije koje servis obezbeđuje

Config Service sadrži konfiguracije za:
- auth-service
- guide-service
- avantura-service
- rezervacije
- finansije
- gateway

4. Instrukcije za lokalno pokretanje i testiranje

Preduslovi:
Java 17 ili novija
Maven

Pokretanje:
Config Service mora biti pokrenut pre svih ostalih mikroservisa.

mvn spring-boot:run

5. Provera rada servisa

Glavni config endpoint primer:
http://localhost:8086/gateway/default

Health check:
http://localhost:8086/actuator/health

Refresh i env endpointi:
http://localhost:8086/actuator/refresh
http://localhost:8086/actuator/env
