package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a method call:
 * <b>exp.methodId(expList)</b>
 */
public class Call extends Exp {

    public Exp exp;
    public Identifier methodId;
    public ExpList expList;

    public Call(Exp exp, Identifier methodId, ExpList expList) {
        this.exp = exp;
        this.methodId = methodId;
        this.expList = expList;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
