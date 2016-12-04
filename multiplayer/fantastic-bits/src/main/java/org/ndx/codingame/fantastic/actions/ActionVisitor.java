package org.ndx.codingame.fantastic.actions;

import org.ndx.codingame.fantastic.actions.spells.AccioSpell;
import org.ndx.codingame.fantastic.actions.spells.FlipendoSpell;
import org.ndx.codingame.fantastic.actions.spells.ObliviateSpell;
import org.ndx.codingame.fantastic.actions.spells.PetrificusSpell;

public interface ActionVisitor<Type> {

	Type visit(MoveTo moveTo);

	Type visit(ThrowTo throwTo);

	Type visit(ObliviateSpell obliviateSpell);

	Type visit(AccioSpell accioSpell);

	Type visit(FlipendoSpell flipendoSpell);

	Type visit(PetrificusSpell petrificus);

}
