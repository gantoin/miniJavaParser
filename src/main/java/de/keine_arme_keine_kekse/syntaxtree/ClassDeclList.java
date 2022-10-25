package de.keine_arme_keine_kekse.syntaxtree;

import java.util.LinkedList;

/**
 * Objects of this class represent a list of classes
 */
public class ClassDeclList extends NodeList {

    private LinkedList<ClassDecl> list;

    public ClassDeclList() {
        list = new LinkedList<ClassDecl>();
    }

    public void addElement(ClassDecl n) {
        list.add(n);
    }

    public ClassDecl elementAt(int i) {
        return (ClassDecl) list.get(i);
    }

    public int size() {
        return list.size();
    }
}
