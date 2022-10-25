package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an object class type.
 */
public class IdentifierTyping extends Typing {

    public String name;

    public IdentifierTyping(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
