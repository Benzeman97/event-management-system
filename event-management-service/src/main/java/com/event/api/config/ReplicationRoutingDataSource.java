// Update Routing Logic for Multiple Slaves

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

      private final Random random = new Random();

    @Override
    protected Object determineCurrentLookupKey() {

        //Read-only transactions → randomly go to SLAVE1 or SLAVE2
        if (ReplicationContext.isReadOnly()) {
            // Randomly choose one of the two slaves
            return random.nextBoolean() ? "SLAVE1" : "SLAVE2";
        } else {
            return "MASTER";
            // Writes → always go to MASTER
        }
    }

}
