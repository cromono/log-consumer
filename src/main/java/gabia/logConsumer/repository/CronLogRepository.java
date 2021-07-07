package gabia.logConsumer.repository;

import gabia.logConsumer.entity.CronLog;

public interface CronLogRepository {

    public CronLog save(CronLog cronLog);
}