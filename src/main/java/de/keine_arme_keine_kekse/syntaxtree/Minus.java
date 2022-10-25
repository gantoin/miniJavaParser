package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a subtraction
 * <b>left - right</b>
 */
public class Minus extends Exp {

    public Exp left;
    public Exp right;

    public Minus(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
