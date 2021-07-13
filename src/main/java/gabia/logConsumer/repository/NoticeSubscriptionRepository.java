package gabia.logConsumer.repository;

import gabia.logConsumer.entity.NoticeSubscription;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeSubscriptionRepository extends JpaRepository<NoticeSubscription, Long> {

    List<NoticeSubscription> findByCronJobId(UUID cronJobId);
}
