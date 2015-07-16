package gauzy.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import gauzy.server.ClientConnection;

public abstract class ChannelHelper {

    public static String readFromServer(SocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        int bytesRead = 0;
        StringBuilder builder = new StringBuilder();
        
        try {
            while ((bytesRead = clientChannel.read(buffer)) > 0) {
                buffer.flip();
                builder.append(Charset.defaultCharset().decode(buffer).toString());
                buffer.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChannelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return builder.toString();
    }
    
    public static String readFromClient(SocketChannel clientChannel) throws CloseException {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        int bytesRead = 0;
        StringBuilder builder = new StringBuilder();
        
        try {
            while ((bytesRead = clientChannel.read(buffer)) > 0) {
                buffer.flip();
                builder.append(Charset.defaultCharset().decode(buffer).toString());
                buffer.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChannelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(bytesRead < 0) {
            throw new CloseException();
        }
        
        return builder.toString();
    }

    public static void send(SocketChannel channel, String data) {
        CharBuffer buffer = CharBuffer.wrap(data);
        while (buffer.hasRemaining()) {
            try {
                channel.write(Charset.defaultCharset().encode(buffer));
            } catch (IOException ex) {
                Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        buffer.clear();
    }
    
    public static void close(SocketChannel channel) {
        try {
            channel.finishConnect();
            channel.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
