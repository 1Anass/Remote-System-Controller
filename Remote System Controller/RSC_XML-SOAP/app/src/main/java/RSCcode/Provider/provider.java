package RSCcode.Provider;

import javax.xml.ws.Endpoint;


public class provider{
    public static void main(String[] args){
        remoteControl remoteController = new remoteControl();

        String url = "http://localhost:1200/RemoteControl";

        Endpoint endpoint = Endpoint.create(remoteController);
        endpoint.publish(url, remoteController);
        try{
        while(true){
            Thread.sleep(60000);
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }
    }
}