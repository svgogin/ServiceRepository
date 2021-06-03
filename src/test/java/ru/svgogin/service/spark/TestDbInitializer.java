package ru.svgogin.service.spark;

import java.sql.Connection;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class TestDbInitializer {
  public static void init(Connection connection) {
    Flyway.configure()
        .encoding("UTF-8")
        .table("flyway_schema_history")
        .schemas("public")
        .baselineOnMigrate(true)
        .baselineVersion("1")
        .dataSource(new SingleConnectionDataSource(connection, true))
        .locations("classpath:migrations")
        .load()
        .migrate();
  }
}
