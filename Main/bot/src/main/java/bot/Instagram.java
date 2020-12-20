package bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.stream.Collectors;
import java.io.*;

import bot.ManageDb.SQLManager;


import org.openqa.selenium.JavascriptExecutor;

public class Instagram {
    static ChromeDriver driver=null;
    public static void follow(int anzahl){
        int count=1;
       driver = App.driver;
                                                           
        WebElement scrollElement =null;
        if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]"));
        }                                            
        else if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div[2]/div")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div"));
        }else{
            System.out.println("scroll element nicht gefunden");
        }
        int scrollPoints=52;
        int addetUsers =0;


        WebElement ele =null;
        while(anzahl!=addetUsers)
        {                                                   
           if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[2]/div[1]/div/div/span/a")){
             ele = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[2]/div[1]/div/div/span/a"));
           }                                           
           else if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[1]/div[2]/div[1]/span/a")) {
                 ele = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[1]/div[2]/div[1]/span/a"));
           }  
           else if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div[2]/div/div/div["+count+"]/div[2]/div[1]/div/span/a")) {
            ele = driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div/div/div["+count+"]/div[2]/div[1]/div/span/a"));
        }                                     
           if(ele==null){
             scroll_Page(scrollElement, scrollPoints);
             continue;
           }  

           String name =ele.getAttribute("title");
           if(!SQLManager.alreadyFollowed(name)){
               //follow
               clickAddButtons(scrollElement, scrollPoints, count);
               SQLManager.addProfileToAngefragt(name);
               System.out.println(name+" wurde geaddet "+addetUsers);
               addetUsers++;
               MyDriverManager.wait(36, 48);
           }
           else{
            System.out.println(name+" wurde bereits geaddet");
           }
           
           
           scrollPoints+=50;
           count++;  
       }
       SQLManager.addlog(addetUsers+" Benutzern entfolgt",0,0);
       System.out.println("Es wurden "+addetUsers+" geaddet.");
    }

    public static void unfollow(int anzahl){

        driver = App.driver;
        //reload page um die zahl bei abonierten upzudaten 
        driver.get(driver.getCurrentUrl());
        MyDriverManager.wait(3,6);
        //abonierte perosnen
        String abonierte = driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[3]/a/span")).getText();
        driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[3]/a")).click();
        MyDriverManager.wait(3,6);

        WebElement scrollElement =null;
        if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]"));
        }
        else if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div[2]/div")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div"));
        }
        int scrollPoints = scrollToBottom(scrollElement,abonierte);

        //unfollow
        int unfollowedUser =0;
        int abonierteInt=0;
        try{
            abonierteInt = Integer.parseInt(abonierte);
            while(anzahl!=unfollowedUser){    
            
                WebElement element = null;  
                if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+(abonierteInt-unfollowedUser)+"]/div/div[2]/div[1]/div/div/span/a")){
                    element = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+(abonierteInt-unfollowedUser)+"]/div/div[2]/div[1]/div/div/span/a"));
                }
                else if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+(abonierteInt-unfollowedUser)+"]/div/div[1]/div[2]/div[1]/span/a")) {
                    element = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li["+(abonierteInt-unfollowedUser)+"]/div/div[1]/div[2]/div[1]/span/a"));
                }     
                if(element==null){
                  scroll_Page(scrollElement, scrollPoints);
                  continue;
                }  
                clickRemoveButtons(scrollElement,scrollPoints, abonierteInt-unfollowedUser );
                
                unfollowedUser++;
                scrollPoints-=50;
            }
        }catch(Exception e){}
        SQLManager.addlog(unfollowedUser+" Benutzern entfolgt",0,0);
       
        
        
    }
    private static void clickRemoveButtons(WebElement ele, int scrollPoints, int count){
        int ansfangsScrollPoints=scrollPoints;
        String xpathEins ="/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[3]/button";
        String xpathZwei ="/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[2]/button";
        while(true){           
            if(MyDriverManager.ElementExistsXpath(xpathEins))  {
                WebElement element = driver.findElement(By.xpath(xpathEins));
                if(element.isEnabled()){
                    MyDriverManager.wait(1,2);
                    element.click();
                    break;
                }
            }else if(MyDriverManager.ElementExistsXpath(xpathZwei))  {
                
                WebElement element = driver.findElement(By.xpath(xpathZwei));
                if(element.isEnabled()){
                    MyDriverManager.wait(1,2);
                    element.click();
                    break;
                }
            }     
            else {
                MyDriverManager.wait(2,3);
                scroll_Page(ele, scrollPoints);
            } 
            MyDriverManager.wait(1,3);            
            
            scrollPoints-=ansfangsScrollPoints;                             
            
        }
        //"nicht mehr folgen" Bestätigung
        MyDriverManager.wait(2,3);
        if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div/div[3]/button[1]")){
            System.out.println("Nicht folgen botton gefunden");
            driver.findElement(By.xpath("/html/body/div[6]/div/div/div/div[3]/button[1]")).click();
        }
    }
    private static void clickAddButtons(WebElement ele, int scrollPoints, int count){
        int ansfangsScrollPoints=scrollPoints;
       
        //abonnenten Liste
        String xpathEins ="/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[3]/button";
        String xpathZwei ="/html/body/div[5]/div/div/div[2]/ul/div/li["+count+"]/div/div[2]/button";
        String xpathDrei ="/html/body/div[6]/div/div/div[2]/ul/div/li["+count+"]/div/div[3]/button";
        String xpathVier ="/html/body/div[6]/div/div/div[2]/ul/div/li["+count+"]/div/div[2]/button";
        //Like Liste
        String xpathFünf ="/html/body/div[6]/div/div/div[2]/div/div/div["+count+"]/div[3]/button ";
        while(true){           
            if(MyDriverManager.ElementExistsXpath(xpathEins))  {
                WebElement element = driver.findElement(By.xpath(xpathEins));
                if(element.isEnabled()){
                    element.click();
                    break;
                }
            }else if(MyDriverManager.ElementExistsXpath(xpathZwei))  {
                
                WebElement element = driver.findElement(By.xpath(xpathZwei));
                if(element.isEnabled()){
                    element.click();
                    break;
                }
            }     
            else if(MyDriverManager.ElementExistsXpath(xpathDrei))  {
                    
                WebElement element = driver.findElement(By.xpath(xpathDrei));
                if(element.isEnabled()){
                    element.click();
                    break;
                }
            }  
            else if(MyDriverManager.ElementExistsXpath(xpathVier))  {
                    
                WebElement element = driver.findElement(By.xpath(xpathVier));
                if(element.isEnabled()){
                    element.click();
                    break;
                }
            }  
            else if(MyDriverManager.ElementExistsXpath(xpathFünf))  {
                    
                WebElement element = driver.findElement(By.xpath(xpathFünf));
                if(element.isEnabled()){
                    element.click();
                    break;
                }
            }     
            else {
                MyDriverManager.wait(2,3);
                scroll_Page(ele, scrollPoints);
            } 
            //
            scrollPoints+=ansfangsScrollPoints;                             
           
        }
    }


    private static void scroll_Page(WebElement webelement, int scrollPoints)
    {
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
                                                                  
        try{
            eventFiringWebDriver.executeScript("document.querySelector('body > div.RnEpo.Yx5HN > div > div > div.isgrP').scrollTop="+scrollPoints);
        }catch(Exception ex){
            eventFiringWebDriver.executeScript("document.querySelector('body > div.RnEpo.Yx5HN > div > div > div.Igw0E.IwRSH.eGOV_.vwCYk.i0EQd > div').scrollTop="+scrollPoints);
        }
        
    }
    private static int scrollToBottom(WebElement webelement, String aboniertePersonenAnzahl)
    {
        String xpathEins ="/html/body/div[5]/div/div/div[2]/ul/div/li["+aboniertePersonenAnzahl+"]/div/div[3]/button";
        String xpathZwei ="/html/body/div[5]/div/div/div[2]/ul/div/li["+aboniertePersonenAnzahl+"]/div/div[2]/button";

        int scrollPoints = 0;  
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);

        while(true){
            if(MyDriverManager.ElementExistsXpath(xpathEins)){
                if(driver.findElement(By.xpath(xpathEins)).isEnabled()){
                    break;
                }
            }
            if(MyDriverManager.ElementExistsXpath(xpathZwei)){
                if(driver.findElement(By.xpath(xpathZwei)).isEnabled()){
                    break;
                }
            }
            eventFiringWebDriver.executeScript("document.querySelector('body > div.RnEpo.Yx5HN > div > div > div.isgrP').scrollTop="+scrollPoints);
            scrollPoints+=10;
        }
        
        System.out.println("nach unten gescrollt");
        return scrollPoints;
        
    }
    
    public static void log(){
        driver = App.driver;
        String follower = driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[2]/a/span")).getText();
        String following = driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[3]/a/span")).getText();

        SQLManager.addlog("Update", Integer.parseInt(following), Integer.parseInt(follower));
    }
}

    

