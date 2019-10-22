package com.resto.wechat.web.util;/**
 * Created by user on 2016/3/4.
 */

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * JSONUtils
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class JSONUtils {

    private static ObjectMapper mapper = null;

    static {

        if (mapper != null) {
            mapper.getSerializationConfig().set(Feature.WRITE_NULL_PROPERTIES, false);
            mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        } else {
            mapper = new ObjectMapper();
            mapper.getSerializationConfig().set(Feature.WRITE_NULL_PROPERTIES, false);
            mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

    }

    /**
     * 解析json
     *
     * @param json
     * @param valueType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T parseJson(String json, Class<T> valueType)
            throws JsonParseException, JsonMappingException, IOException {

        T t = null;

        if (json != null && !"".contentEquals(json.trim()) && valueType != null) {

            t = mapper.readValue(json, valueType);

        }

        return t;
    }

    /**
     * 解析json
     *
     * @param json
     * @param typeRef
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T parseJson(String json, TypeReference<T> typeRef)
            throws JsonParseException, JsonMappingException, IOException {

        T t = null;

        if (json != null && !"".contentEquals(json.trim()) && typeRef != null) {

            t = (T) mapper.readValue(json, typeRef);

        }

        return t;
    }

    /**
     * 生成json
     *
     * @param list
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> String writeJson(List<T> list)
            throws JsonGenerationException, JsonMappingException, IOException {

        return mapper.writeValueAsString(list);

    }

    /**
     * 生成json
     *
     * @param t
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> String writeJson(T t) throws JsonGenerationException,
            JsonMappingException, IOException {

        return mapper.writeValueAsString(t);

    }

    public static void main(String[] args) throws IOException {
        JSONObject j = new JSONObject();
        j.append("resultMSG", "用户登陆成功");
        j.append("resultMSG", "test");
        System.out.print(j.toString());
    }
}
