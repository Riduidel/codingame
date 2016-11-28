package org.ndx.codingame.fantastic.entities;

public interface EntityVisitor<Type> {

	Type visitSnaffle(Snaffle snaffle);

	Type visitWizard(Wizard wizard);

	Type visitBludger(Bludger bludger);

}
