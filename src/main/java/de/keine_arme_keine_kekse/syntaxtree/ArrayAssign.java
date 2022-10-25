package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an assigment:
 * <b>arrayId[index] = exp</b>
 */
public class ArrayAssign extends Statement {

    public Identifier arrayId;
    public Exp index;
    public Exp exp;

    public ArrayAssign(Identifier arrayId, Exp index, Exp exp) {
        this.arrayId = arrayId;
        this.index = index;
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
