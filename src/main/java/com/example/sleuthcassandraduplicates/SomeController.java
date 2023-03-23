package com.example.sleuthcassandraduplicates;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SomeController {
	private final TestService testService;

	public SomeController(TestService testService) {
		this.testService = testService;
	}

	@GetMapping("/test")
	public Mono<String> test() {
		return testService.returnOne()
				.flatMap(testService::get)
				.doOnNext(str -> log.info("Found {}", str))
				.switchIfEmpty(Mono.defer(() -> Mono.just("Unknown").doOnNext(i -> log.info("Not found"))));
	}

	@GetMapping("/test/{id}")
	public Mono<String> test2(@PathVariable Integer id) {
		return testService.returnOne()
				.thenReturn(id)
				.flatMap(i -> testService.update(i, "foo"));
	}
}
