# Notizen 
- **Signieren** einer Nachricht bedeutet, dass eine Checksumme generiert wird und diese wird beim Empfänger entschlüsselt (wenn Checksumme passt, dann wurde Nachricht nicht verändert)
- **Challenge Response Anfrage** Beispiel: Autoschlüssel sendet "Hallo", Auto sendet Zufallszahl, Schlüssel (hat private key) verschlüsselt die Zahl mit dem Private Key. Das Auto versucht das zu entschlüsselt, wenn die Zufallszahl rauskommt = Auto offen.
- **Programmiersprachen** sind Java, PHP und Javascript
- **Datenbankmanagementsystem** wird MySQL oder MARIA DB
- **Chatverlauf** soll nicht gelesen werden können (er soll verschlüsselt auf dem Server liegen)
- nach der ersten **Transportverschlüsselung** gibt es keine weitere Transportverschlüsselung (System ist trusted)
- **Nachrichten-Verteiler** beginnt keine Kommunikation mit dem Web-Server (immer der Web-Server beginnt)
- **Anregungen an unsere Architektur** Verschlüsselung und Load-Balancer früher, Klärung der Frage "Was wird verschlüsselt?"
