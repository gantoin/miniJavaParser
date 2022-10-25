package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a boolean conjunction:
 * {@code left && right}
 */
public class And extends Exp {

    /**
     * left operand of conjunction
     */
    public Exp left;

    /**
     * right operand of conjunction
     */
    public Exp right;

    /**
     * Creates a conjunction
     * 
     * @param left  left operand
     * @param right right operand
     */
    public And(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    /**
     * accepts a visitor
     * 
     * @param v
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
