package mailManager.createEMails;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import com.github.javafaker.Faker;
import com.github.javafaker.PhoneNumber;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import mailManager.MyDriverManager;
import mailManager.captchaSolver.ImageCaptcha;
import mailManager.createEMails.ManageDb.SQLManager;

public class webDe {

    static ChromeDriver driver;

    public static boolean start(ChromeDriver _driver) {
        driver = _driver;

        return registerAccount();
    }

    private static boolean registerAccount() {
        driver.get("https://web.de/?origin=lpc");
        MyDriverManager.wait(5, 10);

        // aktzeptiert die Bedingungen
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/div[4]/iframe")));// ein layer
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/iframe")));// noch ein layer
        driver.findElement(By.xpath("//*[@id=\"save-all-conditionally\"]")).click();// das eigentliche element

        driver.switchTo().defaultContent();

        MyDriverManager.wait(2, 4);

        // register
        try {// verändert sich
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[2]/header/div[1]/div[2]/a")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[1]/header/div[1]/div[2]/a")).click();
        }

        MyDriverManager.wait(3, 6);
        // kostenlos
        driver.findElement(By.xpath("//*[@id=\"top\"]/div/div/div/div[1]/div[1]/div[1]/a/img")).click();

        MyDriverManager.wait(3, 3);

        //personal data
        
        Faker faker = new Faker(new Locale("de-DE"));
        String mail;
        String firstName = faker.address().firstName();
        String lastName = faker.address().lastName();
        String zipCode;
        String cityName;
        String streetAdress;
        String birthDate;
        String password;
        String phoneNumber;
        

        String returnValue = setEMail(firstName, lastName);
        if(returnValue.equals("detected")){
            return false;
        }
        else{
            mail = returnValue;
        }
        // herr oder frau
        setGender(firstName, lastName);

        // first name and second name
        setNames(firstName, lastName);

        MyDriverManager.wait(2, 4);
        zipCode = faker.address().zipCode();
        setPlz(zipCode);

        MyDriverManager.wait(2, 4);
        cityName=faker.address().cityName();
        setOrt(cityName);

        MyDriverManager.wait(4, 7);
        streetAdress = faker.address().streetAddress();
        setStreetAddress(streetAdress);

        MyDriverManager.wait(4, 7);
        birthDate = setBirthDate();

        MyDriverManager.wait(3, 5);
        password = createPassword();
        setPassword(password);

        MyDriverManager.wait(1, 2);
        // phone number
        phoneNumber = setPhoneNumber(faker);

        //captcha
        String captcha = solveCaptcha();
        driver.findElement(By.id("captcha")).sendKeys(captcha);

        MyDriverManager.wait(3,5);

        //create account button
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[5]/onereg-terms-and-conditions/onereg-progress-meter/fieldset/div[3]/div/button/span")).click();
        MyDriverManager.wait(2,3);
        
        SQLManager.addProfile(mail,firstName, lastName, zipCode, cityName, streetAdress, birthDate, password, phoneNumber);
        System.out.println("");
        System.out.println("! E-Mail erstellt !");
        System.out.println("");
        //continue button damit gegen Bot detections
        driver.findElement(By.id("continueButton")).click();
        
        MyDriverManager.wait(8,20);

        return true;
    }
    private static String setPhoneNumber(Faker faker){
        
        String phoneNumber;
        do
        {
            MyDriverManager.wait(1,2);
            driver.findElement(By.id("mobilePhone")).clear();
            MyDriverManager.wait(1,2);
            
            phoneNumber = faker.phoneNumber().cellPhone();
            phoneNumber = phoneNumber.substring(4).replace("-", "");
            driver.findElement(By.id("mobilePhone")).sendKeys(phoneNumber);
            
            MyDriverManager.wait(4,8);

            try {
                driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[1]/onereg-alias/fieldset/onereg-progress-meter/div[2]/div[2]/div/button")).click();
            } catch (Exception e) {}
            MyDriverManager.wait(2,4);
            
            
                        
        }while(MyDriverManager.ElementExistsXpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[4]/onereg-password-recovery/fieldset/onereg-progress-meter/onereg-form-row[1]/onereg-error-messages/pos-form-message/div/span/span"));
        return phoneNumber;
    }

    

    private static String solveCaptcha() {

        String base64 = driver.findElement(By.id("captchaImage")).getAttribute("src").substring(23);
      

        try {
            try {
              return  ImageCaptcha.solveCaptcha(base64);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }

        return "";
    }
    

    private static void setPassword(String password){
        driver.findElement(By.id("password")).sendKeys(password);
        MyDriverManager.wait(4,7);
        driver.findElement(By.id("confirm-password")).sendKeys(password);
        MyDriverManager.wait(4,7);
    }

    private static String createPassword(){
        Character[] zahlen = {'1','2','3','4','5','6','7','8','9','0',};
        Character[] großBuchstaben = {'W','E','R','T','Z','U','I','O','P','Ü','A','S','D','F','G','H','J','K','L','Ö','Ä','Y','X','C','V','B','N','M'};
        Character[] kleinBuchstaben = {'w','e','r','t','z','u','i','o','p','ü','a','s','d','f','g','h','j','k','l','ö','ä','y','x','c','v','b','n','m'};
        Character[] sonderzeichen = {'#','-','_','$'};

        String password ="";
        Random rd = new Random();
        for(int i = 0; i<=3;i++){
            password+=zahlen[rd.nextInt(zahlen.length)];
            password+=großBuchstaben[rd.nextInt(großBuchstaben.length)];
            password+=kleinBuchstaben[rd.nextInt(kleinBuchstaben.length)];
            password+=sonderzeichen[rd.nextInt(sonderzeichen.length)];
        }
        return password;
    }

    private static String setBirthDate(){
        int day;
        int month;
        int year;

        Random rd = new Random();

        day = rd.nextInt(28)+1;
        month = rd.nextInt(12)+1;
        year = rd.nextInt(2001-1985+1)+1985;

        //day
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/onereg-form-row[3]/div/div/div/onereg-dob-wrapper/pos-input-dob/pos-input[1]/input")).sendKeys(Integer.toString(day));
        MyDriverManager.wait(1,2);
        //month
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/onereg-form-row[3]/div/div/div/onereg-dob-wrapper/pos-input-dob/pos-input[2]/input")).sendKeys(Integer.toString(month));
        MyDriverManager.wait(1,2);
        //year
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/onereg-form-row[3]/div/div/div/onereg-dob-wrapper/pos-input-dob/pos-input[3]/input")).sendKeys(Integer.toString(year));
        
        return Integer.toString(day)+"."+Integer.toString(month)+"."+Integer.toString(year);
    }

    
    private static String setEMail(String firstName, String secondName){
        MyDriverManager.wait(2,5);
        Random rn = new Random();
        String randomNumber="";
        String fakeName="";
        
        String eMailFeld="/html/body/onereg-app/div/onereg-form/div/div/form/section/section[1]/onereg-alias/fieldset/onereg-progress-meter/div[2]/div[2]/div/pos-input[1]/input";
        do
        {
            MyDriverManager.wait(1,2);
            driver.findElement(By.xpath(eMailFeld)).clear();
            MyDriverManager.wait(1,2);
            do{
                fakeName = (firstName+secondName+randomNumber).replace("ä", "ae").replace("ü", "ue").replace("ö", "oe");
            }while(fakeName.length()>40);
            driver.findElement(By.xpath(eMailFeld)).sendKeys(fakeName);
            MyDriverManager.wait(4,8);

            try {
                driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[1]/onereg-alias/fieldset/onereg-progress-meter/div[2]/div[2]/div/button")).click();
            } catch (Exception e) {}
            MyDriverManager.wait(2,4);
            
            randomNumber = Integer.toString(rn.nextInt(500)-1);

            if(MyDriverManager.ElementExistsXpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[1]/onereg-alias/fieldset/onereg-progress-meter/div[2]/onereg-error-messages/pos-form-message/div/span/span")){
                return "detected";//Emergency; need to wait!; The Ip got blocked
            }
                        
        }while(!MyDriverManager.ElementExistsXpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[1]/onereg-alias/fieldset/onereg-progress-meter/div[2]/div[3]/pos-form-message/div/span/span"));
       System.out.println("fertig mit E-Mail");
        return (fakeName+"@web.de");
    }
    

    

    private static void setGender(String firstName, String secondName){
        Random rn = new Random();
        String xpath ="";
        if(firstName.endsWith("a")){
            //frau
            xpath="/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/div[1]/div/onereg-radio-wrapper[1]/pos-input-radio/label/i";
        }
        else if((rn.nextInt(4)+1)!=1){
            //frau
            xpath="/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/div[1]/div/onereg-radio-wrapper[1]/pos-input-radio/label/i";
        }else{
            //mann
            xpath="/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/div[1]/div/onereg-radio-wrapper[2]/pos-input-radio/label/i";
        }
        driver.findElement(By.xpath(xpath)).click();
        MyDriverManager.wait(1,2);
    }

    private static void setNames(String firstName, String secondName){
         //der normale xpath verändert sich dauernd                            
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/onereg-form-row[1]/div/div[2]/pos-input/input")).sendKeys(firstName);
        MyDriverManager.wait(3,7);
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/onereg-form-row[2]/div/div[2]/pos-input/input")).sendKeys(secondName);
        MyDriverManager.wait(3,7);
    }

    private static void setPlz(String plz){
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/fieldset/div/div/div[1]/onereg-form-row/div/div/pos-input/input")).sendKeys(plz);
    }

    private static void setOrt(String ort){
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/fieldset/div/div/div[2]/onereg-form-row/div/div[2]/pos-input/input")).sendKeys(ort);
    }

    private static void setStreetAddress(String straßeUndHausnummer){
        driver.findElement(By.xpath("/html/body/onereg-app/div/onereg-form/div/div/form/section/section[2]/onereg-progress-meter/onereg-personal-info/fieldset/fieldset/div/onereg-form-row/div/div[2]/pos-input/input")).sendKeys(straßeUndHausnummer);
    }
}