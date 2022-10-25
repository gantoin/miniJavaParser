package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Abstract class for all statements
 */
public abstract class Statement extends Node {

    @Override
    public abstract void accept(Visitor v);
}
