package mailManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.openqa.selenium.chrome.ChromeDriver;

import mailManager.createEMails.webDe;
import mailManager.createEMails.ManageDb.LiteSQL;
import mailManager.createEMails.ManageDb.SQLManager;

/**
 * Hello world!
 */
public final class App {
	static ChromeDriver driver = null;

	private App() {
	}

	/**
	 * Says hello to the world.
	 * 
	 * @param args The arguments of the program.
	 */
	public static void main(String[] args) {
		// setup
		LiteSQL.connect();
		SQLManager.onCreate();
		driver=MyDriverManager.getDriver();

		WaitForShutdown();
		int startWert=30;
		for (int i = 0; i < startWert; i++) {
			
			try {
				if(webDe.start(driver)==false){//Ip got rejected
					System.out.println("Ip got rejected");
					MyDriverManager.wait(1200,1200);
				}
				driver.close();
				MyDriverManager.wait(200,400);
			} catch (Exception e) { System.out.println("Account konnte wegen eines fehlers nicht erstellt werden: "+e.getMessage());}
			if((i+1)!=startWert){
				driver=MyDriverManager.getDriver();
			}
		}   
		
		shutDown();
		
		
	}

	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static void WaitForShutdown() {
		new Thread(() -> {

			String line = "";

			try {

				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("stop")) {

						shutDown();
						break;
					} else {
						System.out.println("Use 'stop' to stop the program");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}

	private static void shutDown() {
		if(driver!=null){
			driver.close();
		}
		
		LiteSQL.disconnect();
		try {
			System.exit(0);
			reader.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
