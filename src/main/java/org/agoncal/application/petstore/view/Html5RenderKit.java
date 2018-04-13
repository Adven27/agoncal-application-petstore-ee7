package org.agoncal.application.petstore.view;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import java.io.Writer;

public class Html5RenderKit extends RenderKitWrapper {

    private RenderKit wrapped;

    public Html5RenderKit(RenderKit wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return new Html5ResponseWriter(super.createResponseWriter(writer, contentTypeList, characterEncoding));
    }

    @Override
    public RenderKit getWrapped() {
        return wrapped;
    }

}
