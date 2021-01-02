package bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;

public class MyDriverManager {

    public static ChromeDriver driver;

    public static ChromeDriver getDriver(boolean randomUserAgent, String proxyIp, String proxyPort) {
        // WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        String userAgent = "";
        if (randomUserAgent) {
            userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                    + getRandomChromeVersion() + " Safari/537.36";
        } else {
            userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280 Safari/537.36";
        }

        ChromeOptions options = new ChromeOptions();
        List<String> arguments = new ArrayList<String>();
        arguments.add("--profile-directory=Default");
        arguments.add("--user-agent=" + userAgent);
        arguments.add("--disable-blink-features=AutomationControlled");
        arguments.add("--window-size=1280,800");

        // erstellt fake audio devices, damit man nicht bei tausend andfragen die genau
        // gleichen devices hat
        // arguments.add("--disable-permissions-api");
        arguments.add("--disable-user-media-security");
        arguments.add("--disable-encrypted-media");
        arguments.add("--disable-gesture-requirement-for-media-playback");
        arguments.add("--disable-media-source");
        arguments.add("--disable-media-thread-for-media-playback[8]");
        arguments.add("use-fake-ui-for-media-stream");
        options.addArguments(arguments);

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("disable-infobars");

        // proxy

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        if (!proxyIp.equals("")) {
            Proxy proxy = new Proxy();
            proxy.setSslProxy(proxyIp + ":" + proxyPort);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }

        driver = new ChromeDriver(capabilities);

        driver.get("https://whatismyipaddress.com/de/meine-ip");

        return driver;
    }

    private static String getRandomChromeVersion(){
        String[] versions = {"87.0.4280","86.0.4240","85.0.4183","84.0.4147","83.0.4103","81.0.4044","80.0.3987","79.0.3945","78.0.3904","77.0.3865","76.0.3809","75.0.3770","74.0.3729"};
        Random random = new Random();
        System.out.println((random.nextInt(versions.length)));
        return versions[(random.nextInt(versions.length))];
    }
    public static void wait(int min, int max) {
        Random rn = new Random();


        int randomNumber = (rn.nextInt(max) + min) * 1000;
        try {
            Thread.sleep(Long.valueOf(randomNumber));
        } catch (InterruptedException e) {
            System.out.println("!thread hat nicht gewartet!");
            e.printStackTrace();
        }
        System.out.println("fertig mit warten");
    }

    //element exists
    public static boolean ElementExistsXpath(String xpath){
        try{
            driver.findElement(By.xpath(xpath));
            return true;
        }
         catch (Exception e) {
             
            return false;
        }
    }
    
    public static boolean ElementExistsId(String id){
        try{
            driver.findElement(By.id(id));
            return true;
        }
         catch (Exception e) {
            return false;
        }
    }

    public static void hideBrowser(){
        Point p = new Point(0,-1800);
         driver.manage().window().setPosition(p);
         System.out.println("Der Browser wurde versteckt.");
    }
    public static void showBrowser(){
        Point p = new Point(0,0);
         driver.manage().window().setPosition(p);
         System.out.println("Der Browser wurde hervorgeholt.");
    }
}
