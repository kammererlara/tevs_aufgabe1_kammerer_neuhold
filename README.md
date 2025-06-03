# Status-Server – Verteiltes System zur Statusverwaltung
Dieses Projekt implementiert ein verteiltes Status-Server-System mit mehreren gleichwertigen Backend-Instanzen, einem API-Gateway mit Load Balancer, einer zentralen Service Registry und einem Message-Broker. Ziel des Systems ist es, Statusmeldungen zuverlässig und skalierbar entgegenzunehmen, zu verarbeiten und zu replizieren.

## Architekturüberblick
### Komponenten
+ Frontend (Status-Client)
  Implementiert mit HTML und JavaScript. Kommuniziert ausschließlich über eine REST-API mit dem API-Gateway. Ermöglicht das Setzen, Abrufen, Aktualisieren und Löschen von Statusmeldungen.
+ API Gateway mit Load Balancer
  Dient als zentraler Einstiegspunkt für alle Clientanfragen. Der integrierte Load Balancer verteilt die eingehenden REST-API-Aufrufe gleichmäßig auf die verfügbaren Backend-Instanzen.
  Leitet außerdem Serviceinformationen von der Service Registry weiter.
+ Backend – Status-Server-Nodes (Java)
  Mehrere gleichwertige Backend-Instanzen, die Statusmeldungen entgegennehmen und lokal speichern.
  Zur Gewährleistung von Konsistenz und Replikation kommunizieren sie über RabbitMQ miteinander.
  Jeder Node kann unabhängig arbeiten und ist in der Lage, Anfragen vollständig zu verarbeiten.
+ Service Registry (Java)
  Zentrale Registrierungskomponente (Spring Cloud Netflix Eureka), bei der sich alle Backend-Instanzen zur Laufzeit registrieren.
  Ermöglicht dem Gateway, dynamisch verfügbare Services zu entdecken.
+ RabbitMQ
  Message-Broker für die synchrone oder asynchrone Kommunikation und Replikation zwischen den Backend-Nodes.
  Sorgt für Fehlertoleranz und Konsistenz des Systems durch Event-basierte Kommunikation.

## Merkmale
+ Horizontale Skalierbarkeit: Neue Backend-Nodes können einfach hinzugefügt werden.
+ Fehlertoleranz: Fällt ein Node aus, übernimmt ein anderer.
+ Konsistenz durch Messaging: Replikation über RabbitMQ gewährleistet synchronen Status aller Nodes.
+ Service Discovery: Automatische Einbindung neuer Nodes über die Service Registry.
+ Trennung von Zuständigkeiten: Frontend, API-Gateway, Backend und Messaging sind klar voneinander getrennt.

## Technologiestack
+ Java (Spring Boot) – Backend, Gateway, Service Registry
+ RabbitMQ – Messaging-Plattform
+ HTML / JavaScript – Frontend (Status-Client)
+ REST API – Schnittstelle zwischen Frontend und Gateway
+ Eureka – Service Registry
