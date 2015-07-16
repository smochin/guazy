package gauzy.teste;

import gauzy.server.ws.WSServer;

public class WSMain {
    public static void _main(String[] args) {
        WSServer ws = new WSServer();
        
        ws.on((client, data) -> {
            System.out.println(data);
        });
        
        ws.listener(7777);
    }
}
