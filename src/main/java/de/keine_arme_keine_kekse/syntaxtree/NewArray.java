package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an array construction
 * <b>new int[exp]</b>
 */
public class NewArray extends Exp {

    public Exp exp;

    public NewArray(Exp exp) {
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
