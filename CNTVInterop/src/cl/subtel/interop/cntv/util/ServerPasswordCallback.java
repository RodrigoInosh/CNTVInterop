package cl.subtel.interop.cntv.util;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;

public class ServerPasswordCallback implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        String usuario = FileProperties.getParamProperties("usuario", "Interoperabilidad");
        String password = FileProperties.getParamProperties("password", "Interoperabilidad");

        if (usuario.equals(pc.getIdentifier())) {
            System.out.println("pc.getPassword() " + pc.getPassword());
            pc.setPassword(password);
        }
       
    }
}