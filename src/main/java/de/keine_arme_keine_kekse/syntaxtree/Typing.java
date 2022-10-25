package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Abstract class for all types
 */
public abstract class Typing extends Node {

    public abstract void accept(Visitor v);
}
