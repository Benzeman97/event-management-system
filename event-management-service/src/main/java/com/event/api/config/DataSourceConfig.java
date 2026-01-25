// Configure Master + 2 Slaves

@Configuration
public class DataSourceConfig {

    // ---------- MASTER ----------
    @Bean
    @ConfigurationProperties("spring.datasource.master.hikari")
    public HikariDataSource masterDataSource() {
        return new HikariDataSource();
    }

    // ---------- SLAVE 1 ----------
    @Bean
    @ConfigurationProperties("spring.datasource.slave1.hikari")
    public HikariDataSource slave1DataSource() {
        return new HikariDataSource();
    }

    // ---------- SLAVE 2 ----------
    @Bean
    @ConfigurationProperties("spring.datasource.slave2.hikari")
    public HikariDataSource slave2DataSource() {
        return new HikariDataSource();
    }

    // ---------- ROUTING ----------
    @Bean
    public DataSource routingDataSource(
            @Qualifier("masterDataSource") DataSource master,
            @Qualifier("slave1DataSource") DataSource slave1,
            @Qualifier("slave2DataSource") DataSource slave2) {

        Map<Object, Object> targets = new HashMap<>();
        targets.put(DataSourceType.MASTER, master);
        targets.put(DataSourceType.SLAVE1, slave1);
        targets.put(DataSourceType.SLAVE2, slave2);

        ReplicationRoutingDataSource routing = new ReplicationRoutingDataSource();
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(master);
        routing.afterPropertiesSet();

        return routing;
    }

    // ---------- IMPORTANT ----------
    // Prevents early connection selection
    @Bean
    @Primary
    public DataSource dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    // ---------- TX MANAGER ----------
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
