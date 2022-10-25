package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent identifiers that may occur in some
 * expression.
 */
public class IdentifierExp extends Exp {

    private String name;

    public IdentifierExp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
