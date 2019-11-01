package com.sharding.example.config;

import com.sharding.example.type.ShardingType;
import io.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * add by qiqs
 */
public class YamlConfigurationExample {

    private static ShardingType type = ShardingType.SHARDING_DATABASES;

    public static DataSource getDataSource() throws IOException, SQLException {
        return YamlShardingDataSourceFactory.createDataSource(getYmlFile());
    }

    private static File getYmlFile() {
        String result;
        switch (type) {
            case SHARDING_TABLES:
                result = "/META-INF/sharding-tables.yaml";
                break;
            case SHARDING_DATABASES:
                result = "/META-INF/sharding-databases.yaml";
                break;
            case SHARDING_DATABASES_AND_TABLES:
                result = "/META-INF/sharding-database-tables.yamlx";
                break;
            default:
                throw new UnsupportedOperationException(type.name());
        }
        return new File(YamlConfigurationExample.class.getResource(result).getFile());
    }
}
