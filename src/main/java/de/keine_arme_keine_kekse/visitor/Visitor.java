package de.keine_arme_keine_kekse.visitor;

import de.keine_arme_keine_kekse.syntaxtree.And;
import de.keine_arme_keine_kekse.syntaxtree.ArrayAssign;
import de.keine_arme_keine_kekse.syntaxtree.ArrayLength;
import de.keine_arme_keine_kekse.syntaxtree.ArrayLookup;
import de.keine_arme_keine_kekse.syntaxtree.Assign;
import de.keine_arme_keine_kekse.syntaxtree.Block;
import de.keine_arme_keine_kekse.syntaxtree.BooleanTyping;
import de.keine_arme_keine_kekse.syntaxtree.Call;
import de.keine_arme_keine_kekse.syntaxtree.ClassDeclExtends;
import de.keine_arme_keine_kekse.syntaxtree.ClassDeclSimple;
import de.keine_arme_keine_kekse.syntaxtree.False;
import de.keine_arme_keine_kekse.syntaxtree.Formal;
import de.keine_arme_keine_kekse.syntaxtree.Identifier;
import de.keine_arme_keine_kekse.syntaxtree.IdentifierExp;
import de.keine_arme_keine_kekse.syntaxtree.IdentifierTyping;
import de.keine_arme_keine_kekse.syntaxtree.If;
import de.keine_arme_keine_kekse.syntaxtree.IntArrayTyping;
import de.keine_arme_keine_kekse.syntaxtree.IntegerLiteral;
import de.keine_arme_keine_kekse.syntaxtree.IntegerTyping;
import de.keine_arme_keine_kekse.syntaxtree.LessThan;
import de.keine_arme_keine_kekse.syntaxtree.MainClass;
import de.keine_arme_keine_kekse.syntaxtree.MethodDecl;
import de.keine_arme_keine_kekse.syntaxtree.Minus;
import de.keine_arme_keine_kekse.syntaxtree.NewArray;
import de.keine_arme_keine_kekse.syntaxtree.NewObject;
import de.keine_arme_keine_kekse.syntaxtree.Not;
import de.keine_arme_keine_kekse.syntaxtree.Plus;
import de.keine_arme_keine_kekse.syntaxtree.Print;
import de.keine_arme_keine_kekse.syntaxtree.Program;
import de.keine_arme_keine_kekse.syntaxtree.This;
import de.keine_arme_keine_kekse.syntaxtree.Times;
import de.keine_arme_keine_kekse.syntaxtree.True;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.While;

/**
 * Interface for all visitors
 */
public interface Visitor {
  public void visit(Program n);

  public void visit(MainClass n);

  public void visit(ClassDeclSimple n);

  public void visit(ClassDeclExtends n);

  public void visit(VarDecl n);

  public void visit(MethodDecl n);

  public void visit(Formal n);

  public void visit(IntArrayTyping n);

  public void visit(BooleanTyping n);

  public void visit(IntegerTyping n);

  public void visit(IdentifierTyping n);

  public void visit(Block n);

  public void visit(If n);

  public void visit(While n);

  public void visit(Print n);

  public void visit(Assign n);

  public void visit(ArrayAssign n);

  public void visit(And n);

  public void visit(LessThan n);

  public void visit(Plus n);

  public void visit(Minus n);

  public void visit(Times n);

  public void visit(ArrayLookup n);

  public void visit(ArrayLength n);

  public void visit(Call n);

  public void visit(IntegerLiteral n);

  public void visit(True n);

  public void visit(False n);

  public void visit(IdentifierExp n);

  public void visit(This n);

  public void visit(NewArray n);

  public void visit(NewObject n);

  public void visit(Not n);

  public void visit(Identifier n);
}
