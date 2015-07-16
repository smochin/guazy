package gauzy.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import gauzy.common.ChannelHelper;

public class BaseServer implements Server {
    private Map<String, ClientConnection> connections = null;
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;

    private boolean runnging = false;
    private boolean stop = true;

    private Closer closer = null;
    private Acepter acepter = null;
    private Receiver receiver = null;

    public BaseServer() {
        connections = new HashMap<>();
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException ex) {
            Logger.getLogger(BaseServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onForceClose(Closer closer) {
        this.closer = closer;
    }

    @Override
    public void onAcept(Acepter acepter) {
        this.acepter = acepter;
    }

    @Override
    public void onReceive(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
    
    @Override
    public void listener(int port) {
        stop = false;
        try {
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        } catch (IOException ex) {
            Logger.getLogger(BaseServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!isRunning()) {
            Runnable runnable = () -> {
                while (!stop) {
                    try {
                        if (selector.select() == 0) {
                            continue;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keysIt = keys.iterator();
                    while(keysIt.hasNext()) {
                        SelectionKey key = keysIt.next();
                        if (key.isAcceptable()) {
                            try {
                                SocketChannel clientSocketChannel = serverSocketChannel.accept();

                                if (clientSocketChannel != null) {
                                    clientSocketChannel.configureBlocking(false);
                                    clientSocketChannel.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE);
                                    String id = codeForConnection(clientSocketChannel.getRemoteAddress());
                                    ClientConnection connection = new ClientConnection(id, clientSocketChannel);
                                    connections.put(id, connection);
                                    if(null != acepter) {
                                        acepter.onAcept(connection);
                                    }
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(BaseServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (key.isReadable()) {
                            
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String id = null;
                            try {
                                id = codeForConnection(((SocketChannel)key.channel()).getRemoteAddress());
                            } catch (IOException ex) {
                                Logger.getLogger(BaseServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            ClientConnection connection = connections.get(id);
                            id=null;
                            try {
                               String msg = ChannelHelper.readFromClient(clientChannel);
                               if(!msg.isEmpty() && receiver != null) {
                                   receiver.onReceive(connection, msg);
                               }
                               msg = null;
                            } catch (Exception se) {
                                try {
                                    clientChannel.finishConnect();
                                    clientChannel.close();
                                    clientChannel = null;
                                } catch (IOException ex) {
                                    Logger.getLogger(BaseServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                connections().remove(connection.id);
                                connection = null;
                                System.err.println(connections());
                            }
                        }
                        key=null;
                        keysIt.remove();
                    }
                    keysIt=null;
                    keys = null;
                }
            };
            runnable.run();
        } else {
            //já está rodando.....
        }
    }

    @Override
    public void stop() {
        for(int i = 0; i < connections().size(); i++) {
            connections().get(i).close();
            connections=null;
        }
        connections().clear();
        stop = true;
    }
    
    public Map<String, ClientConnection> connections() {
        if(null == connections) {
            connections = new HashMap<>();
        }
        return connections;
    }
    
    private String codeForConnection(SocketAddress address) {
        InetSocketAddress inet = ((InetSocketAddress)address);
        String code = inet.hashCode()+""+inet.getPort();
        inet=null;
        return code;
    }

}
