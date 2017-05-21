package test;

import var.DBMS;

public class testDB {

	public static void main(String[] args) {
		DBMS dbms = new DBMS();
		String u1 = "user1";
		String u2 = "user2";
		String u3 = "user3";
		String u4 = "user4";
		String[] u = {u1,u2,u3,u4};
		int i = 0;
		for (String user : u) {
			dbms.createUser(user, "mutti123", "susiliebt" + user + "@web.de");
		}

		System.out.println();
		dbms.addContact(u[0],u[1]);
		dbms.addContact(u[0],u[1]);
		dbms.addContact(u[1],u[2]);
		dbms.addContact(u[2],u[3]);

		for (i = 0; i < u.length; i++) {
			System.out.print(u[i] + ":   ");
			for (String s : dbms.getContacts(u[i])) {
				System.out.print(s);
			}
			System.out.println();
		}

		dbms.checkToken("nö");

	}
}
