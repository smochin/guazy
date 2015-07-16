
package gauzy.server;

@FunctionalInterface
public interface Receiver {
    void onReceive(ClientConnection connection, String data);
}
