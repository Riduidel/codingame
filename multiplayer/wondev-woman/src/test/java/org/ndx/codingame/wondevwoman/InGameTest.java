package org.ndx.codingame.wondevwoman;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class InGameTest {
	// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT)
	// @Required(percentile99=PERCENTILE)
	@Test
	public void just_to_always_keep_assertj_in_imports() {
		assertThat(true).isNotEqualTo(false);
	}
}
