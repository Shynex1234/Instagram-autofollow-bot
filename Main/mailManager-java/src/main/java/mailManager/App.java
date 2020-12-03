package mailManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Hello world!
 */
public final class App {
    static WebDriver driver=null;
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        
        //WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        System.out.println("");
        System.out.println(System.getProperty("user.dir"));
        System.out.println("");

        String baseUrl ="https://patrickhlauke.github.io/recaptcha/";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36";
        ChromeOptions options = new ChromeOptions();
        List<String> arguments = new ArrayList<String>();
        arguments.add("--profile-directory=Default");
        arguments.add("--start-maximized");
        arguments.add("--disable-plugins-discovery");
        arguments.add("--user-agent="+userAgent);
        options.addArguments(arguments);
        
        
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("disable-infobars");
        driver = new ChromeDriver(options);
        
        
        
        driver.get(baseUrl);
        
    }
}
