package org.agoncal.application.petstore.security;

import org.agoncal.application.cfg.ConfigProperty;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

public class LoginContextProducer {
    @Inject private SimpleCallbackHandler callbackHandler;

    @Produces
    public LoginContext produceLoginContext(@ConfigProperty("loginConfigFile") String loginConfigFileName,
                                            @ConfigProperty("loginModuleName") String loginModuleName) throws LoginException, URISyntaxException {

        //System.setProperty("java.security.auth.login.config", new File(LoginContextProducer.class.getResource(loginConfigFileName).toURI()).getPath());
        try {
            return new LoginContext(loginModuleName, callbackHandler);
        } catch (Exception e) {
            System.out.println("ouch!!!");
            return null;
        }
    }
}