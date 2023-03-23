package com.example.sleuthcassandraduplicates;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

import brave.sampler.Sampler;
import lombok.NonNull;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

@Configuration
@EnableConfigurationProperties(CassandraProperties.class)
public class ConfigureCassandra {

  @Bean
  public CqlSessionFactoryBean createCqlSessionFactory(CassandraProperties cassandraProperties) {
    var contactPoints =
        cassandraProperties.getContactPoints().stream()
            .map(host -> InetSocketAddress.createUnresolved(host, cassandraProperties.getPort()))
            .collect(Collectors.toList());

    CqlSessionFactoryBean factory = new CqlSessionFactoryBean();
    factory.setContactPoints(contactPoints);
    factory.setKeyspaceCreations(getKeyspaceCreations(cassandraProperties));
    factory.setKeyspaceName(cassandraProperties.getKeyspaceName());
    factory.setLocalDatacenter(cassandraProperties.getLocalDatacenter());
    factory.setPort(cassandraProperties.getPort());
    factory.setUsername(cassandraProperties.getUsername());
    factory.setPassword(cassandraProperties.getPassword());

    return factory;
  }

  @NonNull
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations(
      CassandraProperties cassandraProperties
  ) {
    return List.of(
        CreateKeyspaceSpecification.createKeyspace(cassandraProperties.getKeyspaceName())
            .ifNotExists()
            .withSimpleReplication()
    );
  }

  @Bean
  Sampler defaultSampler() {
    return Sampler.ALWAYS_SAMPLE;
  }
}
