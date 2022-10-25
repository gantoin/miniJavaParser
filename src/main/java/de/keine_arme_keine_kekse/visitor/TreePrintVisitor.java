package de.keine_arme_keine_kekse.visitor;

import java.io.PrintWriter;

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
import de.keine_arme_keine_kekse.syntaxtree.Node;
import de.keine_arme_keine_kekse.syntaxtree.Not;
import de.keine_arme_keine_kekse.syntaxtree.Plus;
import de.keine_arme_keine_kekse.syntaxtree.Print;
import de.keine_arme_keine_kekse.syntaxtree.Program;
import de.keine_arme_keine_kekse.syntaxtree.This;
import de.keine_arme_keine_kekse.syntaxtree.Times;
import de.keine_arme_keine_kekse.syntaxtree.True;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.While;
import de.keine_arme_keine_kekse.visitor.symboltable.Type;

/**
 * This visitor displays the syntax tree of a Minijava program as a tree
 */
public class TreePrintVisitor implements Visitor {

    private PrintWriter writer;

    private int indent = 0;

    public TreePrintVisitor(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void visit(Program n) {
        indent = 1;
        write_name_and_type(n);
        n.mainClass.accept(this);
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(MainClass n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.classId.accept(this);
        n.formalId.accept(this);
        n.statement.accept(this);
        indent--;
    }

    @Override
    public void visit(ClassDeclSimple n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.classId.accept(this);
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
            if (i + 1 < n.varDecls.size()) {
            }
        }
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(ClassDeclExtends n) {
        write_prefix(indent);
        indent++;

        write_name_and_type(n);

        n.subclassId.accept(this);
        n.superclassId.accept(this);
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
            if (i + 1 < n.varDecls.size()) {
                writer.println();
            }
        }
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(VarDecl n) {
        write_prefix(indent);
        indent++;

        write_name_and_type(n);

        n.type.accept(this);
        n.id.accept(this);
        indent--;
    }

    @Override
    public void visit(MethodDecl n) {
        write_prefix(indent);
        indent++;

        write_name_and_type(n);

        n.resultType.accept(this);
        n.methodId.accept(this);
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
            if (i + 1 < n.formalList.size()) {
            }
        }
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
            if (i < n.statements.size()) {
            }
        }
        n.returnExp.accept(this);
        indent--;
    }

    @Override
    public void visit(Formal n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.type.accept(this);
        n.id.accept(this);
        indent--;
    }

    @Override
    public void visit(IntArrayTyping n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(BooleanTyping n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(IntegerTyping n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(IdentifierTyping n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        write_prefix(indent);
        write_name_and_type(n.name);
        indent--;
    }

    @Override
    public void visit(Block n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        for (int i = 0; i < n.statements.size(); i++) {
            n.statements.elementAt(i).accept(this);
        }
        indent--;
    }

    @Override
    public void visit(If n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        n.thenStatement.accept(this);
        n.elseStatement.accept(this);
        indent--;
    }

    @Override
    public void visit(While n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        n.statement.accept(this);
        indent--;
    }

    @Override
    public void visit(Print n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(Assign n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.id.accept(this);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayAssign n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.arrayId.accept(this);
        n.index.accept(this);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(And n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(LessThan n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Plus n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Minus n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(Times n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.left.accept(this);
        n.right.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayLookup n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.arrayId.accept(this);
        n.index.accept(this);
        indent--;
    }

    @Override
    public void visit(ArrayLength n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.arrayId.accept(this);
        indent--;
    }

    @Override
    public void visit(Call n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        n.methodId.accept(this);
        for (int i = 0; i < n.expList.size(); i++) {
            n.expList.elementAt(i).accept(this);
            if (i + 1 < n.expList.size()) {
            }
        }
        indent--;
    }

    @Override
    public void visit(IntegerLiteral n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        write_prefix(indent);
        writer.println(n.getValue());
        indent--;
    }

    @Override
    public void visit(True n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(False n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(IdentifierExp n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        write_prefix(indent);
        writer.println(n.getName());
        indent--;
    }

    @Override
    public void visit(This n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        indent--;
    }

    @Override
    public void visit(NewArray n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(NewObject n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        write_prefix(indent);
        writer.println(n.classId.getName());
        indent--;
    }

    @Override
    public void visit(Not n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        n.exp.accept(this);
        indent--;
    }

    @Override
    public void visit(Identifier n) {
        write_prefix(indent);
        indent++;
        write_name_and_type(n);
        write_prefix(indent);
        writer.println(n.getName());
        indent--;
    }

    public void write_name_and_type(Object n) {

        writer.print(n.getClass().getSimpleName());

        if (n instanceof Node) {
            Type type = ((Node) n).getType();
            if (type != null) {
                writer.print(" : " + type);
            }
        }
        writer.println("");
    }

    public void write_prefix(int indent) {
        for (int i = 0; i < indent - 1; i++) {
            writer.print("    ");
        }
        writer.print("|-- ");
    }
}
