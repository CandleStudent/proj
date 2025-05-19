package ru.delivery;//package ru.delivery;
//
//import org.springframework.context.ApplicationContextInitializer;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ConfigurableApplicationContext;
//
//@Testcontainers
//class TestContainerConfiguration implements
//    ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//  @Container
//  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
//      .withDatabaseName("testdb")
//      .withUsername("testuser")
//      .withPassword("testpass");
//
//  static {
//    POSTGRES.start(); // Запускаем контейнер один раз перед всеми тестами
//  }
//
//  @Override
//  public void initialize(ConfigurableApplicationContext applicationContext) {
//    TestPropertyValues.of(
//        "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
//        "spring.datasource.username=" + POSTGRES.getUsername(),
//        "spring.datasource.password=" + POSTGRES.getPassword(),
//        "spring.datasource.driver-class-name=org.postgresql.Driver",
//        "spring.liquibase.enabled=true",
//        "spring.liquibase.url=" + POSTGRES.getJdbcUrl(),
//        "spring.liquibase.user=" + POSTGRES.getUsername(),
//        "spring.liquibase.password=" + POSTGRES.getPassword()
//    ).applyTo(applicationContext.getEnvironment());
//  }
//}
