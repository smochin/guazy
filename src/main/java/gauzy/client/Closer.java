package gauzy.client;

@FunctionalInterface
public interface Closer {
    void close(String data);
}
