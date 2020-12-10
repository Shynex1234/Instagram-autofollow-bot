package mailManager.captchaSolver;

import java.io.IOException;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.Normal;

public class ImageCaptcha {

    public static String solveCaptcha(String base64) throws IOException, InterruptedException {

        TwoCaptcha solver = new TwoCaptcha("f504eb81edeb8eb6d12e710c3ab4fe2c");
        
        Normal captcha = new Normal();
        captcha.setBase64(base64);

        try {
            solver.solve(captcha);
            System.out.println("Captcha solved: " + captcha.getCode());
            solver.report(captcha.getId(), true);
            return captcha.getCode();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "captcha not solved";
        }

        
    }
}