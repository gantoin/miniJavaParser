package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent an integer constant.
 */
public class IntegerLiteral extends Exp {

    private int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
