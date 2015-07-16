package gauzy.teste;

import gauzy.server.BaseServer;

public class BaseMain {
    public static void _main(String[] args) {
        
        BaseServer server = new BaseServer();
        
//        server.onAcept(client -> {
//            System.out.println(client);
//            server.connetions().forEach((id, client1) -> {
//                if(client1 != client) {
//                    client1.send(client.id+" Acabou de entrar\n");
//                }
//            });
//        });
//        
//        server.onReceive((client, data) -> {
//            System.out.println(data);
//            if(server.connetions().size() < 2) {
//                client.send("System: Sala vazia, =(\n");
//            } else {
//                server.connetions().forEach((id, client1) -> {
//                    if(client1 != client) {
//                        client1.send(client.id+": "+data+"\n");
//                    }
//                });
//            }
//        });
        
//        server.onForceClose((client, couse) -> {
//            server.connetions().forEach((id, client1) -> {
//                if(client1 != client) {
//                    client1.send(client.id+" saiu da sala\n");
//                }
//            });
//        });
        
        server.listener(7777);
        
    }
}
