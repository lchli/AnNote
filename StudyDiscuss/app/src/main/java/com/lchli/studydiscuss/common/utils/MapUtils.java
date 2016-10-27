package com.lchli.studydiscuss.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lchli on 2016/8/22.
 */

public class MapUtils {

    public static Map<String, String> stringMap() {
        return new HashMap<>();
    }

    public static <K, V> String join(Map<K, V> map, String seprator) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        for (Map.Entry<K, V> entry : entrySet) {
            stringBuilder.append(entry.getKey() + "=" + entry.getValue()).append(seprator);
        }
        int sepIndex = stringBuilder.lastIndexOf(seprator);
        return stringBuilder.toString().substring(0, sepIndex);
    }
}
