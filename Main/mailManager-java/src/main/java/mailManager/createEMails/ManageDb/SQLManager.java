package mailManager.createEMails.ManageDb;

import java.time.LocalDate;
import java.util.*;

public class SQLManager {

	public static void onCreate() {

		// Questions
		// MasssageId answer

		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS eMailProfiles(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
		+"mail TEXT,"
		+"firstName TEXT,"
		+"lastName TEXT,"
		+"zipCode TEXT,"
		+"cityName TEXT,"
		+"streetAdress TEXT,"
		+"birthDate TEXT,"
		+"password TEXT,"
		+"phoneNumber TEXT,"
		+"createDate TEXT,"
		+"nextTimeOnline TEXT)");
	}

	public static void addProfile(String mail, String firstName,String lastName,String zipCode,String cityName,String streetAdress,String birthDate,String password,String phoneNumber) {

		Random r = new Random();
		LocalDate date = LocalDate.now(); 

		
			LiteSQL.onUpdate("INSERT INTO eMailProfiles ("
			+"mail,"
			+"firstName,"
			+"lastName,"
			+"zipCode,"
			+"cityName,"
			+"streetAdress,"
			+"birthDate,"
			+"password,"
			+"phoneNumber,"
			+"createDate,"
			+"nextTimeOnline) VALUES ('"+mail + "','" + firstName + "','"+ lastName + "','"+ zipCode + "','"+ cityName + "','"+streetAdress+ "','"+birthDate+"','"+password+"','"+phoneNumber+"','"+date+"','"+date.plusDays(r.nextInt(3))+"')");
		

	}

	
}