package gauzy.client;

@FunctionalInterface
public interface Error {
    void error(Exception exception);
}
