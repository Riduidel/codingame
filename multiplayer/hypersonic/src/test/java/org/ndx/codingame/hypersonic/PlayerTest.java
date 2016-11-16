package org.ndx.codingame.hypersonic;
import java.util.Collection;

public class PlayerTest {
	public static Playfield read(Collection<String> rows) {
		Playfield returned = null;
		int rowIndex = 0;
		for (String string : rows) {
			if(returned==null) {
				returned = new Playfield(string.length(), rows.size());
			}
			returned.readRow(string, rowIndex++);
		}
		return returned;
	}
}
