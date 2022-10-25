package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;
import de.keine_arme_keine_kekse.visitor.symboltable.Type;

/**
 * Abstract class for all nodes of the syntax tree.
 */
public abstract class Node {

    /* Attribute beginLabel */
    private String beginLabel;

    public void setBeginLabel(String label) {
        this.beginLabel = label;
    }

    public String getBeginLabel() {
        return this.beginLabel;
    }

    /* Attribut nextLabel */
    private String nextLabel;

    public void setNextLabel(String label) {
        this.nextLabel = label;
    }

    public String getNextLabel() {
        return this.nextLabel;
    }

    /* Attribut trueLabel */
    private String trueLabel;

    public void setTrueLabel(String label) {
        trueLabel = label;
    }

    public String getTrueLabel() {
        return this.trueLabel;
    }

    /* Attribut falseLabel */
    private String falseLabel;

    public void setFalseLabel(String label) {
        falseLabel = label;
    }

    public String getFalseLabel() {
        return this.falseLabel;
    }

    /* Attribut Type */
    private Type attributeType;

    public void setType(Type type) {
        attributeType = type;
    }

    public Type getType() {
        return this.attributeType;
    }

    public abstract void accept(Visitor v);
}
