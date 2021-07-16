package gabia.logConsumer.exception;

public class CronJobNotFoundException extends RuntimeException {

    public CronJobNotFoundException(String message) {
        super(message);
    }
}
