package co.gov.anm.sgd.handler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class WSHandlerResolver implements HandlerResolver {
    
    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
            List<Handler> handlerChain = new ArrayList<Handler>();
            
            WSHandler handler = new WSHandler();
            handlerChain.add(handler);
            
            return handlerChain;
    }
}
