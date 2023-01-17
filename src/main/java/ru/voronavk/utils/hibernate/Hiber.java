package ru.voronavk.utils.hibernate;

import ru.voronavk.entities.*;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.cfg.AvailableSettings.*;

public class Hiber {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void shutdown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public static void build() {
        entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                archiverPersistenceUnitInfo(),
                new HashMap<String, Object>() {{
                    put(JPA_JDBC_DRIVER, "org.postgresql.Driver");
                    //put(JPA_JDBC_URL, JPA_JDBC_URL);
                    put(DIALECT, PostgreSQL95Dialect.class);
                    put(SHOW_SQL, true);
                    put(QUERY_STARTUP_CHECKING, false);
                    put(GENERATE_STATISTICS, false);
                    put(USE_REFLECTION_OPTIMIZER, false);
                    put(HBM2DDL_AUTO, "create");
                    put(USE_SECOND_LEVEL_CACHE, false);
                    put(USE_QUERY_CACHE, false);
                    put(USE_STRUCTURED_CACHE, false);
                    put(STATEMENT_BATCH_SIZE, 20);
                    put(URL, "jdbc:postgresql://127.0.0.1:5432/botsdb");
                    put(USER, "equipment");
                    put(PASS, "password");
                }}
        );

        entityManager = entityManagerFactory.createEntityManager();
    }

    private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "Hiber";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<java.net.URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return entityClassNames();
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return hibernateProperties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }

    private static Properties hibernateProperties() {
        return new Properties();
    }

    private static DataSource dataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("botsdb");
        dataSource.setUser("equipment");
        dataSource.setPassword("password");
        return dataSource;
    }

    private static List<String> entityClassNames() {
        Class<?>[] entities = new Class<?>[]{
                Person.class,
                PersonState.class,
                MultiOrder.class,
                PrintPhotoMultiOrder.class,
                PrintPhotoOrder.class,
                Phase.class,
                FormatPhoto.class
        };
        return Arrays.stream(entities).map(Class::getName).collect(Collectors.toList());
    }
}
