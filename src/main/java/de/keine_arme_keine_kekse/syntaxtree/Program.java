package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a Minijava program
 */
public class Program extends Declaration {

    public MainClass mainClass;
    public ClassDeclList classes;

    public Program(MainClass mainClass, ClassDeclList classes) {
        this.mainClass = mainClass;
        this.classes = classes;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
