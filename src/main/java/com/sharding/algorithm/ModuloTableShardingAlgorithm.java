package com.sharding.algorithm;

import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModuloTableShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    public Collection<String> doSharding(Collection<String> collection, Collection<ShardingValue> values) {

        List<String> result = new ArrayList<String>();
        result.add("1");
        return result;
    }
}
