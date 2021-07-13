package gabia.logConsumer.repository;

import gabia.logConsumer.entity.WebhookSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {

    List<WebhookSubscription> findByNoticeSubscriptionId(Long id);

    List<WebhookSubscription> findByNoticeSubscriptionIdIn(List<Long> id);
}
