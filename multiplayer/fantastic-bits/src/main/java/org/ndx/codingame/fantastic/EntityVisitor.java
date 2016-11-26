package org.ndx.codingame.fantastic;

public interface EntityVisitor<Type> {

	Type visitSnaffle(Snaffle snaffle);

	Type visitWizard(Wizard wizard);

	Type visitBludger(Bludger bludger);

}
