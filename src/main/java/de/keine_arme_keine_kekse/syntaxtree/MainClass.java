package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a class that includes the method main
 */
public class MainClass extends Declaration {

    public Identifier classId;
    public Identifier formalId;
    public Statement statement;

    public MainClass(Identifier classId, Identifier formalId,
            Statement statement) {

        this.classId = classId;
        this.formalId = formalId;
        this.statement = statement;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
