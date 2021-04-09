package vip.itchen.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisToolUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 模糊匹配查找Key数据
     * 慎用，会有性能问题
     */
    public Collection<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * Key删除
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * Key批量删除
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /* ----------- string --------- */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    public <T> T get(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return parseJson(value, clazz);
    }

    public <T> List<T> multiGet(Collection<String> keys, Class<T> clazz) {
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        return parseJsonList(values, clazz);
    }

    public <T> void set(String key, T obj, Long timeout, TimeUnit unit) {
        if (obj == null) {
            return;
        }

        String value = toJson(obj);
        if (timeout != null) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
        } else {
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    public <T> T getAndSet(String key, T obj, Class<T> clazz) {
        if (obj == null) {
            return get(key, clazz);
        }

        String value = stringRedisTemplate.opsForValue().getAndSet(key, toJson(obj));
        return parseJson(value, clazz);
    }

    public int decrement(String key, int delta) {
        Long value = stringRedisTemplate.opsForValue().increment(key, -delta);
        return value == null ? 0 : value.intValue();
    }

    public int increment(String key, int delta) {
        Long value = stringRedisTemplate.opsForValue().increment(key, delta);
        return null == value ? 0 : value.intValue();
    }

    public int increment(String key, int delta, Long timeout, TimeUnit timeUnit) {
        int value = increment(key, delta);
        stringRedisTemplate.expire(key, timeout, timeUnit);
        return value;
    }

    /* ----------- list --------- */
    public int size(String key) {
        Long size = stringRedisTemplate.opsForList().size(key);
        return size == null ? 0 : size.intValue();
    }

    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        List<String> list = stringRedisTemplate.opsForList().range(key, start, end);
        return parseJsonList(list, clazz);
    }

    public void rightPushAll(String key, Collection<?> values, Long timeout,
                                    TimeUnit unit) {
        if (values == null || values.isEmpty()) {
            return;
        }

        stringRedisTemplate.opsForList().rightPushAll(key, toJsonList(values));
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, unit);
        }
    }

    public <T> void leftPush(String key, T obj) {
        if (obj == null) {
            return;
        }

        stringRedisTemplate.opsForList().leftPush(key, toJson(obj));
    }

    public <T> T leftPop(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForList().leftPop(key);
        return parseJson(value, clazz);
    }

    public <T> T rightPop(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForList().rightPop(key);
        return parseJson(value, clazz);
    }

    public void remove(String key, int count, Object obj) {
        if (obj == null) {
            return;
        }

        stringRedisTemplate.opsForList().remove(key, count, toJson(obj));
    }

    /* ----------- zset --------- */
    public int zcard(String key) {
        Long size = stringRedisTemplate.opsForZSet().zCard(key);
        return size == null ? 0 : size.intValue();
    }

    public <T> List<T> zrange(String key, long start, long end, Class<T> clazz) {
        Set<String> set = stringRedisTemplate.opsForZSet().range(key, start, end);
        return parseJsonList(setToList(set), clazz);
    }

    private List<String> setToList(Set<String> set) {
        if (set == null) {
            return null;
        }
        return new ArrayList<String>(set);
    }

    public void zadd(String key, Object obj, double score) {
        if (obj == null) {
            return;
        }
        stringRedisTemplate.opsForZSet().add(key, toJson(obj), score);
    }

    public void zaddAll(String key, List<ZSetOperations.TypedTuple<?>> tupleList, Long timeout, TimeUnit unit) {
        if (tupleList == null || tupleList.isEmpty()) {
            return;
        }

        Set<ZSetOperations.TypedTuple<String>> tupleSet = toTupleSet(tupleList);
        stringRedisTemplate.opsForZSet().add(key, tupleSet);
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, unit);
        }
    }

    private Set<ZSetOperations.TypedTuple<String>> toTupleSet(List<ZSetOperations.TypedTuple<?>> tupleList) {
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new LinkedHashSet<>();
        for (ZSetOperations.TypedTuple<?> t : tupleList) {
            tupleSet.add(new DefaultTypedTuple<>(toJson(t.getValue()), t.getScore()));
        }
        return tupleSet;
    }

    public void zrem(String key, Object obj) {
        if (obj == null) {
            return;
        }
        stringRedisTemplate.opsForZSet().remove(key, toJson(obj));
    }

    public void unionStore(String destKey, Collection<String> keys, Long timeout, TimeUnit unit) {
        if (keys == null || keys.isEmpty()) {
            return;
        }

        Object[] keyArr = keys.toArray();
        String key = (String) keyArr[0];

        Collection<String> otherKeys = new ArrayList<>(keys.size() - 1);
        for (int i = 1; i < keyArr.length; i++) {
            otherKeys.add((String) keyArr[i]);
        }

        stringRedisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
        if (timeout != null) {
            stringRedisTemplate.expire(destKey, timeout, unit);
        }
    }

    public static String toJson(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return JSON.toJSONString(obj, SerializerFeature.SortField);
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static List<String> toJsonList(Collection<?> values) {
        if (values == null) {
            return null;
        }

        List<String> result = new ArrayList<>();
        for (Object obj : values) {
            result.add(toJson(obj));
        }
        return result;
    }

    public static <T> List<T> parseJsonList(List<String> list, Class<T> clazz) {
        if (list == null) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (String s : list) {
            result.add(parseJson(s, clazz));
        }
        return result;
    }
}
