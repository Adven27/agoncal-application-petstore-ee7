package it;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;

public class BaseIT {
    protected static WebArchive deployment() {
        return ((WebArchive) EmbeddedMaven.forProject("pom.xml")
                .useMaven3Version("3.3.9")
                .setGoals("package")
                .setQuiet()
                .setProfiles("E2E")
                .ignoreFailure()
                .build()
                .getDefaultBuiltArchive()).addClasses(DataSets.class, BaseIT.class);
    }
}