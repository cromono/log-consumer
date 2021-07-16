package gabia.logConsumer.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Getter
@Table(name = "cron_job")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronJob {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "cron_job_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "cron_name")
    @NotNull
    private String cronName;

    @Column(name = "cron_expr")
    @NotNull
    private String cronExpr;

    @Temporal(TemporalType.TIME)
    private Date minStartTime;
    @Temporal(TemporalType.TIME)
    private Date maxEndTime;

    @Column(name = "server_ip")
    private String server;
}
