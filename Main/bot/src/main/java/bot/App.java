package bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.chrome.ChromeDriver;

import bot.ManageDb.LiteSQL;
import bot.ManageDb.SQLManager;

/**
 * Hello world!
 */
public final class App {

    public static ChromeDriver driver = null;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    

    public static void main(String[] args) {
        //setup
        System.out.println("Bitte gebe die Ip des Proxys ein.");
        String proxyId="";
        try {
            proxyId = reader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("Bitte gebe den Port des Proxys ein.");
        String proxyPort="";
        try {
            proxyPort = reader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("Bitte gebe den Namen deiner database  ein.");
        String databse="";
        try {
            databse = reader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        LiteSQL.connect(databse);

        driver = MyDriverManager.getDriver(false,proxyId,proxyPort);
        driver.get("https://whatismyipaddress.com/de/meine-ip");
        System.out.println("please check the Ip");
        MyDriverManager.wait(2,2);
        driver.get("https://www.instagram.com");
        
        SQLManager.onCreate();
        
        //warte auf command
        
        String line = "";

			try {

				while ((line = reader.readLine().toLowerCase()) != null) {
                    String[] args2 = line.split(" ");
					if (line.equals("stop")) {

                        shutDown();
						break;
                    } 
                    else if (args2[0].equals("follow")) {

                        int anzahl =0;
                        try{
                            if(args2.length==1){
                                System.out.println("Bitte gebe die Anzahl der zu Abonierenden Profilen an:   follow [zahl]");
                            }else{
                                anzahl = Integer.parseInt(args2[1]);
                                try{
                                    Instagram.follow(anzahl);
                                }catch(Exception e){
                                    System.out.println("Beim folgen ist ein problem aufgetreten: ");
                                    e.printStackTrace();
                                }
                                
                            }
                            
                        }catch(Exception e){
                            System.out.println("Die zahl hat ein falsches format oder ist keine Zahl");
                        }
						
                    } 
                    else if (args2[0].equals("unfollow")) {

                        int anzahl =0;
                        try{
                            if(args2.length==1){
                                System.out.println("Bitte gebe die Anzahl der zu Abonierenden Profilen an:   unfollow [zahl]");
                            }else{
                                anzahl = Integer.parseInt(args2[1]);
                                try{
                                    Instagram.unfollow(anzahl);
                                }catch(Exception e){
                                    System.out.println("Beim folgen ist ein problem aufgetreten: "+e);
                                }
                                
                            }
                            
                        }catch(Exception e){
                            System.out.println("Die zahl hat ein falsches format oder ist keine Zahl");
                        }
						
                    } 
                    else if(args2[0].equals("log")){
                        try{
                            Instagram.log();
                        }catch(Exception ex){
                            System.out.println("Gehe für diesen Befehl auf dein Instagram profil            "+ ex);
                        }
                        
                    }
                    else{
                        System.out.println("");
                        System.out.println("----------------------------");
                        System.out.println("Diesen Befehl gibt es nicht.");
                        System.out.println("----------------------------");
                        System.out.println("Befehle:");
                        System.out.println("-stop");
                        System.out.println("- < follow [zahl] > Gehe auf irgendeine Liste von Profilen und starte dem command.");
                        System.out.println("- < unfollow [zahl] > Gehe dazu auf dein Profil.");
                        System.out.println("- < log > Speichert die anzahl deiner Abonenten und abonierten ab. Gehe dazu auf dein Profil.");
                        System.out.println("");
                    }

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
    }

    private static void shutDown() {
        LiteSQL.disconnect();
		try {
			System.exit(0);
			reader.close();
        } 
        catch (IOException e) {
			
			e.printStackTrace();
		}
		if(driver!=null){
			driver.close();
		}
		
	}
}
