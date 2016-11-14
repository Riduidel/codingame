package org.ndx.codingame.hypersonic;

import org.ndx.codingame.hypersonic.content.ContentAdapter;
import org.ndx.codingame.hypersonic.content.ContentVisitor;
import org.ndx.codingame.hypersonic.content.Fire;
import org.ndx.codingame.hypersonic.content.FireThenItem;
import org.ndx.codingame.hypersonic.content.Item;

public class ScoreComputer extends ContentAdapter<Integer> implements ContentVisitor<Integer> {

	public ScoreComputer() {
		super(0);
	}
	
	@Override
	public Integer visitFire(Fire fire) {
		return EvolvableConstants.SCORE_SUICIDE;
	}
	
	@Override
	public Integer visitFireThenItem(FireThenItem fire) {
		return EvolvableConstants.SCORE_SUICIDE;
	}
	
	@Override
	public Integer visitItem(Item item) {
		return EvolvableConstants.SCORE_CATCHED_ITEM;
	}
}
