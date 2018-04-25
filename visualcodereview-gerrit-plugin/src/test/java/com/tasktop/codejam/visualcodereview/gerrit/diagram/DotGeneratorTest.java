package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.io.Resources;
import com.tasktop.codejam.visualcodereview.gerrit.AbstractRepositoryTest;

public class DotGeneratorTest extends AbstractRepositoryTest {

	private DotGenerator generator = new DotGenerator();

	@Test
	public void generate() throws IOException {
		String dot = generator.generate(repository, "ced86368a80bf33aecb467a3a3034a5e796ddd21");
		assertThat(dot).isEqualTo(expectedDot());
	}

	private String expectedDot() throws IOException {
		return Resources.toString(DotGeneratorTest.class.getResource("expected.dot"), StandardCharsets.UTF_8);
	}

}
