package gabia.logConsumer.repository;

import gabia.logConsumer.entity.CronJob;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronJobRepository extends JpaRepository<CronJob, UUID> {

}
