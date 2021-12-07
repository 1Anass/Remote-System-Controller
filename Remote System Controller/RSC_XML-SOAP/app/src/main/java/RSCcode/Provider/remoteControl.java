package RSCcode.Provider;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import java.util.Objects;
import javax.jws.WebService;

@WebService


public class remoteControl{
    public String[] getProperties(){
        
        
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

    public byte[] screenshot(){
        BufferedImage screenCapture = null;
        byte[] fBytes = null;
        try{
        Robot robot = new Robot();

        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        screenCapture = robot.createScreenCapture(rectangle);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "png", bs);
        fBytes = bs.toByteArray();
        }
        catch(Exception ex){
            ex.printStackTrace();
            return fBytes;
        }
        
        return fBytes;
    }

    public boolean reboot() throws RuntimeException, IOException {
        try{
        String osName = System.getProperty("os.name");

        String shutdownCommand;

        if (osName.contains("Linux") || osName.contains("Mac OS X")) {
            shutdownCommand = "sudo shutdown -r now";
        } else if (osName.contains("Windows")) {

            shutdownCommand = "shutdown /r -t 2";

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