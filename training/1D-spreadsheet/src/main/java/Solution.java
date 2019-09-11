

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

enum Operations {
	VALUE, ADD, SUB, MULT;
}

class Value {
	final String text;
	Optional<Integer> value = Optional.empty();

	public Value(String t) {
		this.text = t;
	}

	public Integer get(List<Row> operations) {
		if(!value.isPresent()) {
			value = Optional.of(doGet(operations));
		}
		return value.get();

	}

	Integer doGet(List<Row> operations) {
		if(text.equals("_")) {
			return 0;
		} else if(text.startsWith("$")) {
			String op = text.substring(1);
			return operations.get(Integer.parseInt(op)).compute(operations);
		} else {
			return Integer.parseInt(text);
		}
	}

	@Override
	public String toString() {
		return text;
	}
}

class Row {

	Operations operation;
	Value first;
	Value second;

	Optional<Integer> result = Optional.empty();

	public Row(String operation, String arg1, String arg2) {
		this.operation = Operations.valueOf(operation);
		this.first = new Value(arg1);
		this.second = new Value(arg2);
	}

	@Override
	public String toString() {
		return "Operation [operation=" + operation + ", first=" + first + ", second=" + second + "]";
	}

	public Integer compute(List<Row> operations) {
		if(!result.isPresent()) {
			result = Optional.of(doCompute(operations));
		}
		return result.get();
	}

	Integer doCompute(List<Row> operations) {
		switch (this.operation) {
		case VALUE:
			return first.get(operations);
		case ADD:
			return first.get(operations) + second.get(operations);
		case SUB:
			return first.get(operations) - second.get(operations);
		case MULT:
			return first.get(operations) * second.get(operations);
		}
		throw new UnsupportedOperationException();
	}
}

class Computer {
	List<Row> operations = new ArrayList<>();

	public void addOperation(String operation, String arg1, String arg2) {
		operations.add(new Row(operation, arg1, arg2));
	}

	public List<Integer> compute() {
		return operations.stream().map(o -> o.compute(operations)).collect(Collectors.toList());
	}
}

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		Computer computer = new Computer();
		for (int i = 0; i < N; i++) {
			String operation = in.next();
			String arg1 = in.next();
			String arg2 = in.next();
			computer.addOperation(operation, arg1, arg2);
		}
		for (int i : computer.compute()) {

			// Write an action using System.out.println()
			// To debug: System.err.println("Debug messages...");

			System.out.println(i);
		}
	}
}