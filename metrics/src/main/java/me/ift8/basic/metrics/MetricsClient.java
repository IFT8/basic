package me.ift8.basic.metrics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuye on 09/06/2017.
 */
@Slf4j
@AllArgsConstructor
public class MetricsClient {

    private static InfluxDB influxDB;

    private String url;
    private String username;
    private String password;
    private String database;

    @PostConstruct
    private void init() {
        influxDB = InfluxDBFactory.connect(url, username, password)
                .enableGzip()
                .enableBatch(1000, 100, TimeUnit.MILLISECONDS);

        needInit();
    }

    @PreDestroy
    public void dispose() {
        influxDB.close();
    }

    private boolean needInit() {
        long c = influxDB.describeDatabases().stream().filter(db -> db.equals(database)).count();
        if (c == 0) {
            influxDB.createDatabase(database);
            return true;
        } else {
            return false;
        }
    }

    public void write(String measurement, long count, long time, Map<String, String> tags) {
        if (tags == null) {
            tags = new HashMap<>();
        }
        try {
            Point point = Point.measurement(measurement)
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("during", time)
                    .addField("count", count)
                    .tag(tags)
                    .build();
            influxDB.write(database, "autogen", point);
        } catch (Exception ex) {
            log.error("打点到InfluxDb出现异常 measurement:【{}】count:【】time:【{}】tags:【{}】", measurement, count, time, tags, ex);
        }
    }
}

