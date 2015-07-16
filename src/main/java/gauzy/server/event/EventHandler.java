package gauzy.server.event;

import gauzy.server.ClientConnection;
import org.json.JSONObject;


@FunctionalInterface
public interface EventHandler {
    void handle(ClientConnection client, JSONObject json);
}
