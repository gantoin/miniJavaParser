package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a print statment
 * <b>System.out.println(exp)</b>
 */
public class Print extends Statement {

    public Exp exp;

    public Print(Exp exp) {
        this.exp = exp;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
