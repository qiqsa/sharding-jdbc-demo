package com.sharding;

import com.sharding.algorithm.HintShardingAlgorithmImpl;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.HintShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ShardingJdbcDemo {

    public static Connection getConn() throws SQLException {
        Map<String, DataSource> dataSourceMap = createDataSourceMap();
        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("ds${0}.t_order_${0..1}");

        //配置item表规则
        TableRuleConfiguration orderTableRuleConfigI = new TableRuleConfiguration();
        orderTableRuleConfigI.setLogicTable("t_order_item");
        orderTableRuleConfigI.setActualDataNodes("ds${0..1}.t_order_item_${0..1}");

        //配置golbalTable
        TableRuleConfiguration orderTableRuleConfigg = new TableRuleConfiguration();
        orderTableRuleConfigg.setLogicTable("globalTable");
        orderTableRuleConfigg.setActualDataNodes("ds${0}.globalTable_${0}");

        // 配置分库 + 分表策略
       // orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2+10}"));
        orderTableRuleConfigI.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_item_${order_id % 2}"));
        //自定义路由算法
       // orderTableRuleConfigI.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id",new PreciseModuloShardingTableAlgorithm()));
       //hint强制路由算法
       // orderTableRuleConfigI.setTableShardingStrategyConfig(new HintShardingStrategyConfiguration(new HintShardingAlgorithmImpl()));

        //无分库分表

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfigI);
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfigg);
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new HintShardingStrategyConfiguration(new HintShardingAlgorithmImpl()));

       // shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");

        // 省略配置order_item表规则...
        // ...

        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());
        return dataSource.getConnection();
    }


    static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap();
        result.put("ds0", createDataSource("ds_0"));
        result.put("ds1", createDataSource("ds_1"));
        return result;
    }

    public static DataSource createDataSource(final String dataSourceName) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s?useCursorFetch=true", dataSourceName));
        result.setUsername("root");
        result.setPassword("root");
        return result;
    }
}
