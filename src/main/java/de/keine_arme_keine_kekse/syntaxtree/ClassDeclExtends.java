package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent subclasses
 */
public class ClassDeclExtends extends ClassDecl {

    public Identifier subclassId;
    public Identifier superclassId;
    public VarDeclList varDecls;
    public MethodDeclList methodDecls;

    public ClassDeclExtends(Identifier idSubclass, Identifier idSuperclass,
            VarDeclList varDecls, MethodDeclList methodDecls) {
        this.subclassId = idSubclass;
        this.superclassId = idSuperclass;
        this.varDecls = varDecls;
        this.methodDecls = methodDecls;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
