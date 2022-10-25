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
 * This visitor displays the syntax tree of a Minijava program as a Minijava
 * program
 */
public class PrettyPrintVisitor implements Visitor {

    private int indent;

    private PrintWriter writer;

    public PrettyPrintVisitor(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void visit(Program n) {
        indent = 0;

        n.mainClass.accept(this);
        writelnCode();

        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
            writelnCode();
        }
    }

    @Override
    public void visit(MainClass n) {
        writePrefix();
        writeCode("class ");
        n.classId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope();

        writePrefix();
        writeCode("public static void main (String[] ");
        n.formalId.accept(this);
        writeCode(") ");

        writelnCode("{");
        enterScope();

        writePrefix();
        n.statement.accept(this);
        writelnCode();

        leaveScope();
        writePrefix();
        writelnCode("}");

        leaveScope();
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(ClassDeclSimple n) {
        writePrefix();
        writeCode("class ");
        n.classId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope();

        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }

        for (int i = 0; i < n.methodDecls.size(); i++) {
            writePrefix();
            n.methodDecls.elementAt(i).accept(this);
            writelnCode();
            if (i < n.methodDecls.size() - 1) {
                writelnCode();
            }
        }

        leaveScope();
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(ClassDeclExtends n) {
        writeCode("class ");
        n.subclassId.accept(this);
        writeCode(" extends ");
        n.superclassId.accept(this);
        writeCode(" ");
        writelnCode("{");
        enterScope();

        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }

        for (int i = 0; i < n.methodDecls.size(); i++) {
            writePrefix();
            n.methodDecls.elementAt(i).accept(this);
            writelnCode();
            if (i < n.methodDecls.size() - 1) {
                writelnCode();
            }
        }

        leaveScope();
        writePrefix();
        writelnCode("}");
    }

    @Override
    public void visit(VarDecl n) {
        n.type.accept(this);
        writeCode(" ");
        n.id.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(MethodDecl n) {
        writeCode("public ");
        n.resultType.accept(this);
        writeCode(" ");
        n.methodId.accept(this);

        writeCode("(");
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
            if (i + 1 < n.formalList.size()) {
                writeCode(", ");
            }
        }
        writeCode(") ");
        writelnCode("{");

        enterScope();

        for (int i = 0; i < n.varDecls.size(); i++) {
            writePrefix();
            n.varDecls.elementAt(i).accept(this);
            writelnCode();
            if (i == n.varDecls.size() - 1) {
                writelnCode();
            }
        }

        for (int i = 0; i < n.statements.size(); i++) {
            writePrefix();
            n.statements.elementAt(i).accept(this);
            writelnCode();
        }

        writePrefix();
        writeCode("return ");
        n.returnExp.accept(this);
        writelnCode(";");

        leaveScope();
        writePrefix();
        writeCode("}");
    }

    @Override
    public void visit(Formal n) {
        n.type.accept(this);
        writeCode(" ");
        n.id.accept(this);
    }

    @Override
    public void visit(IntArrayTyping n) {
        writeCode("int[]");
    }

    @Override
    public void visit(BooleanTyping n) {
        writeCode("boolean");
    }

    @Override
    public void visit(IntegerTyping n) {
        writeCode("int");
    }

    @Override
    public void visit(IdentifierTyping n) {
        writeCode(n.name);
    }

    @Override
    public void visit(Block n) {
        writelnCode("{");
        enterScope();

        for (int i = 0; i < n.statements.size(); i++) {
            writePrefix();
            n.statements.elementAt(i).accept(this);
            writelnCode();
        }

        leaveScope();
        writePrefix();
        writeCode("}");
    }

    @Override
    public void visit(If n) {
        writeCode("if (");
        n.exp.accept(this);
        writeCode(") ");

        n.thenStatement.accept(this);

        writeCode(" else ");
        n.elseStatement.accept(this);
    }

    @Override
    public void visit(While n) {
        writeCode("while (");
        n.exp.accept(this);
        writeCode(") ");

        n.statement.accept(this);
    }

    @Override
    public void visit(Print n) {
        writeCode("System.out.println(");
        n.exp.accept(this);
        writeCode(");");
    }

    @Override
    public void visit(Assign n) {
        n.id.accept(this);
        writeCode(" = ");
        n.exp.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(ArrayAssign n) {
        n.arrayId.accept(this);
        writeCode("[");
        n.index.accept(this);
        writeCode("] = ");
        n.exp.accept(this);
        writeCode(";");
    }

    @Override
    public void visit(And n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" && ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(LessThan n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" < ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Plus n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" + ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Minus n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" - ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Times n) {
        writeCode("(");
        n.left.accept(this);
        writeCode(" * ");
        n.right.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(ArrayLookup n) {
        n.arrayId.accept(this);
        writeCode("[");
        n.index.accept(this);
        writeCode("]");
    }

    @Override
    public void visit(ArrayLength n) {
        n.arrayId.accept(this);
        writeCode(".length");
    }

    @Override
    public void visit(Call n) {
        n.exp.accept(this);
        writeCode(".");
        n.methodId.accept(this);
        writeCode("(");
        for (int i = 0; i < n.expList.size(); i++) {
            n.expList.elementAt(i).accept(this);
            if (i + 1 < n.expList.size()) {
                writeCode(", ");
            }
        }
        writeCode(")");
    }

    @Override
    public void visit(IntegerLiteral n) {
        writeCode(n.getValue());
    }

    @Override
    public void visit(True n) {
        writeCode("true");
    }

    @Override
    public void visit(False n) {
        writeCode("false");
    }

    @Override
    public void visit(IdentifierExp n) {
        writeCode(n.getName());
    }

    @Override
    public void visit(This n) {
        writeCode("this");
    }

    @Override
    public void visit(NewArray n) {
        writeCode("new int[");
        n.exp.accept(this);
        writeCode("]");
    }

    @Override
    public void visit(NewObject n) {
        writeCode("new ");
        writeCode(n.classId.getName());
        writeCode("()");
    }

    @Override
    public void visit(Not n) {
        writeCode("(!");
        n.exp.accept(this);
        writeCode(")");
    }

    @Override
    public void visit(Identifier n) {
        writeCode(n.getName());
    }

    /*
     * ==========================================================================
     * helper methods
     * =======================================================================
     */

    public void writelnCode() {
        writer.println();
    }

    public void writelnCode(String code) {
        writer.println(code);
    }

    public void writeCode(String code) {
        writer.print(code);
    }

    public void writeCode(int num) {
        writer.print(num + "");
    }

    public void writePrefix() {
        for (int i = 0; i < indent; i++) {
            writeCode("    ");
        }
    }

    private void enterScope() {
        this.indent++;
    }

    private void leaveScope() {
        this.indent--;
    }
}
