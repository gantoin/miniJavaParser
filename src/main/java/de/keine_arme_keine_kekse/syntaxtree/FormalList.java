package de.keine_arme_keine_kekse.syntaxtree;

import java.util.Vector;

/**
 * Objects of this class represent a list of formal parameters
 */
public class FormalList extends NodeList {

    private final Vector<Formal> list;

    public FormalList() {
        list = new Vector<>();
    }

    public void addElement(Formal n) {
        list.addElement(n);
    }

    public Formal elementAt(int i) {
        return list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
