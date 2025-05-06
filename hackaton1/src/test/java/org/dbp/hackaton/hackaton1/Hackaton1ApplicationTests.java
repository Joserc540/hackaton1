package org.dbp.hackaton.hackaton1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Hackaton1ApplicationTests {

	@Test
	void contextLoads() {
	}

}
