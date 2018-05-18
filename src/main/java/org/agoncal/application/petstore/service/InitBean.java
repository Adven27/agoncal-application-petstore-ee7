package org.agoncal.application.petstore.service;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;
import java.sql.Connection;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class InitBean {
    private static final String STAGE = "";
    private static final String CHANGELOG_FILE = "db/changelog/db.changelog-master.xml";

    @Resource
    private DataSource ds;

    @PostConstruct
    protected void bootstrap() {
        try (Connection connection = ds.getConnection()) {
            Liquibase lb = new Liquibase(
                    CHANGELOG_FILE,
                    new ClassLoaderResourceAccessor(getClass().getClassLoader()),
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))
            );
            lb.validate();
            //lb.dropAll();
            lb.update(STAGE);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}