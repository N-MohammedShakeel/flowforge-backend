package com.ms.flowforge;

import org.springframework.boot.SpringApplication;

public class TestFlowforgeApplication {

	public static void main(String[] args) {
		SpringApplication.from(FlowforgeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
