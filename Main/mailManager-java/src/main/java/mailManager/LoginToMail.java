package mailManager;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import mailManager.createEMails.ManageDb.Person;
import mailManager.createEMails.ManageDb.SQLManager;

public class LoginToMail {
    static ChromeDriver driver = null;

    public static void LoginToAllAccounts(ChromeDriver _driver){
        driver=_driver;
        Person p = null;
        int personId =20;
        do{     
            p=SQLManager.getPersonById(personId)   ;                   
            loginToMail(p.mail,p.password);
            personId++;
           MyDriverManager.wait(10,20);
           driver.quit();
           driver=MyDriverManager.getDriver();
           System.out.println("Person Number: "+personId);
        }while(p!=null);

        System.out.println("------------------------------------------------------");
        System.out.println("------------------alle Mails gelesen------------------");
        System.out.println("------------------------------------------------------");
    }

    private static void loginToMail(String mail, String password){
        driver.get("https://web.de/?origin=lpc");
        MyDriverManager.wait(5, 10);

        // aktzeptiert die Bedingungen
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/div[4]/iframe")));// ein layer
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/iframe")));// noch ein layer
        driver.findElement(By.xpath("//*[@id=\"save-all-conditionally\"]")).click();// das eigentliche element

        driver.switchTo().defaultContent();

        MyDriverManager.wait(2, 4);

        //Login
        driver.findElement(By.id("freemailLoginUsername")).sendKeys(mail);
        MyDriverManager.wait(4, 7);
        driver.findElement(By.id("freemailLoginPassword")).sendKeys(password);
        MyDriverManager.wait(4, 7);
        //click login
        driver.findElement(By.xpath("//*[@id=\"freemailLoginForm\"]/button")).click();
        MyDriverManager.wait(3,5);
        
        if(MyDriverManager.ElementExistsXpath("/html/body/div/div/div/div/div/a")){
            driver.findElement(By.xpath("/html/body/div/div/div/div/div/a")).click();
        }
        //irgendein iframe                   
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/atlas-app/div/atl-app-stack/atl-app-iframe[4]/iframe")));
        //Postfach
        driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[1]/ul/li[2]/a")).click();
        MyDriverManager.wait(3,5);


        //irgendein iframe    
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/atlas-app/div/atl-app-stack/atl-app-iframe[5]/iframe")));

        try{
           
            openMails();
            driver.switchTo().defaultContent();
        }catch(Exception e){
            driver.findElement(By.xpath("/html/body/div/div/div/div/div/a")).click();
        }
    }
    private static void openMails(){  
        MyDriverManager.wait(1, 2);  
        //Check box
        driver.findElement(By.id("checkbox-select-all")).click();
        MyDriverManager.wait(1, 2);
        //dropdown      
        driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[1]/div[1]/div/form/div[2]/div[1]/ul[1]/li[2]/a")).click();
        MyDriverManager.wait(1, 2);
        //gelesen
        driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/ul/li[6]/input")).click();
       
    }
    
}
