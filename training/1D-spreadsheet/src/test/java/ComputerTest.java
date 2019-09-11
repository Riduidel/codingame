

import static org.assertj.core.api.Assertions.assertThat;

import java.rmi.server.Operation;
import java.util.List;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.stateful.Action;
import net.jqwik.api.stateful.ActionSequence;


public class ComputerTest {
	@Provide
	Arbitrary<String> operationType() {
		Operations[] OPS = Operations.values();
		return Arbitraries.randomValue(random -> OPS[random.nextInt(OPS.length)].toString());
	}

	@Provide
	Arbitrary<String> first() {
		return Arbitraries.oneOf(
				Arbitraries.integers().between(-10_000, 10_000).map(n -> Integer.toString(n)));
	}

	@Provide
	Arbitrary<String> second() {
		return Arbitraries.oneOf(Arbitraries.integers().between(-10_000, 10_000).map(n -> Integer.toString(n)),
				Arbitraries.constant("_"));
	}
	
	public static class AddOperation implements Action<Computer> {
		public final String operation;
		public final String first;
		public final String second;
		public AddOperation(String operation, String first, String second) {
			super();
			this.operation = operation;
			this.first = first;
			this.second = second;
		}
		@Override
		public Computer run(Computer model) {
			// Given
			Computer returned = new Computer();
			List<Row> operations = returned.operations;
			operations.addAll(model.operations);
			int before = operations.size();
			
			// When
			returned.addOperation(operation, first, second);
			
			// Then
			// Size has correctly grown
			assertThat(operations).isNotEmpty();
			assertThat(operations).hasSize(before+1);
			
			// Computation has to be verified in the final state,
			// because references may come to elements after the current one
			// And this is where property based testing touches its limit
			return returned;
		}
		@Override
		public String toString() {
			return "[operation=" + operation + ", first=" + first + ", second=" + second + "]";
		}
	}
	
	@Provide Arbitrary<ActionSequence<Computer>> allOperations() {
		return Arbitraries.sequences(oneOperation());
	}
	
	@Provide Arbitrary<Action<Computer>> oneOperation() {
		return operationType().flatMap(this::oneOperationWithType);
	}
	
	private Arbitrary<Action<Computer>> oneOperationWithType(String operationType) {
		return first().flatMap(first -> oneOperationWithTypeAndFirst(operationType, first));
	}
	
	private Arbitrary<Action<Computer>> oneOperationWithTypeAndFirst(String operationType, String first) {
		return second().flatMap(second -> 
			Arbitraries.create(() -> new AddOperation(operationType, first, second)));
	}

	@Property
	void validate_computer(@ForAll("allOperations") ActionSequence<Computer> actions) {
		Computer c = actions.run(new Computer());
		List<Integer> results = c.compute();
		List<Row> operations = c.operations;
		assertThat(results).hasSameSizeAs(operations);
	}

}
