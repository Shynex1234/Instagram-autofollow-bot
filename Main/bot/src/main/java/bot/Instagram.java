package bot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;


import bot.ManageDb.SQLManager;

import java.util.ArrayList;
import java. util.List;

public class Instagram {
    static ChromeDriver driver=null;
    public static void follow(int anzahl){
    
       driver = App.driver;
                                                           
        
        int scrollPoints=52;
        int addetUsers =1;

        
 
                        
        boolean abwechseln = true;         
        while(anzahl>=addetUsers)
        {    //org.openqa.selenium.StaleElementReferenceException
        try{
            String letztername="";
            String letzterNamederVorherigenRunde=""; 
                WebElement scrollElement =null;
            if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]")){
                scrollElement =driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]"));
            }                                            
            else if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div[2]/div")){
                scrollElement =driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div"));
            }else{
                System.out.println("scroll element nicht gefunden");
            } 
            
            WebElement firstElement =null;                                                   
            try{                                           
                firstElement = driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div/div/div[1]"));
            } 
            catch(Exception e) {
                try{                                            
                    firstElement = driver.findElement(By.xpath("//html/body/div[5]/div/div/div[2]/ul/div/li[1]/div"));
                }catch(Exception ex){                           
                    firstElement = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/div/div/div[1]"));
                }
                    
            }     
            System.out.println("erstes Element: "+firstElement);                                     
            
            List<WebElement> listOfElements = firstElement.findElements(By.xpath("following-sibling::*"));
            listOfElements.add(0, firstElement);
            System.out.println("Es wurden "+listOfElements.size()+" neue Elemente geladen");

            String name="";
            for(WebElement element : listOfElements){
                
                try{//falls das element nicht mehr actuell ist
                    name =element.findElement(By.cssSelector(".FPmhX.notranslate.MBL3Z")).getAttribute("title");
                    if(!SQLManager.alreadyFollowed(name)){
                        //follow
                        System.out.println("Benutzer "+name+" wird geaddet");
                        clickAddButton(scrollElement, scrollPoints, element);
                        SQLManager.addProfileToAngefragt(name);
                        System.out.println(name+" wurde geaddet "+addetUsers+"/"+anzahl);
                        addetUsers++;
                        MyDriverManager.wait(36,48);
                    }
                    else{
                        System.out.println(name+" wurde bereits geaddet");
                    }
                    if(anzahl<=addetUsers){
                        break;
                    }
                }catch(Exception e){
                    
                    break;
                }
                
            }

            
            if(abwechseln){
                letztername=name;
                abwechseln=false;
            }else{
                letzterNamederVorherigenRunde=name;
                abwechseln=true;
            }
            if(letztername.equals(letzterNamederVorherigenRunde)){
                System.out.println("Es wurden alle Leute dieser Liste geaddet");
                break;
            }

           
           
           System.out.println("");
           scroll_Page(scrollElement, scrollPoints);
           scrollPoints+=500;
        }catch(org.openqa.selenium.StaleElementReferenceException e){
            System.out.println("org.openqa.selenium.StaleElementReferenceException");
            System.out.println("Es wird 15-20s gewartet und dann weiter gemacht");
            MyDriverManager.wait(15,20);
            continue;
        }
           
       }
       SQLManager.addlog(addetUsers-1+" Benutzern getfolgt",0,0);
       System.out.println("Es wurden "+(addetUsers-1)+" geaddet.");
    }

    public static void unfollow(int anzahl){
  
        
        driver = App.driver;
        MyDriverManager.wait(3,6);

        WebElement scrollElement =null;
        if(MyDriverManager.ElementExistsXpath("/html/body/div[5]/div/div/div[2]")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]"));
        }
        else if(MyDriverManager.ElementExistsXpath("/html/body/div[6]/div/div/div[2]/div")){
            scrollElement =driver.findElement(By.xpath("/html/body/div[6]/div/div/div[2]/div"));
        }
        int scrollPoints = scrollToBottom(scrollElement);

        //unfollow
        
        List<String> unfollowedUsers = new ArrayList<>();
        while(unfollowedUsers.size()<=anzahl){
            WebElement firstElement = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li[1]"));
            List<WebElement> listOfElementsInWrongOrder = firstElement.findElements(By.xpath("following-sibling::*"));

            List<WebElement> listOfElements = new ArrayList<>();

            for(WebElement ele :listOfElementsInWrongOrder){
                listOfElements.add(0,ele);
            }
            
            for(WebElement element : listOfElements){
                boolean alreadyUnfollowed = false;
                String name = element.findElement(By.cssSelector(".FPmhX.notranslate._0imsa")).getText();
                for(String s : unfollowedUsers){
                    if(s.equals(name)){
                        alreadyUnfollowed=true;
                    }
                }
                
                if(!alreadyUnfollowed){
                    clickRemoveButtons(scrollElement,element,scrollPoints);
                    unfollowedUsers.add(name);
                    if(unfollowedUsers.size()<=anzahl){//damit man wenn alle geaddet wurden nicht noch warten muss
                        MyDriverManager.wait(36,48);
                    }
                    System.out.println(unfollowedUsers.size()+"/"+anzahl);
                }
                if(unfollowedUsers.size()>=anzahl){
                    break;
                }
            }
            if(unfollowedUsers.size()>=anzahl){
                break;
            }
            scrollPoints-=300;
            scroll_Page(scrollElement, scrollPoints);
            
        }
        
        SQLManager.addlog(unfollowedUsers.size()+" Benutzern entfolgt",0,0);
       
        
        
    }
    private static void clickRemoveButtons(WebElement scrollElement, WebElement ele, int scrollPoints){
        
        while(!ele.isEnabled()){
            scroll_Page(scrollElement,scrollPoints);
            scrollPoints-=50;
        }
        ele.findElement(By.tagName("button")).click();  
        MyDriverManager.wait(1,2);
        driver.findElement(By.cssSelector(".aOOlW.-Cab_")).click();       
        
    }
    private static void clickAddButton(WebElement scrollElement, int scrollPoints, WebElement ele){
        
        WebElement button = ele.findElement(By.tagName("button"));
        
        
        while(!button.isEnabled()){  
            scroll_Page(scrollElement, scrollPoints); 
            scrollPoints+=50;                             
        }
        button.click();
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
    private static int scrollToBottom(WebElement scrollElement)
    {                      
        
        int scrollPoints = 0; 
        
        boolean abwechseln = true;
        String lastName=null;
        String currentName=null;


        while(true){
            MyDriverManager.wait(2,3);
            WebElement firstElement = driver.findElement(By.xpath("/html/body/div[5]/div/div/div[2]/ul/div/li[1]"));
            List<WebElement> listOfElements = firstElement.findElements(By.xpath("following-sibling::*"));
            listOfElements.add(0, firstElement);

            String lastNameOfList=null;
            for(WebElement ele :listOfElements){
                lastNameOfList=ele.findElement(By.cssSelector(".FPmhX.notranslate._0imsa")).getText();
            }
            if(abwechseln){
                lastName=lastNameOfList;
                abwechseln=false;
            }else{
                currentName=lastNameOfList;
                abwechseln=true;
            }
            if(lastName!=null&&currentName!=null){
                if(currentName.equals(lastName)){
                    break;
                }
            }
            
            scrollPoints+=1000;
            scroll_Page(scrollElement, scrollPoints);
            
        }
        System.out.println("Es wurde nach unten gescrollt");
        return scrollPoints;
        
    }
    
    public static void log(){
        driver = App.driver;
        String follower = driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[2]/a/span")).getText().replace(".", "").replace("k","000");
        String following = driver.findElement(By.xpath("/html/body/div[1]/section/main/div/header/section/ul/li[3]/a/span")).getText().replace(".", "").replace("k","000");

        SQLManager.addlog("Update", Integer.parseInt(following), Integer.parseInt(follower));
    }
}

    

