import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PlayerTest {
	public static final List<String> SAMPLE_PLAYGROUND = Arrays.asList(
			"...0..1..0...",
			".0.........0.",
			"...0..0..0...",
			"0...........0",
			"...0.0.0.0...",
			".0.........0.",
			"...0.0.0.0...",
			"0...........0",
			"...0..0..0...",
			".0.........0.",
			"...0..0..0..."	
			);

	public static Player.Playground read(List<String> asList) {
		Player.Playground returned = null;
		int index = 0;
		for(String s : asList) {
			if(returned==null) {
				returned = new Player.Playground(s.length(), asList.size());
			}
			returned.readRow(s, index++);
		}
		return returned;
	}

	public static class PlaygroundTest {
		@Test
		public void can_build_a_valid_playground() {
			Player.Playground playground = read(Arrays.asList(
					"..",
					"0."));
			assertThat(playground.positions[0][1], is(Player.Content.BOX));
		}
	}

	public static class BomberTest {

		@Test
		public void can_find_nearest_box() {
			Player.Playground playground = read(SAMPLE_PLAYGROUND);
			Player.Bomber me = new Player.Bomber(0, 0, 0, 0, 0);
			Player.Location b = playground.findNearest(Player.Content.BOX, me);
			assertThat(b, is(new Player.Location(Player.Content.BOX, 1, 1)));
		}

		@Test
		public void sort_by_distance() {
			Player.Playground playground = read(SAMPLE_PLAYGROUND);
			Player.Bomber me = new Player.Bomber(0, 0, 0, 0, 0);
			Collection<Player.Location> b = playground.sortByDistanceTo(Player.Content.BOX, me, 1);
			assertThat(b, hasItems(
					new Player.Location(Player.Content.BOX, 1, 1)));
			b = playground.sortByDistanceTo(Player.Content.BOX, me, 2);
			assertThat(b, hasItems(
					new Player.Location(Player.Content.BOX, 1, 1),
					new Player.Location(Player.Content.BOX, 3, 0)
					));
		}

		@Test
		public void can_attack_nearest_box() {
			Player.Playground playground = read(SAMPLE_PLAYGROUND);
			assertThat(playerAttack(playground, 0, 0), is("MOVE 1 1"));
			assertThat(playerAttack(playground, 1, 0), is("BOMB 1 1"));
			// Now bomb has been dropped, it's time to evade !
			assertThat(playerAttack(playground, 1, 0, new int[][] {
				{ 1, 1 }
			}), is("MOVE 3 0"));
			assertThat(playerAttack(playground, 2, 0), is("BOMB 3 0"));
		}
		
		@Test public void can_resolve_some_locations() {
			Player.Playground playground = read(SAMPLE_PLAYGROUND);
			Player.Bomber bomber = new Player.Bomber(0, 0, 0, 0, 3);
			Collection<Player.Location> boxes = playground.sortByDistanceTo(Player.Content.BOX, bomber, 2);
			assertThat(boxes.size(), is(2));
			Player.Location intersection = bomber.findDestination(playground, boxes);
			assertThat(intersection, is(new Player.Location(Player.Content.NOTHING, 1, 0)));
		}
		
		private String playerAttack(Player.Playground playground, int playerX, int playerY) {
			return playerAttack(playground, playerX, playerY, new int[0][]);
		}
		
		private String playerAttack(Player.Playground playground, int playerX, int playerY, int[][] bombs) {
			Player.Bomber b = new Player.Bomber(0,playerX, playerY, 1, 3);
			Collection<Player.Bomb> bombsList = new ArrayList<>();
			for (int i = 0; i < bombs.length; i++) {
				if(bombs[i].length==2) {
					bombsList.add(new Player.Bomb(0, bombs[i][0], bombs[i][1], 3, 0));
				}
			}
			return b.attackNearestBox(playground, bombsList, Collections.emptyList());
		}
	}
}
