package gauzy.teste;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import gauzy.client.EventClient;

public class ClientEventMain {

    public static void main(String[] args) {
        int max = 1000;

        Runnable[] runnables = new Runnable[max];
        for (int i = 0; i < max; i++) {
            runnables[i] = ()->{
                EventClient client = EventClient.openServer("localhost", 7070);

                client.emmit("ping");
                
                TimeUnit s = TimeUnit.SECONDS;
                client.on("pong", params -> {
                    try {
                        s.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientEventMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    client.emmit("ping");
                });

                client.receive();
            };
        }
        
        for(int i = 0; i < max; i++) {
            runnables[i].run();
        }
    }
}
