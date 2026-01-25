// Update Routing Logic for Multiple Slaves

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

          // List of slaves
    private static final List<DataSourceType> SLAVES = List.of(
            DataSourceType.SLAVE1,
            DataSourceType.SLAVE2
    );

      // ThreadLocal to stick a slave per transaction/thread
    private static final ThreadLocal<DataSourceType> SELECTED_SLAVE = new ThreadLocal<>();

      private final Random random = new Random();

      // Every time a database connection is requested, Spring calls this method to decide which actual DataSource (MASTER or SLAVE) to use.

     @Override
    protected Object determineCurrentLookupKey() {

        // Writes → always go to MASTER
        if (!ReplicationContext.isReadOnly()) {
            SELECTED_SLAVE.remove();  // Remove sticky slave for write transaction
            return DataSourceType.MASTER;
        }

        // Reads → pick one slave per transaction/thread
        if (SELECTED_SLAVE.get() == null) {
            SELECTED_SLAVE.set(SLAVES.get(random.nextInt(SLAVES.size())));
        }

        return SELECTED_SLAVE.get();
    }


}
