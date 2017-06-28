package org.ndx.codingame.wondevwoman.actions;

public interface WonderActionVisitor<Returned> {

	Returned visitMove(Move move);

	Returned visitPush(Push push);

}
