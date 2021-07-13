package gabia.logConsumer.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notice_subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeSubscription {

    @Id
    @Column(name = "notice_subscription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rcv_user_account")
    private String rcvUserAccount;

    @Column(name = "create_user_account")
    private String createUserAccount;

    @Column(name = "cron_job_id")
    private UUID cronJobId;
}
