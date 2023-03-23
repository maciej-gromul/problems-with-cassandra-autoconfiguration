package com.example.sleuthcassandraduplicates;

import reactor.core.publisher.Mono;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

@Service
public class TestService {
	private final SomeModelRepository repository;

	public TestService(SomeModelRepository repository) {
		this.repository = repository;
	}

	@NewSpan("GET")
	public Mono<String> get(@SpanTag("id") Integer id) {
		return repository.findById(id).map(SomeModel::getValue);
	}

	@NewSpan("UPDATE")
	public Mono<String> update(@SpanTag("id") Integer id, @SpanTag("value") String value) {
		return repository.save(SomeModel.builder().id(id).value(value).build()).map(SomeModel::getValue);
	}

	@NewSpan("Return one")
	public Mono<Integer> returnOne(){
		return Mono.just(1);
	}
}
