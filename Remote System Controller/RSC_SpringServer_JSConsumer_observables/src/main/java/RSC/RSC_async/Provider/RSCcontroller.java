package RSC.RSC_async.Provider;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import java.util.Objects;

import java.util.Base64;
import java.io.*;
import java.util.Base64;
import java.awt.*;
import javax.imageio.*;

@RestController
@RequestMapping(path="/remotehost")

public class RSCcontroller {

    @GetMapping(path="/properties")

    public @ResponseBody String[] getProperties(){
        
        
        try {
            String name = System.getProperty("os.name");
            String version = System.getProperty("os.version");
            String[] properties = {name,version};
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
    }

    @GetMapping(path="/screenshot")

    public @ResponseBody String screenshot(){
        BufferedImage screenCapture = null;
        byte[] fBytes = null;
        String encodedString = null;
        try{
        System.setProperty("java.awt.headless", "false");
        Robot robot = new Robot();

        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        screenCapture = robot.createScreenCapture(rectangle);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        
        ImageIO.write(screenCapture, "png", bs);
        fBytes = bs.toByteArray();
        return Base64.getEncoder().encodeToString(fBytes);
        }
        catch(Exception ex){
            ex.printStackTrace();
            //return fBytes;
            
            return null;
       
        }
        
        
       
    }

    @GetMapping(path="/reboot")

    public @ResponseBody boolean reboot() throws RuntimeException, IOException {
        try{
        String osName = System.getProperty("os.name");

        String shutdownCommand;

        if (osName.contains("Linux") || osName.contains("Mac OS X")) {
            shutdownCommand = "sudo shutdown -r now";
        } else if (osName.contains("Windows")) {

            shutdownCommand = "shutdown /r -t 1000";

        } else {
            return false;
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
        return true;
    }
    catch(Exception ex){
        ex.printStackTrace();
        return false;
    }

    }
}