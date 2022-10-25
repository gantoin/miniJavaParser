package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent the object
 * <b>this</b>
 */
public class This extends Exp {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
