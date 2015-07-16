package gauzy.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import gauzy.common.ChannelHelper;
import org.json.JSONObject;

public class BaseClient implements Client {

    private SocketChannel channel = null;

    public BaseClient(String host, int port) {
        try {
            channel = SocketChannel.open(new InetSocketAddress(host, port));
            channel.configureBlocking(false);

            System.out.println("AAAAAAAA");
            while (channel.isConnectionPending());
            if (channel.isConnected()) {
                if (acceptedHandler != null) {
                    acceptedHandler.handle();
                }
            }

            listener();

        } catch (IOException ex) {
            if (error != null) {
                error.error(ex);
            }
        }
    }

    private AcceptedHandler acceptedHandler = null;

    @Override
    public void onAccepted(AcceptedHandler acceptedHandler) {
        this.acceptedHandler = acceptedHandler;
    }

    private Closer closer = null;

    @Override
    public void onForceClose(Closer closer) {
        this.closer = closer;
    }

    private Receiver receiver = null;

    @Override
    public void onReceive(Receiver receiver) {
        this.receiver = receiver;
    }

    private Error error = null;

    @Override
    public void onError(Error error) {
        this.error = error;
    }

    private void listener() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                String msg = ChannelHelper.readFromServer(channel);
                if (!msg.isEmpty() && receiver != null) {
                    receiver.receive(new JSONObject(msg));
                }
            }
        });
    }

    public void send(JSONObject params) {
        ChannelHelper.send(channel, params.toString());
    }

}
