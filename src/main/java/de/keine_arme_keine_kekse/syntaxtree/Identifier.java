package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an identifier that may occur on the
 * left-hand-side of an assignment.
 */
public class Identifier extends Node {

    private String name;

    public Identifier(String name) {
        this.name = name;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
