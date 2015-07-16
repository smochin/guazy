package gauzy.server.event;

import gauzy.server.Acepter;
import java.util.HashMap;
import java.util.Map;
import gauzy.server.BaseServer;
import gauzy.server.ClientConnection;
import gauzy.server.Receiver;
import org.json.JSONObject;

public class EventServer {
    private Map<String, EventHandler> events = null;
    private BaseServer server = new BaseServer();
    
    public EventServer() {
        events = new HashMap<>();
        init();
    }
    
    private Receiver receiver = (client, data) -> {
        JSONObject json = new JSONObject(data);
        String event = json.getString("event");
        if(events.containsKey(event)) {
            events.get(event).handle(client, json);
        } else {
            System.out.println("Não existe: "+connections());
        }
        event = null;
        json = null;
    };
    
    private Acepter acepter = (client) -> {
        System.out.println("nova conexao");
    };
    
    private void init() {
        server.onReceive(receiver);
        server.onAcept(acepter);
    }
    
    public void on(String event, EventHandler handler) {
        events.put(event, handler);
    }
    
    public void listener(int port) {
        server.listener(port);
    }

    public Map<String, ClientConnection> connections() {
        return server.connections();
    }

    public void close() {
        server.stop();
        receiver = null;
        acepter = null;
        if(null != events) {
            events.clear();
        }
        events = null;
        server = null;
    }
}
