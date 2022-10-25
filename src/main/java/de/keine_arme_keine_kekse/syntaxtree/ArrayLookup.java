package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an expression:
 * <b>arrayId[index]</b>
 */
public class ArrayLookup extends Exp {

    public Exp arrayId;
    public Exp index;

    public ArrayLookup(Exp arrayId, Exp index) {
        this.arrayId = arrayId;
        this.index = index;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
