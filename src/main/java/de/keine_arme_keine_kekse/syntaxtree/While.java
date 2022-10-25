package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a while statement
 * <b>while(exp) statement</b>
 */
public class While extends Statement {

    public Exp exp;
    public Statement statement;

    public While(Exp exp, Statement statement) {
        this.exp = exp;
        this.statement = statement;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
