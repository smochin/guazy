package gauzy.teste;

import gauzy.server.event.EventServer;

public class EventMain_ {
    static long count = 0;
    public static void main(String[] args) {
        
        Runnable runnable = () -> {
            EventServer event = new EventServer();
            
            event.on("ping", (client, message) -> {
                System.out.print(count++);
                client.event("pong").param("data", "fasdasd").param("@Client", client.id).send();
            });
            
            event.on("connections", (client, message) ->{
                client.event("response-connection").param("list", event.connections().toString()).send();
            });
            
            event.on("", null);

            event.listener(7070);
        };
        
        Thread thread = new Thread(runnable, "Agora Papai");
        thread.start();
        
    }
}
