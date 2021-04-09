package vip.itchen.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import vip.itchen.support.xss.StringXssDeserializer;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.util.List;

/**
 * @author alabimofa
 * @date 2020/5/6
 * description: Json工具类
 */
public class JsonUtils {

    private static class JsonMapper extends ObjectMapper {
        JsonMapper(){
            super();
            // 反序列化时，忽略字符串中多余的属性
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 反序列化时，强制带小数的字段转为BigDecimal
            this.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            // 时间序列化为时间戳
            this.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // BigDecimal防止序列化为科学计数法
            this.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
            // Long以及BigInteger序列化为字符串
            SimpleModule module = new SimpleModule();
            module.addSerializer(Long.class, ToStringSerializer.instance);
            module.addSerializer(Long.TYPE, ToStringSerializer.instance);
            module.addSerializer(BigInteger.class, ToStringSerializer.instance);
            // 字符串反序列化时，强制xss过滤
            module.addDeserializer(String.class, new StringXssDeserializer());
            this.registerModule(module);
        }
    }

    private static class Holder {
        private final static ObjectMapper MAPPER = new JsonMapper();
    }

    public static ObjectMapper getInstance(){
        return Holder.MAPPER;
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return Holder.MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toBean(String json, Class<T> clazz) {
        return Holder.MAPPER.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> List<T> toList(String json, Class<T> clazz) {
        JavaType javaType = Holder.MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        return Holder.MAPPER.readValue(json, javaType);
    }
}
