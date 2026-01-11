// Update Routing Logic for Multiple Slaves

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

      private final Random random = new Random();

      // Every time a database connection is requested, Spring calls this method to decide which actual DataSource (MASTER or SLAVE) to use.

    @Override
    protected Object determineCurrentLookupKey() {

        //Read-only transactions → randomly go to SLAVE1 or SLAVE2
        if (ReplicationContext.isReadOnly()) {
            // Randomly choose one of the two slaves
            return random.nextBoolean() ? DataSourceType.SLAVE1 : DataSourceType.SLAVE2;
        } else {
            return DataSourceType.MASTER;
            // Writes → always go to MASTER
        }
    }

}
