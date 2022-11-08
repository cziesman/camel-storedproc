package com.redhat.camel.storedproc.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Converter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ToStringBean {

    @Converter
    public String convert(Object[] values) {

        if (values == null || values.length != 1) {
            return null;
        }

        Map<String, List<Map<String, String>>> map = (Map<String, List<Map<String, String>>>) values[0];

        Optional<List<Map<String, String>>> firstValue = map.values().stream().findFirst();
        if (firstValue.isEmpty()) {
            return null;
        }

        List<Map<String, String>> mapList = firstValue.get();
        if (mapList.size() != 1) {
            return null;
        }

        Map<String, String> resultMap = mapList.get(0);
        if (resultMap.size() != 1) {
            return null;
        }

        return resultMap.values().stream().findFirst().get();
    }
}
