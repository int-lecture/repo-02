# Beispiel anhand des Nachrichten-Verteilers

## Wenn eine Nachricht auf dem Server liegt 
### Client
query { 
  to: "bernd"; 
  last-sequence: 17;
}
### Server
messages[
  message{
    from: "franz";
    to: "bernd";
    sequence: 18;
    text: "Hallo, heute Karstadt?!"
    date: "2017-03-27 T 10:10"
  },...
]

## Wenn eine Nachricht geschickt wird
### Client
message{
  from: "bernd";
  to: "franz";
  date: "2017-03-27 T 10:17"
  text: "Klar, und danach Media Markt?"
}
### Server
received{
  date: "....";
  sequence: 901;
}
