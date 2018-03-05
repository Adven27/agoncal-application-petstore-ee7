package org.agoncal.application.petstore.model;

import java.io.Serializable;

public interface Ent<ID> extends Identifiable<ID>, Serializable {

    /**
     * May be unsupported when ID contains references to other entities.
     */
    void setId(ID id);

    boolean isIdSettingSupported();

}
