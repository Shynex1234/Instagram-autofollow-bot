package mailManager.Instagram.scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import mailManager.MyDriverManager;

import java.awt.image.BufferedImage;

//gehe auf einen Accound und öffne das erste Bild. drücke danach start
public class ImageScraper {

    static int minimumLikes = 5000;

    static ChromeDriver driver = null;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void start(ChromeDriver _driver) {
        driver = _driver;
        
        new Thread(() -> {
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    
                    System.out.println("start dowload of pictures");
                    startDowloading();
                     
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        }).start();
    }

    private static void startDowloading(){
        boolean firstPicture=true;
        int pictureNumber =  2;
        while(true){
            
            MyDriverManager.wait(4, 5);
            try{
                int likes=5000;
                try{
                    likes =  Integer.parseInt(driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/article/div[3]/section[2]/div/div/button/span")).getText().replace(".",""));
                }
                catch(Exception e){ System.out.println("Likes werden nicht angezeigt");};
               
               if(minimumLikes<=likes){//dowload image  
                  try {  
                    String link="";                                         
                          try{//unterschiedliche Element machmal
                            link = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div/article/div[2]/div/div/div[1]/div[1]/img")).getAttribute("src");
                          }
                          catch(Exception e){
                            link = driver.findElement(By.xpath("html/body/div[5]/div[2]/div/article/div[2]/div/div[1]/div[2]/div/div/div/ul/li[2]/div/div/div/div[1]/div[1]/img")).getAttribute("src");
                          }                                    
                        
                        URL imageURL = new URL(link);
                        BufferedImage saveImage = ImageIO.read(imageURL);
                  

                        //download image to the workspace where the project is, save picture as picture.png (can be changed)
                        int dowloadedImages = new File("Content").listFiles().length;
                        ImageIO.write(saveImage, "png", new File("Content\\"+(dowloadedImages+1) + ".png"));
                        System.out.println("Bild gefunden und heruntergeladen.");
                    } catch (Exception e) {
                        System.out.println("Kein Bild gefunden");
                    }
                    
                }      
                                                
                //next picture              
                
                    pictureNumber =  2;
                    if(firstPicture){     
                        driver.findElement(By.xpath("/html/body/div[5]/div[1]/div/div/a")).click();
                    }
                    else{             
                        driver.findElement(By.xpath("/html/body/div[5]/div[1]/div/div/a[2]")).click();
                    }
                
                
                firstPicture=false;
                
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }

    }
}
