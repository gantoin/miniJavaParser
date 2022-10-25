package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an
 * <b>if (exp) thenStatement else elseStatement</b>
 */
public class If extends Statement {

    public Exp exp;
    public Statement thenStatement;
    public Statement elseStatement;

    public If(Exp exp, Statement thenStatement, Statement elseStatement) {

        this.exp = exp;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
