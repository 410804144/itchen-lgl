package vip.itchen.support.xss;

import cn.hutool.http.HTMLFilter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author alabimofa
 */
public class StringXssDeserializer extends JsonDeserializer<String> {

    private static final HTMLFilter FILTER =  new HTMLFilter();

    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String source = p.getText().trim();
        return FILTER.filter(source);
    }
}
