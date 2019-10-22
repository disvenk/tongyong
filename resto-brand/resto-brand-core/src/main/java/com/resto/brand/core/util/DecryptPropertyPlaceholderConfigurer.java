package com.resto.brand.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件解密类
 *
 * @author yeq
 */
public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    /**
     * 配置文件属性值
     */
    private Map<String, String> properties;

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        if (props == null || props.size() < 1) {
            return;
        }

        properties = new HashMap<String, String>();

        String keyStr = null;
        for (Object key : props.keySet()) {
            if (key == null) {
                continue;
            }
            keyStr = key.toString();
            properties.put(keyStr, props.getProperty(keyStr));
        }
    }

    /**
     * 获取属性值
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return (properties == null || properties.size() < 1) ? null : properties.get(key);
    }

    protected String resolvePlaceholder(String placeholder, Properties props) {
        String str = props.getProperty(placeholder);

        if (placeholder == null || StringUtils.isEmpty(str)) {
            return str;
        }

        if (!placeholder.startsWith("jdbc.")) {
            return str;
        }

        if (placeholder.endsWith(".username")) {
            str = Encrypter.decrypt(str);
        } else if (placeholder.endsWith(".password")) {
            str = Encrypter.decrypt(str);
        }

        return str;
    }
}
