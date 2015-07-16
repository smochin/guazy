
package gauzy.client;

import org.json.JSONObject;


public interface Receiver {
    void receive(JSONObject data);
}
