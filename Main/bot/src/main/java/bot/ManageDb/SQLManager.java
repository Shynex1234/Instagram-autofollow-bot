package bot.ManageDb;

import java.sql.ResultSet;
import java.time.LocalDate;

public class SQLManager {

	public static void onCreate() {

		// Questions
		// MasssageId answer

		LiteSQL.onUpdate(
				"CREATE TABLE IF NOT EXISTS angefragt(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT)");
		LiteSQL.onUpdate(
				"CREATE TABLE IF NOT EXISTS log(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, follower INTEGER, following INTEGER, action TEXT, date Text)");

	}

	public static void addProfileToAngefragt(String name) {
		LiteSQL.onUpdate("INSERT INTO angefragt(name) VALUES ('" + name + "')");
		System.out.println(name + " wurde in der datenbank gespeichert");
	}

	public static void addlog(String action, int following, int follower) {
		LocalDate date = java.time.LocalDate.now();
		LiteSQL.onUpdate("INSERT INTO log(follower,following,action,date) VALUES ("+follower+","+following+",'"+action+"','"+date+"')");
	}
	public static boolean alreadyFollowed(String name){
		ResultSet set = null;

		try{
			set = LiteSQL.onQuery("SELECT name FROM angefragt WHERE name = '"+name+"'");
			set.getString("name");
			return true;
		}catch(Exception e){return false;}

		
	}

	
	
}