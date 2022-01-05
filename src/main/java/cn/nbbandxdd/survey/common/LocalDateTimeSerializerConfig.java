package cn.nbbandxdd.survey.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>LocalDateTime序列化配置。
 *
 * @author howcurious
 */
@Configuration
public class LocalDateTimeSerializerConfig {

    /**
     * <p>jackson2ObjectMapperBuilderCustomizer。
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> {

            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

    /**
     * <p>LocalDateTime序列化。
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        /**
         * <p>serialize。
         *
         * @param value value
         * @param gen gen
         * @param serializers serializers
         * @throws IOException IOException
         */
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            if (value != null) {

                long timestamp = value.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }

    /**
     * <p>LocalDateTime反序列化。
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        /**
         * <p>deserialize。
         *
         * @param p p
         * @param ctxt ctxt
         * @return LocalDateTime
         * @throws IOException IOException
         */
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            long timestamp = p.getValueAsLong();
            if (0 < timestamp) {

                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Shanghai"));
            } else {

                return null;
            }
        }
    }
}
