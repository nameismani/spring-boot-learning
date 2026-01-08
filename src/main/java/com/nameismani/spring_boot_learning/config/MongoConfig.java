package com.nameismani.spring_boot_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;

@Configuration
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoMappingContext context,
            MongoCustomConversions conversions) {

        // Correct way to create MappingMongoConverter using DbRefResolver
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);

        // Use the new constructor that requires DbRefResolver
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);

        // Set custom conversions and disable the _class field
        converter.setCustomConversions(conversions);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));  // Disables _class field

        return converter;
    }
}
