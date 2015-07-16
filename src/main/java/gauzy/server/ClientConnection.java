package gauzy.server;

import java.nio.channels.SocketChannel;
import gauzy.common.ChannelHelper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class ClientConnection {
    public final String id;
    private SocketChannel channel;
    
    public ClientConnection(String id, SocketChannel channel) {
        this.id = id;
        this.channel = channel;
    }
    

    private JSONObject json = null;
    public ClientConnection event(String event) {
        param("event", event);
        return this;
    }

    public ClientConnection param(String key, String value) {
        if(null == json) {
            json = new JSONObject();
        }
        json.put(key, value);
        return this;
    }
    
    public void send() {
        ChannelHelper.send(channel, json.toString());
        json = null;
    }

    public void emmit(String event) {
        event(event).send();
    }

    public void close() {
        try {
            emmit("finish");
            channel.shutdownInput();
            channel.shutdownOutput();
            ChannelHelper.close(channel);
            channel = null;
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
