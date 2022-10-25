package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent the boolean value
 * <b>true</b>
 */
public class True extends Exp {

    public void accept(Visitor v) {
        v.visit(this);
    }
}
