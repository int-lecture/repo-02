# Anforderungen
## Senden einer Nachricht
### Intialisierung (Client)
1. Text der Nachricht
2. Empfänger (Profilreferenz)
3. Absender (Profilreferenz)
4. Datum
5. (Bild-URL)

### Antwort (Server)
1. Empfangsbestätigung
2. (Empfangsdatum)
3. (Nummer der empfangenen Nachricht)

## Empfangen von Nachrichten
### Abfrage (Client)
1. eigene Profilreferenz (+ Login)
2. Nummer letzter empfangener Nachricht

### Antwort (Server)
Nachrichten-Array mit Infos über:
  1. Text der Nachricht
  2. Absender (Profilreferenz)
  3. Datum + Uhrzeit
  4. Nummer der Nachricht
  5. (Bild-URL)
