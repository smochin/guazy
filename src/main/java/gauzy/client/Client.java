package gauzy.client;

public interface Client {
    void onAccepted(AcceptedHandler acceptedHandler);
    void onForceClose(Closer closer);
    void onReceive(Receiver receiver);
    void onError(Error exception);
}
