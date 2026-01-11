// Configure Master + 2 Slaves

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

     @Bean
     @ConfigurationProperties(prefix = "spring.datasource.slave1")
     public DataSource slave1DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(DataSource masterDataSource,
                                        DataSource slave1DataSource,
                                        DataSource slave2DataSource) {
      
      Map<Object, Object> targetDataSources = new HashMap<>();
      targetDataSources.put("MASTER", masterDataSource);
      targetDataSources.put("SLAVE1", slave1DataSource);
      targetDataSources.put("SLAVE2", slave2DataSource);

      ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
      routingDataSource.setTargetDataSources(targetDataSources);
      routingDataSource.setDefaultTargetDataSource(masterDataSource);
      return routingDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
