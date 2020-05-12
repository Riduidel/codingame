package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.ContentVisitor;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Nothing;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Wall;

public interface SpringChallengePlayground extends MutablePlayground<Content> {
	public default DiscretePoint putBackOnPlayground(DiscretePoint point) {
		int x = point.x,
			y = point.y;
		if(x<0) {
			x = getWidth()-1;
		} else if(x>=getWidth()) {
			x = 0;
		}
		if(y<0) {
			y = getHeight()-1;
		} else if(y>=getHeight()) {
			y = 0;
		}
		return new DiscretePoint(x, y);
	}

	public default boolean allow(final DiscretePoint position) {
		return allow(position.x, position.y);
	}

	public default boolean allow(final int p_x, final int p_y) {
		if(contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}

	public default void readRow(final String row, final int rowIndex) {
		final char[] characters = row.toCharArray();
		for (int x = 0; x < characters.length; x++) {
			char character = characters[x];
			Content content = null;
			switch(character) {
			case Ground.CHARACTER: content = Ground.instance; break;
			case Wall.CHARACTER: content = Wall.instance; break;
			case BigPill.CHARACTER: content = new BigPill(x, rowIndex); break;
			case SmallPill.CHARACTER: content = new SmallPill(x, rowIndex); break;
			case PotentialSmallPill.CHARACTER: content = PotentialSmallPill.instance; break;
			}
			set(x, rowIndex, content);
		}
	}

	public default String toString(final ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1+"\n"+r2).get();
	}

	public default Collection<String> toStringCollection(final ContentVisitor<String> visitor) {
		return accept(new PlaygroundAdapter<Collection<String>, Content>() {
			private StringBuilder row;
			@Override
			public void endVisitRow(final int y) {
				returned.add(row.toString());
			}
			@Override
			public void startVisit(final ImmutablePlayground<Content> playground) {
				returned = new ArrayList<>(playground.getHeight());
			}
			@Override
			public void startVisitRow(final int y) {
				row = new StringBuilder();
			}
			@Override
			public void visit(final int x, final int y, final Content content) {
				if(content==null) {
					row.append(Nothing.instance.accept(visitor));
				} else {
					row.append(content.accept(visitor));
				}
			}
		});
	}

	public <Type extends Content> List<Type> getAll(Class<Type> class1);

	public List<List<DiscretePoint>> speedPointsAt(DiscretePoint p);

	public List<DiscretePoint> nextPointsAt(DiscretePoint p);

	public Playground<Integer> zero();
	public ScoringSystem cacheDistanceMapTo(BigPill pill);

	public default Turn readWriteProxy() {
		return new Turn(this, getBigPillsDistances());
	}

	public ImmutablePlayground<Integer> getBigPillsDistances();
}
