package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent the type <b>boolean</b>
 */
public class BooleanTyping extends Typing {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
