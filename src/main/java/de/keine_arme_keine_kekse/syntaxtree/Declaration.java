package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Abstract class for all declarations
 */
public abstract class Declaration extends Node {

    @Override
    public abstract void accept(Visitor v);
}
