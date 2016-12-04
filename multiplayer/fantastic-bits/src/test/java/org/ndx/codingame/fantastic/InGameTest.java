package org.ndx.codingame.fantastic;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.Actions;
import org.ndx.codingame.fantastic.actions.ThrowTo;
import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.TeamStatus;

public class InGameTest {
	@Test public void do_not_aim_same_target() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(0);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 15000, 2250, 0, 0, 1, false, false));
			playing.add(new Wizard(3, 15000, 5250, 0, 0, 1, false, false));
			playing.add(new Snaffle(4, 5910, 1723, 0, 0));
			playing.add(new Snaffle(5, 10090, 5777, 0, 0));
			playing.add(new Snaffle(6, 4008, 2238, 0, 0));
			playing.add(new Snaffle(7, 11992, 5262, 0, 0));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 7450, 3750, 0, 0));
			playing.add(new Bludger(10, 8550, 3750, 0, 0));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 1000, 5250, 0, 0, 0, false, true));
			myTeam.add(new Wizard(1, 1000, 2250, 0, 0, 0, false, false));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(0)).isNotEqualTo(actions.get(1));
	}
	@Test public void can_find_actions_in_1480781999197() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(9);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 13739, 4878, 1015, 271, 1, false, false));
			playing.add(new Wizard(3, 11531, 5244, -331, -12, 1, false, false));
			playing.add(new Snaffle(4, 5910, 1723, 0, 0));
			playing.add(new Snaffle(5, 10090, 5777, 0, 0));
			playing.add(new Snaffle(6, 5553, 2018, 1044, 82));
			playing.add(new Snaffle(7, 9943, 5050, -778, -67));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 3577, 2686, -401, -226));
			playing.add(new Bludger(10, 12364, 4867, 324, 316));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 2106, 2700, -1158, -207, 0, false, false));
			myTeam.add(new Wizard(1, 4565, 1866, 302, -32, 0, false, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
	}
	@Test public void can_find_actions_in_1480787390621() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(27);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 7811, 3745, -299, -146, 1, false, false));
			playing.add(new Wizard(3, 2342, 4407, -609, -173, 1, false, false));
			playing.add(new Snaffle(4, 5911, 1961, 28, 17));
			playing.add(new Snaffle(5, 4776, 5352, -18, -2));
			playing.add(new Snaffle(6, 8526, 2447, 6, 2));
			playing.add(new Snaffle(7, 2315, 3643, -131, -40));
			playing.add(new Snaffle(8, 6811, 3722, -1048, -164));
			playing.add(new Bludger(9, 7084, 2925, 110, 27));
			playing.add(new Bludger(10, 4070, 4134, -397, -444));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 6969, 752, 43, -14, 0, false, true));
			myTeam.add(new Wizard(1, 5911, 1961, 28, 17, 0, false, false));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(0)).isNotEqualTo("ACCIO 4");
		assertThat(actions.get(1)).doesNotStartWith("ACCIO");
	}
	@Test public void can_find_actions_in_1480793421339() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(21);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 5160, 2712, 101, -144, 1, false, false));
			playing.add(new Wizard(3, 2999, 5714, -240, -117, 1, false, false));
			playing.add(new Snaffle(4, 6222, 2362, 87, -60));
			playing.add(new Snaffle(7, 1546, 4910, -96, -37));
			playing.add(new Bludger(9, 10513, 3118, 407, -173));
			playing.add(new Bludger(10, 11035, 3122, 441, -210));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 2953, 4052, -250, 130, 0, false, false));
			myTeam.add(new Wizard(1, 14671, 3510, 186, -51, 0, false, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(1)).doesNotStartWith("ACCIO");
	}

	@Test public void can_find_actions_in_1480794842241() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(0);
		Snaffle s7;
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 15000, 2250, 0, 0, 1, false, false));
			playing.add(new Wizard(3, 15000, 5250, 0, 0, 1, false, false));
			playing.add(new Snaffle(4, 5910, 1723, 0, 0));
			playing.add(new Snaffle(5, 10090, 5777, 0, 0));
			playing.add(new Snaffle(6, 4008, 2238, 0, 0));
			playing.add(s7 = new Snaffle(7, 11992, 5262, 0, 0));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 7450, 3750, 0, 0));
			playing.add(new Bludger(10, 8550, 3750, 0, 0));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 1000, 5250, 0, 0, 0, false, true));
			myTeam.add(new Wizard(1, 1000, 2250, 0, 0, 0, false, false));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(0)).isNotEqualTo(Actions.moveTo(s7));
	}
	@Test public void can_find_actions_in_1480797429586() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(8);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 10595, 1458, -180, -107, 1, false, false));
			playing.add(new Wizard(3, 3566, 4461, -291, -149, 1, false, false));
			playing.add(new Snaffle(4, 7183, 1534, 236, -78));
			playing.add(new Snaffle(5, 3968, 1744, 383, -548));
			playing.add(new Snaffle(6, 8715, 1736, -403, -74));
			playing.add(new Snaffle(7, 2184, 3603, -98, -30));
			playing.add(new Snaffle(8, 9385, 5866, 81, 185));
			playing.add(new Bludger(9, 10656, 4486, 341, 440));
			playing.add(new Bludger(10, 5717, 4068, -383, -194));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 7183, 1534, 236, -78, 0, true, false));
			myTeam.add(new Wizard(1, 9385, 5866, 81, 185, 0, true, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(1)).startsWith("THROW");
	}
	@Test public void can_find_actions_in_1480850751124() {
		final Status status = new Status();
			status.setTeam(1);
			status.setMagic(12);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(0, 702, 5813, 71, 64, 0, false, false));
			playing.add(new Wizard(1, 6452, 2193, 435, -5, 0, true, false));
			playing.add(new Snaffle(4, 6960, 5368, 0, 0));
			playing.add(new Snaffle(5, 9040, 2132, 0, 0));
			playing.add(new Snaffle(6, 6452, 2193, 435, -5));
			playing.add(new Snaffle(7, 9548, 5307, -435, 5));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 3193, 3805, -55, -256));
			playing.add(new Bludger(10, 13577, 3144, 182, 190));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(2, 9607, 1382, -410, -12, 1, false, false));
			myTeam.add(new Wizard(3, 9548, 5307, -435, 5, 1, true, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
	}
	@Test public void can_find_actions_in_1480861742428() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(2);
		Snaffle s4;
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 14802, 2613, -94, 173, 1, false, false));
			playing.add(new Wizard(3, 14665, 5491, -160, 115, 1, false, false));
			playing.add(s4 = new Snaffle(4, 3187, 679, 0, 0));
			playing.add(new Snaffle(5, 12813, 6821, 0, 0));
			playing.add(new Snaffle(6, 2115, 3202, 0, 0));
			playing.add(new Snaffle(7, 13885, 4298, 0, 0));
			playing.add(new Snaffle(8, 4966, 1827, 0, 0));
			playing.add(new Snaffle(9, 11034, 5673, 0, 0));
			playing.add(new Snaffle(10, 8000, 3750, 0, 0));
			playing.add(new Bludger(11, 7096, 3829, -209, 46));
			playing.add(new Bludger(12, 8904, 3671, 209, -46));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(0, 1198, 4887, 94, -173, 0, false, false));
			myTeam.add(new Wizard(1, 1335, 2009, 160, -115, 0, false, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
		assertThat(actions.get(1)).isEqualTo(Actions.moveTo(s4));
	}

	@Test public void can_find_actions_in_1480869369850() {
		final Status status = new Status();
			status.setTeam(1);
			status.setMagic(5);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(0, 8021, 1581, 561, 83, 0, false, false));
			playing.add(new Wizard(1, 15544, 2319, -109, -89, 0, false, false));
			playing.add(new Snaffle(8, 13911, 3932, -2, 2));
			playing.add(new Bludger(9, 5287, 873, 767, -66));
			playing.add(new Bludger(10, 15789, 1055, -594, 385));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(2, 15429, 3773, -53, 117, 1, false, false));
			myTeam.add(new Wizard(3, 2969, 2455, 259, 179, 1, false, true));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
	}
	@Test public void throw_directly_in_goal() {
		final Status status = new Status();
			status.setTeam(0);
			status.setMagic(10);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(2, 801, 3737, -276, -251, 1, true, false));
			playing.add(new Wizard(3, 2069, 2826, -339, -280, 1, false, false));
			playing.add(new Snaffle(4, 801, 3737, -276, -251));
			playing.add(new Snaffle(7, 12630, 6003, -396, -59));
			playing.add(new Bludger(9, 3631, 4219, 66, -338));
			playing.add(new Bludger(10, 5254, 4578, -211, -235));
			Wizard w0;
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(w0 = new Wizard(0, 12630, 6003, -396, -59, 0, true, true));
			myTeam.add(new Wizard(1, 4317, 5639, 98, 180, 0, false, false));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final Map<Wizard, Action> actions = entitiesStore.computeActions();
		assertThat(actions).hasSize(2);
		final Action actionOfW0 = actions.get(w0);
		assertThat(actionOfW0).isInstanceOf(ThrowTo.class);
		final ThrowTo throwed = (ThrowTo) actionOfW0;
		assertThat(Playground.goals.get(1).intersectionWith(throwed.direction.toLine())).isNotEmpty();
	}
	@Test public void can_find_actions_in_1480874912326() {
		final Status status = new Status();
			status.setTeam(1);
			status.setMagic(5);
		final List<Entity> playing=new ArrayList<>();
			playing.add(new Wizard(0, 1680, 4405, 143, -178, 0, false, false));
			playing.add(new Wizard(1, 1631, 3131, 133, 186, 0, false, false));
			playing.add(new Snaffle(4, 2135, 3839, 0, 0));
			playing.add(new Snaffle(5, 14055, 3925, -200, -279));
			playing.add(new Snaffle(6, 2290, 6901, 0, 0));
			playing.add(new Snaffle(7, 13710, 599, 0, 0));
			playing.add(new Snaffle(8, 8000, 3750, 0, 0));
			playing.add(new Bludger(9, 5841, 4084, -453, 85));
			playing.add(new Bludger(10, 10159, 3848, 453, 48));
		final List<Wizard> myTeam=new ArrayList<>();
			myTeam.add(new Wizard(2, 14000, 969, -211, -270, 1, false, true));
			myTeam.add(new Wizard(3, 14055, 3925, -200, -279, 1, true, false));
		final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());
		final List<String> actions = entitiesStore.computeActionsToString();
		assertThat(actions).hasSize(2);
	}
}
