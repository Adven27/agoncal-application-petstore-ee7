package org.agoncal.application.petstore.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;
import static org.agoncal.application.petstore.model.LocaleEntity.GET_ALL_QUERY;

@Entity
@Table(name = "T_LOCALE")
@NamedQueries(value = {
        @NamedQuery(name = GET_ALL_QUERY, query = "SELECT o FROM LocaleEntity o")
})
@IdClass(LocaleEntity.PrimaryKey.class)
@NoArgsConstructor(access = PROTECTED) //For JPA
public class LocaleEntity extends AbstractEntity<LocaleEntity.PrimaryKey> {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_NAME = "LocaleEntity";

    public static final String GET_ALL_QUERY = ENTITY_NAME + ".getAll";
    public static final String GET_ALL_SUPPORTED_QUERY = ENTITY_NAME + ".getAllSupported";
    public static final String GET_ALL_SUPPORTED_NAMES_QUERY = ENTITY_NAME + ".getAllSupportedNames";
    public static final String GET_PHONE_CODE = ENTITY_NAME + ".getPhoneCode";

    public static final LocaleEntity ROOT = null;

    @Id
    @Column(name = "LOCALE", nullable = false, unique = true)
    private
    @Getter
    @Setter
    String locale;

    @Column(name = "ISO_639_1_CODE", nullable = false)
    private
    @Getter
    @Setter
    String iso639_1Code;

    @Column(name = "NAME", nullable = false)
    private
    @Getter
    @Setter
    String name;

    @Column(name = "PHONE_CODE", nullable = false, updatable = false)
    private
    @Getter
    @Setter
    String phoneCode;

    @Column(name = "ICON", nullable = false, updatable = false)
    private
    @Getter
    @Setter
    String icon;

    @Column(name = "IS_SUPPORTED", nullable = false)
    private
    @Getter
    @Setter
    String isSupported;

    @Column(name = "IS_DEFAULT", nullable = false)
    private
    @Getter
    @Setter
    String isDefault;

    @Override
    public PrimaryKey getId() {
        return new PrimaryKey(getLocale());
    }

    @Override
    public void setId(PrimaryKey id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isIdSettingSupported() {
        return false;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = PROTECTED) //JPA requires it
    public static class PrimaryKey implements Serializable {

        private static final long serialVersionUID = 1L;

        private String locale;
    }
}
