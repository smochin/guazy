package gauzy.server.ws;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import gauzy.server.BaseServer;
import gauzy.server.event.EventHandler;

public class WSServer {
    private Map<String, WSClient> clients = null;
    private Map<String, EventHandler> events = null;
    private BaseServer server = new BaseServer();

    public WSServer() {
        init();
    }
    
    
    public void init() {
        server.onReceive((client, data) -> {
            
            if(clients.containsKey(client.id)) {
                //já foi logado
            } else {
                //devo logar
                //e adicionar o cliente
            }
        });
    }

    private boolean isHandshake(String data) {
        Scanner scanner = new Scanner(data);
        String line = null;
        System.out.println(data);
        //apenas a primeira linha
        //GET /aspas?a=1 HTTP/1.1
        if(scanner.hasNextLine()) {
            line = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(line);
            String get = st.nextToken(" ");
            String path = st.nextToken(" ");
            System.out.println(get);
            System.out.println(path);
        }
        
        scanner.close();
        scanner = null;
        
        return true;
    }
    
    public void on(EventHandler handler) {
        
    }
    
    
    public void listener(int port) {
        server.listener(port);
    }
    
    
}
