import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class UseCasesTest {
	private void assertComputationOf(String text, String expected) {
		Computer c = new Computer();
		for(String line : text.split("\n")) {
			String[] params = line.split(" ");
			c.addOperation(params[0], params[1], params[2]);
		}
		List<Integer> result = c.compute();
		List<Integer> expectedList = Arrays.stream(expected.split("\n")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
		assertThat(result).containsSequence(expectedList);
	}

	@Test
	void validate_computer() {
		assertComputationOf("VALUE 3 _\r\n" + 
				"ADD $0 4", "3\r\n" + 
						"7");
	}


}
