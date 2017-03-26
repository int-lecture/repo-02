# Unsere Komponentenbeschreibung 

## Webserver
- erhält von Webbrowsern anfragen und stellt unseren Messenger zur Verfügung
## Authentifizierung
- Der Authentifizierungsserver verwaltet die Anmeldung 
- Daten: er verwaltet die Passwörter der User.
## Verschlüsselung
- Alle Daten die versendet werden, werden beim Absender verschlüsselt und beim Empfänger entschlüsselt (Algorithmus)
## Nachrichten-Verteiler
- Versendet und empfängt alle Nachrichten, verwaltet den Chatverlauf.
- Daten: Arbeitet in unserem Beispiel mit dem Chatverläufen.
## Load-Balancer (Verbindungen)
- Stellt Verbindungen so her, dass die Ressourcen am Besten genutzt werden.
- Daten: hat (vermutlich) Listen von Webservern.
## Account-Manager
- verwaltet alle Daten der User, kümmert sich um Änderungen im Profil etc.
- Daten: In unserem Beispiel arbeitet er mit den Profilen der Usern.

# Skalierbarkeit der Komponenten
Da die folgenden Punkte aus Überlegungen entstanden sind, sind diese Annahmen.
## Komponenten, die eventuell erweitert werden können
- Webserver --> Da der Load-Balancer vermutlich eine Liste verfügbarer Web-Server besitzt, wird diese Arbeit parallelisiert.
- Account-Manager --> Muss parallelisierbar bleiben, damit bei mehr Anfragen, die Applikation nicht langsamer wird.
- Nachrichten-Verteiler --> Da die meisten Daten (vermutlich) hier lagern und der meiste Input/Output stattfindet. 
- Authentifizierung --> die Anfragen bei der Anmeldung zu parallelisieren, ist wohl mit etwas mehr Aufwand zu bewältigen.

## Komponenten, die eventuell nicht erweitert werden können
- Verschlüsselung --> allerdings wünschenswert, da jede Nachricht durch die **Verschlüsselung und Entschlüsselung** muss.
- Verbindungen --> es ist nicht wünschenswert mehrere **Load-Balancer** zu haben, da sonst die Zuordnung der Webserver nicht korrekt verlaufen könnte




