package org.agoncal.application.petstore;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class BaseServiceIT {

    protected static JavaArchive baseDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(BaseServiceIT.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}