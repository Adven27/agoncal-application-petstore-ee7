package org.agoncal.application.petstore.model;

import java.util.Objects;

public abstract class AbstractEntity<ID> implements Ent<ID> {

    private static final long serialVersionUID = 1L;

    public AbstractEntity() {}

    public AbstractEntity(AbstractEntity<? extends ID> entity) {
        if (isIdSettingSupported()) {
            setId(entity.getId());
        }
    }

    @Override
    public boolean isIdSettingSupported() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Class<? extends AbstractEntity> clazz = getClass();
        return clazz.isInstance(obj) && Objects.equals(clazz.cast(obj).getId(), getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}