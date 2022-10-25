package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an expression:
 * <b>arrayId.length</b>
 */
public class ArrayLength extends Exp {

    public Exp arrayId;

    public ArrayLength(Exp array) {
        this.arrayId = array;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
