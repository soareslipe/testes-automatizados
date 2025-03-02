package com.tests.Tests.Course;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
	@Test
	public void testSum() {
		Calculator calculator = new Calculator();
		Assertions
			.assertThat(calculator.sum(1, 2)).isEqualTo(3);
	}
}
