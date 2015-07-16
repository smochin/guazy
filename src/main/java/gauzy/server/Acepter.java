package gauzy.server;

import gauzy.server.ClientConnection;

@FunctionalInterface
public interface Acepter {
    void onAcept(ClientConnection connection);
}
