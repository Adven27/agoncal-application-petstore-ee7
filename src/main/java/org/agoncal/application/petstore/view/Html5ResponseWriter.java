package org.agoncal.application.petstore.view;

import java.io.IOException;
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

public class Html5ResponseWriter extends ResponseWriterWrapper {

    private static final String[] HTML5_INPUT_ATTRIBUTES = { "data-name"/*, "data-id", "autofocus" */};

    private ResponseWriter wrapped;

    public Html5ResponseWriter(ResponseWriter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        return new Html5ResponseWriter(super.cloneWithWriter(writer));
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        super.startElement(name, component);
        UIComponent component2 = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if (("div".equals(name) || "input".equals(name)) && component2 != null) {
            for (String attributeName : HTML5_INPUT_ATTRIBUTES) {
                String attributeValue = (String) component2.getAttributes().get(attributeName);
                if (attributeValue != null) {
                    super.writeAttribute(attributeName, attributeValue, null);
                }
            }
        }
    }

    @Override
    public ResponseWriter getWrapped() {
        return wrapped;
    }

}