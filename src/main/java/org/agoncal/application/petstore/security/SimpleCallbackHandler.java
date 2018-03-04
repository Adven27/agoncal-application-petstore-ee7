package org.agoncal.application.petstore.security;

import org.agoncal.application.petstore.view.shopping.CredentialsBean;

import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.callback.*;
import java.io.IOException;

@Named
public class SimpleCallbackHandler implements CallbackHandler {

    @Inject
    //@RequestScoped
    private CredentialsBean credentials;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callback;
                nameCallback.setName(credentials.getLogin());
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                passwordCallback.setPassword(credentials.getPassword().toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}