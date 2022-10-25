package de.keine_arme_keine_kekse.syntaxtree;

import de.keine_arme_keine_kekse.visitor.Visitor;

/**
 * Objects of this class represent a formal parameter:
 * <b>type id</b>
 */
public class Formal extends Declaration {

  public Typing type;
  public Identifier id;

  public Formal(Typing type, Identifier id) {
    this.type = type;
    this.id = id;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
