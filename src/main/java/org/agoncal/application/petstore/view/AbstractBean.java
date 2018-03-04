package org.agoncal.application.petstore.view;

import org.agoncal.application.petstore.util.Loggable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static javax.faces.application.FacesMessage.*;

@Loggable
public abstract class AbstractBean {
    private String getMessage(String msgKey, Locale locale, Object... args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle bundle = ResourceBundle.getBundle("Messages", locale, classLoader);
        String msgValue = bundle.getString(msgKey);
        return MessageFormat.format(msgValue, args);
    }

    protected void addInformationMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(SEVERITY_INFO, getMessage(message, context.getViewRoot().getLocale(), args), null));
    }

    protected void addWarningMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(SEVERITY_WARN, getMessage(message, context.getViewRoot().getLocale(), args), null));
    }

    protected void addErrorMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        context.addMessage(null, new FacesMessage(SEVERITY_ERROR, getMessage(message, locale, args), null));
    }

    protected String getParam(String param) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        return map.get(param);
    }

    protected Long getParamId(String param) {
        return Long.valueOf(getParam(param));
    }
}