package gauzy.client;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class EventClient {
    private Map<String, EventHandler> handlers = null;
    private BaseClient client = null;

    private EventClient(String host, int port) {
        client = new BaseClient(host, port);
        handlers = new HashMap<>();
    }
    
    public static EventClient openServer(String host, int port) {
        return new EventClient(host, port);
    }
    
    public void on(String event, EventHandler handler) {
       handlers.put(event, handler);
    }

    public void receive() {
        client.onReceive(params->{
            System.out.println(params.toString());
            if(handlers.containsKey(params.getString("event"))) {
                handlers.get(params.getString("event")).handle(params);
            }
        });
    }

    private JSONObject params = null;
    public EventClient event(String string) {
        param("event", string);
        return this;
    }

    public EventClient param(String key, String value) {
        if(null == params) {
            params = new JSONObject();
        }
        params.put(key, value);
        return this;
    }
    
    public void send() {
        client.send(params);
        params=null;
    }

    public void emmit(String event) {
        event(event).send();
    }
    
}
