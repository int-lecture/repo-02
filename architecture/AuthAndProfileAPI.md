# API für Authentifizierung und Profile

  ## Erarbeiten der Details zum HTCPCP-Protokoll (HTCPC-TEA-Protokoll)
  Ihr Server wird wärend der Registrierung bei der Eingabe von einem bereits vergebenem Pseudonym oder 
  User, ein Antwort-JSON mit dem Statuscode 418 - I'M A TEAPOT liefern. Bearbeitet Sie daher zur Vorbereitung vor der Vorlesung   die folgenden Unterlagen:
  
  * HTCPCP (Hyper Text Coffee Pot Conrol Protocol)
    - [Wikipedia HTCPCP](https://de.wikipedia.org/wiki/Hyper_Text_Coffee_Pot_Control_Protocol)
    - [RFC 2324](https://tools.ietf.org/html/rfc2324)
    - [RFC 7168](https://tools.ietf.org/html/rfc7168)
      
  ## Ports der Services :
  
  * Login : 5001
  * Registrierung : 5002
  
  ## Login eines Benutzers
  
  Loginanfragen werden bei der URL `/login` per `POST` als JSON-Dokument (Mime-Type: `application/json`) abgegeben. 
  Eine beispielhafte Nachricht sieht wie folgt aus:

```json
{
  "user": "bob@web.de",
  "password": "halloIchbinBob"
}
```
Das Token ist gemäß [Base64](https://de.wikipedia.org/wiki/Base64) formatiert.

Sind die Daten nicht korrekt formatiert, sendet der Server den Statuscode 400. Zusätzliche Felder werden aber ignoriert, damit man später das Protokoll einfacher erweitern kann.

Sind die Daten korrekt formatiert, sendet der Server den Status 200 und ein Antwort-JSON mit der Token in Base64-Format und dem Auslaufdatum. Z.B.:

```json
{
  "token": "test123",
  "expire-date": "2017-03-30T17:00:00Z"
}
```
  ## Validierung des Tokens
  
  Authentifizierungsanfragen werden bei der URL `/auth` per `POST` als JSON-Dokument (Mime-Type: `application/json`) abgegeben. 
  Eine beispielhafte Nachricht sieht wie folgt aus:

```json
{
  "token": "test123",
  "pseudonym": "hans"
}
```
Falls die Authentifizierung erfolgreich ist sendet der Server den Statuscode 200 und eine json response zurück.

```json
{
  "success": "true",
  "expire-date": "2017-03-30T17:00:00Z"
}
```
Ist das Token nicht korrekt formatiert oder abgelaufen, sendet der Server den Statuscode 401. 

## Profilanfragen
  
  Profilanfragen werden bei der URL `/profile` per `POST` als JSON-Dokument (Mime-Type: `application/json`) abgegeben. 
  Eine beispielhafte Nachricht sieht wie folgt aus:

```json
{
  "token": "test123",
  "getownprofile": "bob"
}
```

Sind die Daten nicht korrekt formatiert, sendet der Server den Statuscode 400. Zusätzliche Felder werden aber ignoriert, damit man später das Protokoll einfacher erweitern kann.

Sind die Daten korrekt formatiert, sendet der Server den Status 200 und ein Antwort-JSON mit allen Daten des eigenen Profils.
z.B:

```json
{
  "name": "susi",
  "email": "susiLiebtBob@web.de",
  "contact": ["bob" , "peter", "klaus"]
}
```

## Registrierungsanfragen
  
 Registrierungsanfragen werden bei der URL `/register` per `PUT` als JSON-Dokument (Mime-Type: `application/json`) abgegeben. 
  Eine beispielhafte Nachricht sieht wie folgt aus:

```json
{
  "pseudonym": "bob",
  "password": "halloIchbinBob",
  "user": "bob@web.de"
}
```

Sind die Daten nicht korrekt formatiert, sendet der Server den Statuscode 400. Zusätzliche Felder werden aber ignoriert, damit man später das Protokoll einfacher erweitern kann.

Ist ein Pseudonym  oder user schon vergeben, sendet der Server den Statudscode 418.

Sind die Daten korrekt formatiert, sendet der Server den Status 200 und ein Antwort-JSON mit dem Resultat. Z.B.:

```json
{
  "success": "true",
}
```

## Chat Server Änderungen

Die /send Methode des Chat Servers muss um das Token des Absenders ergänzt werden.

```json
{
  "token": "1234acbd",
  "from": "bob",
  "to" : "jim",
  "date": "2017-03-30T17:00:00Z",
  "text": "Hallo Jim, wie geht es Dir?"  
}
```

Außerdem muss ein neuer response Typ 401 (Unauthorized) zurück gegeben werden falls die sessionid ungültig oder abgelaufen ist.
