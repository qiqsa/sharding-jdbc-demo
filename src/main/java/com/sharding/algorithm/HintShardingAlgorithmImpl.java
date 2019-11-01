package com.sharding.algorithm;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;

import java.util.*;

public class HintShardingAlgorithmImpl implements HintShardingAlgorithm {
    public Collection<String> doSharding(Collection<String> collection, ShardingValue shardingValue) {
        ListShardingValue value = (ListShardingValue) shardingValue;
        List result = new ArrayList();
        Iterator it = value.getValues().iterator();
        while(it.hasNext()){
            String sh = (String) it.next();
            if(!collection.contains(sh)){
                throw new RuntimeException("cant't find sharding dataSource ---->"+sh);
            }
            result.add(sh);
        }
        return result;
    }
}
