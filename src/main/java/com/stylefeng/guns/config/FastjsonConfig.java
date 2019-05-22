package com.stylefeng.guns.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * fastjson配置类
 *
 * @author fengshuonan
 * @date 2017-05-23 22:56
 */
@Configuration
public class FastjsonConfig {

    @Bean
    public FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat, //结果是否格式化,不添加默认为false
                SerializerFeature.WriteNullStringAsEmpty,//字符类型字段如果为null,输出为”“,而非null
                SerializerFeature.WriteMapNullValue, //是否输出值为null的字段,默认为false
                SerializerFeature.WriteNullBooleanAsFalse,  //Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullListAsEmpty   //List字段如果为null,输出为[],而非null
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        ValueFilter valueFilter = new ValueFilter() {
            public Object process(Object o, String s, Object o1) {
                if (null == o1) {
                    o1 = "";
                }
                return o1;
            }
        };
        fastJsonConfig.setSerializeFilters(valueFilter);
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }

}
