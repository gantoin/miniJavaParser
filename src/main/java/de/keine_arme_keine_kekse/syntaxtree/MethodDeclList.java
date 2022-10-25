package de.keine_arme_keine_kekse.syntaxtree;

import java.util.Vector;

/**
 * Objects of this class represent a list of method declarations
 */
public class MethodDeclList extends NodeList {

    private Vector list;

    public MethodDeclList() {
        list = new Vector();
    }

    public void addElement(MethodDecl n) {
        list.addElement(n);
    }

    public MethodDecl elementAt(int i) {
        return (MethodDecl) list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
