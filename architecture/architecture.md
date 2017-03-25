# Unsere Komponentenbeschreibung 
----------------------------------------------------
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



