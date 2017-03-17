package org.ndx.codingame.gaming.tounittest;

public class ToUnitTestStringBuilder {
	private final String methodPrefix;
	public String build(final ToUnitTestFiller filler) {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"@Test public void ").append(methodPrefix).append("_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(filler.build());
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"}\n\n");
		return returned.toString();
	}
	public ToUnitTestStringBuilder(final String methodPrefix) {
		super();
		this.methodPrefix = methodPrefix;
	}
}
