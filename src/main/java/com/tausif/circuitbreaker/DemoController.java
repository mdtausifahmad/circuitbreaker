package com.tausif.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

	Logger LOG = LoggerFactory.getLogger(DemoController.class);

	private CircuitBreakerFactory circuitBreakerFactory;
	private HttpBinService httpBin;

	public DemoController(CircuitBreakerFactory circuitBreakerFactory, HttpBinService httpBinService) {
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.httpBin = httpBinService;
	}

	@GetMapping("/get")
	public Map get() {
		return httpBin.get();
	}

	@GetMapping("/delay/{seconds}")
	public Map delay(@PathVariable int seconds) {
		return circuitBreakerFactory
				.create("delay")
				.run(httpBin.delaySuppplier(seconds), t -> {
			LOG.warn("delay call failed error", t);
			Map<String, String> fallback = new HashMap<>();
			fallback.put("hello", "world");
			return fallback;
		});
	}

}