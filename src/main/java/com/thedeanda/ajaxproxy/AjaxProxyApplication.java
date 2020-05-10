package com.thedeanda.ajaxproxy;

import com.thedeanda.ajaxproxy.config.model.ServerConfig;
import com.thedeanda.ajaxproxy.db.ServerConfigDao;
import com.thedeanda.ajaxproxy.health.SampleHealthCheck;
import com.thedeanda.ajaxproxy.resources.ServerResource;
import com.thedeanda.ajaxproxy.service.ServerConfigService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AjaxProxyApplication extends Application<AjaxProxyConfiguration> {

    public static void main(String[] args) throws Exception {
        args = new String[]{"server", "config.yml"};
        new AjaxProxyApplication().run(args);
    }

    @Override
    public String getName() {
        return "AjaxProxy";
    }

    @Override
    public void initialize(final Bootstrap<AjaxProxyConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AjaxProxyConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AjaxProxyConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final AjaxProxyConfiguration config,
                    final Environment environment) {
        // TODO: implement application

        try {
            runLiquibase(config, environment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "datasource");
        // jdbi.installPlugin(new SqlObjectPlugin());


        //daos
        final ServerConfigDao serverConfigDao = jdbi.onDemand(ServerConfigDao.class);

        //services
        final ServerConfigService serverConfigService = new ServerConfigService(serverConfigDao);

        //resources
        final ServerResource resource = new ServerResource(serverConfigService, serverConfigDao);



        environment.healthChecks().register("sample", new SampleHealthCheck());
        environment.jersey().register(resource);
    }

    private void runLiquibase(final AjaxProxyConfiguration config, final Environment environment) throws Exception {
        DataSource dataSource = config.getDataSourceFactory().build(environment.metrics(), "mydatasource");

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        Liquibase liquibase = new liquibase.Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update(new Contexts(), new LabelExpression());
        liquibase.close();

    }
}
