package com.example.sleuthcassandraduplicates;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface SomeModelRepository extends ReactiveCassandraRepository<SomeModel, Integer> {
}
