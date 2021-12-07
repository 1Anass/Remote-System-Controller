import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.lang.model.util.ElementScanner14;

public class RSCclient {
    public static void main(String[] args) throws Exception {
        // getting the command that the end user choose to do, it is passed from the
        // command line

        // opening the connection actively
        String ipAddress = "localhost";
        Socket pipetoserver = new Socket(ipAddress, 80);

        String command = args[0];
        try {
            InputStream in = pipetoserver.getInputStream();
            OutputStream out = pipetoserver.getOutputStream();

            BufferedReader headerreader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter headerwriter = new BufferedWriter(new OutputStreamWriter(out));

            // the end user choose to get the name and version of the OS of the remote
            // system
            if (command.equals("get")) {
                // create the appropriate header for this operation
                String header = "Get\n";

                // send header to server
                headerwriter.write(header, 0, header.length());
                headerwriter.flush();

                // read header sent by server
                header = headerreader.readLine();
                if (header.equals("Unavailable")) {
                    System.out.println("Server unable to retrieve OS properties, please try later\n");

                } else {
                    // Extract name and version from header
                    StringTokenizer s = new StringTokenizer(header, ";");
                    String Name = s.nextToken();
                    String Version = s.nextToken();

                    // printing the name and version of OS to the client
                    System.out.println("Name: " + Name + " Version: " + Version);
                }
            } else if (command.equals("screenshot")) {

                // create the appropriate header for this operation
                String header = "Screenshot\n";

                // send header to server
                headerwriter.write(header, 0, header.length());
                headerwriter.flush();
                // read header sent by server
                header = headerreader.readLine();
                if (header.equals("No")) {
                    System.out.println("Screenshot could not be taken, please try later");
                }
                
                else if (header.equals("Yes")) {
                    // Prepare date to have it as name of screenshot
                    DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                    LocalDateTime localtime = LocalDateTime.now();
                    // read image as a bufferedimage
                    BufferedImage screenshot = ImageIO.read(in);
                    if(screenshot==null)
                    System.out.println("Photo not received\n");     
                    // name the image with date
                    File imgfile = new File("RSC/ " + dateformatter.format(localtime) + ".png");
                    // save the buffered image to png file
                    ImageIO.write(screenshot, "png", imgfile);
                    System.out.println("Screenshot saved in RSC/ directory and has name: "
                            + dateformatter.format(localtime) + "\n");
                }
            } else if (command.equals("reboot")) {

                // Create the appropriate header for this operation
                String header = "Reboot\n";

                // send header to server
                headerwriter.write(header, 0, header.length());
                headerwriter.flush();

                boolean flag = true;
                // while the remote system is reachable, the client keeps checking if a header
                // is sent
                while (pingRemoteSystem(ipAddress)) {
                    if (headerreader.ready() && headerreader.readLine().equals("Unable")) {
                        System.out.println("Remote system could not be rebooted successfully, please try later\n");
                        flag = false;
                        break;
                    }

                }
                // if the loop finishes because the remote system is no more reachable then, the
                // client annouces that remote system is rebooted successfully
                if (flag == true) {
                    System.out.println("Remote system rebooted successfully\n");
                }

            } else if (command.equals("help")) {
                System.out.println("The program supports the following commands as arguments: \n");
                System.out.println("get: to get name and version of OS of the remote system\n");
                System.out.println("reboot: to reboot the remote system \n");
                System.out.println("screenshot: to take screenshots of the remote systems screen \n");
            } else {
                System.out.println("Invalid Command ! Choose from the following commands: \n");
                System.out.println("get: to get name and version of OS of the remote system\n");
                System.out.println("reboot: to reboot the remote system \n");
                System.out.println("screenshot: to take screenshots of the remote systems screen \n");

            }
        }

        catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pipetoserver.close();
        }

    }

    public static boolean pingRemoteSystem(String ipAddress) throws UnknownHostException, IOException {
        InetAddress remoteSystem = InetAddress.getByName(ipAddress);
        // Ping Request Sent to remote system
        if (remoteSystem.isReachable(5000))
            return true;
        else
            return false;
    }
}