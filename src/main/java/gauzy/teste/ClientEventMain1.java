package gauzy.teste;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import gauzy.client.EventClient;

public class ClientEventMain1 {

    public static void main(String[] args) {

        EventClient client = EventClient.openServer("localhost", 7070);

        client.emmit("connections");
        TimeUnit s = TimeUnit.SECONDS;
        
        client.on("response-connection", params -> {
            System.out.println(params.toString());
            try {
                s.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientEventMain1.class.getName()).log(Level.SEVERE, null, ex);
            }
            client.emmit("connections");
        });

        client.receive();
    }
}
