/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gauzy.server;

import gauzy.common.Couse;
import gauzy.server.ClientConnection;

@FunctionalInterface
public interface Closer {
    void onClose(ClientConnection connection, Couse couse);
}
