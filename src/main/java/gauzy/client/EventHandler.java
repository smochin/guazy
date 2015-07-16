package gauzy.client;

import org.json.JSONObject;


@FunctionalInterface
public interface EventHandler {
    void handle(JSONObject parans);
}
