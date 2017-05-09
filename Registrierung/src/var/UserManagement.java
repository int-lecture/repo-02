package var;

import java.rmi.AccessException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Klasse die sich um die Benutzerverwaltung kümmert.
 *
 */
public class UserManagement {

	/* Map mit Username als Schlüssel und einer LinkedList für alle Kontakte */
	private static Map<String, LinkedList<String>> UserContacts = new HashMap<String, LinkedList<String>>();

	/*
	 * Map mit Username als Schlüssel und einem Array mit der E-Mail und dem
	 * Passwort
	 */
	private static Map<String, String[]> Userbase = new HashMap<String, String[]>();

	/**
	 * Gibt alle Kontakte eines Nutzers zurück. Wenn es keinen Nutzer mit dem
	 * Namen gibt wird eine Exception geworfen.
	 * 
	 * @param user
	 *            Benutzername
	 * @return Kontakte
	 * @throws AccessException
	 */
	protected String[] getContacts(String user, String token) throws NoSuchElementException, AccessException {
		if (!UserContacts.containsKey(user)) {
			throw new NoSuchElementException();
		}
		if (!checkToken(user, token)) {
			throw new AccessException("Token falsch oder nicht mehr gültig");
		}
		String[] contacts = new String[UserContacts.get(user).size()];
		for (int i = 0; i < contacts.length; i++) {
			contacts[i] = UserContacts.get(user).get(i);
		}
		return contacts;
	}

	/**
	 * Erstellt einen neuen Nutzer
	 * 
	 * @param user
	 *            Benutzername Name des neuen Nutzers
	 */
	protected void createUser(String username, String email, String password) {
		if (!UserContacts.containsKey(username)) {
			UserContacts.put(username, new LinkedList<String>());
			String[] UserInfo = { email, password, generateToken(), new Date().toString() };
			Userbase.put(username, UserInfo);
		}
	}

	/**
	 * Generiert ein neues sicheres Token.
	 * 
	 * @return Validierungstoken
	 */
	private String generateToken() {
		// TODO: Token generieren
		return "extremely save token";
	}

	/**
	 * Überschreibt das Token und die Zeit an dem es gesetzt wurde.
	 * 
	 * @param user
	 *            Benutzername
	 * @param token
	 *            Validierungstoken
	 */
	protected void setToken(String user, String token) {
		Userbase.get(user)[2] = token;
		Userbase.get(user)[3] = new Date().toString();
	}

	/**
	 * Überprüft ob das Token eines Nutzers korrekt ist und ob es noch gültig
	 * ist.
	 * 
	 * @param user
	 *            Benutzername
	 * @param token
	 *            Validierungstoken
	 * @return Gültigkeit
	 */
	protected boolean checkToken(String user, String token) {
		// TODO: Gültigkeit hinzufügen
		return token.equals(Userbase.get(user)[2]);
	}

	/**
	 * Gibt die Email eines Nutzers zurück, wenn dieser sich mit dem Token
	 * ausweisen kann.
	 * 
	 * @param user
	 *            Benutzername
	 * @param token
	 *            Validierungstoken
	 * @return E-Mail
	 * @throws AccessException
	 */
	protected String getEmail(String user, String token) throws AccessException {
		if (checkToken(user, token)) {
			return Userbase.get(user)[0];
		} else {
			throw new AccessException("Token falsch oder nicht mehr gültig");
		}
	}

	/**
	 * Legt einen neuen Kontakt für den angegebenen Nutzer an. Wirft eine
	 * Exception wenn der Nutzer nicht registriert ist.
	 * 
	 * @param user
	 *            Benutzername Name des Nutzers
	 * @param contact
	 *            Name des neuen Kontakts
	 * @throws InvalidParameterException
	 */
	protected void addContact(String username, String contact) throws InvalidParameterException {
		if (!UserContacts.containsKey(username) || username.equals(contact)) {
			throw new InvalidParameterException("Falsche Parameter: (" + username + ", " + contact + ")");
		}
		if (!UserContacts.get(username).contains(contact)) {
			UserContacts.get(username).add(contact);
		}
	}

	/**
	 * Fügt Beispielwerte hinzu.
	 */
	protected void BeispielWerte() {
		String[] newUsers = { "Bob", "Hans", "Lisa", "Tom", "Tim", "Kevin", "David", "Fabian", "Philip" };
		for (String user : newUsers) {
			createUser(user, " ", " ");
			int length = (int) (Math.random() * 4 + 1);
			for (int i = 0; i < length; i++) {
				int Kontakt = (int) (Math.random() * newUsers.length - 1);
				try {
					addContact(user, newUsers[Kontakt]);
				} catch (InvalidParameterException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		System.out.println(this.toString());
	}

	
	/**
	 * Gibt einen String zurück der alle User und deren Kontakte enthällt.
	 */
	@Override
	public String toString() {
		String s = "";
		for (String user : UserContacts.keySet()) {
			s = s + "User: " + user + " ->";
			try {
				for (String contacts : getContacts(user, Userbase.get(user)[2])) {
					s = s + "  " + contacts;
				}
			} catch (AccessException | NoSuchElementException e) {
				System.out.println(e.getMessage());
			}
			s = s + "\n";
		}
		return s;
	}
}
