/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gauzy.server.event;

/**
 *
 * @author andre
 */
public class EventBinder {
    public static EventHandler on(String event, EventHandler handler) {
        return handler;
    }
}
