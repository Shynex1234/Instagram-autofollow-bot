package mailManager.Instagram.scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.openqa.selenium.chrome.ChromeDriver;

public class scrapeNewUsers {
    static ChromeDriver driver = null;
    
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void start(ChromeDriver _driver){
        driver = _driver;
        new Thread(() -> {

			String line = "";

			try {

				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("start")) {
                        System.out.println("start dowload of member");
						startDowloading();
						break;
					} else {
					
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
    }

    public static void startDowloading(){
        //driver.findElement(By.xpath(""))
    }
}
