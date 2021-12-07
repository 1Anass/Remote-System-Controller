import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import java.util.Objects;

public class RSCserver {

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(80);
        while (true) {
            System.out.println("Server is waiting...");
            // opening the connection passively
            Socket pipetoclient = ss.accept();
            System.out.println("Server is connected to client hosted in " + pipetoclient.getInetAddress()
                    + " listening to port " + pipetoclient.getPort() + "\n");
            try {
                InputStream in = pipetoclient.getInputStream();
                OutputStream out = pipetoclient.getOutputStream();

                BufferedReader headerreader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter headerwriter = new BufferedWriter(new OutputStreamWriter(out));

                // Read header sent by the client
                String header = null;
                String command = null;
                command = headerreader.readLine();

                if (Objects.isNull(command))
                    continue;

                if (command.equals("Get")) {
                    // Get the name and version of OS
                    try {
                        String Name = System.getProperty("os.name");
                        String Version = System.getProperty("os.version");

                        // Create header to be sent to the client
                        header = Name + ";" + Version + "\n";

                        // Send header to client

                    } catch (Exception ex) {
                        // header to be sent if properties could not be retrieved
                        header = "Unavailable\n";

                    } finally {
                        headerwriter.write(header, 0, header.length());
                        headerwriter.flush();
                    }

                } else if (command.equals("Screenshot")) {
                    try {

                        Robot robot = new Robot();

                        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);

                        header = "Yes\n";

                        headerwriter.write(header, 0, header.length());
                        headerwriter.flush();

                        ImageIO.write(bufferedImage, "png", out);
                    } catch (Exception ex) {
                        // if exception is raised then the screenshot could not be taken
                        header = "No\n";
                        headerwriter.write(header, 0, header.length());
                        headerwriter.flush();
                    }

                } else if (command.equals("Reboot")) {

                    try {
                        reboot(); // function definition is found below
                    } catch (Exception ex) {
                        header = "Unable\n";
                        headerwriter.write(header, 0, header.length());
                        headerwriter.flush();
                    }
                } else {
                    continue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            pipetoclient.close();
        }

    }

    public static void reboot() throws RuntimeException, IOException {
        String osName = System.getProperty("os.name");

        String shutdownCommand;

        if (osName.contains("Linux") || osName.contains("Mac OS X")) {
            shutdownCommand = "sudo shutdown -r now";
        } else if (osName.contains("Windows")) {

            shutdownCommand = "shutdown /r -t 2";

        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);

    }

}