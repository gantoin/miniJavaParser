package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent the construction of a new object
 * <b>new classId()</b>
 */
public class NewObject extends Exp {

    public Identifier classId;

    public NewObject(Identifier classId) {
        this.classId = classId;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
