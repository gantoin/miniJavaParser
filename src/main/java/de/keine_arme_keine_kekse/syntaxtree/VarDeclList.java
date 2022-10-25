package de.keine_arme_keine_kekse.syntaxtree;

import java.util.Vector;

/**
 * Objects of this class represent a list of variable declarations
 */
public class VarDeclList extends NodeList {

    private Vector list;

    public VarDeclList() {
        list = new Vector();
    }

    public void addElement(VarDecl n) {
        list.addElement(n);
    }

    public VarDecl elementAt(int i) {
        return (VarDecl) list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
