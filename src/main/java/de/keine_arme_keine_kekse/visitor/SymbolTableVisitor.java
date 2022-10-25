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
import de.keine_arme_keine_kekse.syntaxtree.Typing;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.While;
import de.keine_arme_keine_kekse.visitor.symboltable.ClassTable;
import de.keine_arme_keine_kekse.visitor.symboltable.MethodTable;
import de.keine_arme_keine_kekse.visitor.symboltable.ProgramTable;
import de.keine_arme_keine_kekse.visitor.symboltable.SymbolTable;
import de.keine_arme_keine_kekse.visitor.symboltable.Type;

/**
 * Objects of the class SymbolTableVisitor are used to traverse an abstract
 * syntax tree for some Minijava program (in a depth-first manner) in order to
 * fill the corresponding symbol table.
 *
 * Note: the implementation includes no error handling
 */
public class SymbolTableVisitor implements Visitor {

    /**
     * Offset for globabl variables
     */
    private static final int OFFSET_GLOBALS = -1;
    /**
     * symbol table corresponding to the actually visited Minijava program
     */
    private SymbolTable symbolTable;
    /**
     * offset of a variable declaration in an environment. It gives the number
     * of the storage place of a formal parameter or a local variable
     */
    int offset;

    /**
     * Creates a new instance with empty symbol table.
     */
    public SymbolTableVisitor() {

        this.symbolTable = new ProgramTable();
        this.offset = OFFSET_GLOBALS;
    }

    /**
     * returns the entire symbol table
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    /*
     * ========================================================================
     * visit methods
     * =====================================================================
     */

    @Override
    public void visit(Program n) {
        n.mainClass.accept(this);
        for (int i = 0; i < n.classes.size(); i++) {
            n.classes.elementAt(i).accept(this);
        }
        symbolTable.cleanup();
    }

    @Override
    public void visit(MainClass n) {

        // no traversal for classId and formal parameter of main method
        // n.classId.accept(this);
        // n.formalId.accept(this);

        this.enterClassScope(n.classId);
        this.enterMethodScope("main", null);

        n.statement.accept(this);

        this.leaveMethodScope();
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclSimple n) {

        /* enter scope of current class */
        this.enterClassScope(n.classId);

        // n.classId.accept(this);

        /* visit global variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* visit method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        /* leave current class scope */
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclExtends n) {

        /* enter scope of current class */
        this.enterClassScope(n.subclassId);

        /*
         * n.subclassId.accept(this);
         * n.superclassId.accept(this);
         */

        this.setSuperClassname(n.superclassId.getName());

        /* global variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /* method declarations */
        for (int i = 0; i < n.methodDecls.size(); i++) {
            n.methodDecls.elementAt(i).accept(this);
        }

        this.leaveClassScope();
    }

    @Override
    public void visit(VarDecl n) {

        n.type.accept(this);
        // n.id.accept(this);

        this.putVariable(n.id, n.type, offset);
        offset = offset + 1;

    }

    @Override
    public void visit(MethodDecl n) {

        /* enter scope of current method */
        this.enterMethodScope(n.methodId.getName(), n.resultType);

        /*
         * n.resultType.accept(this);
         * n.methodId.accept(this);
         */

        /* visit formal parameters */
        for (int i = 0; i < n.formalList.size(); i++) {
            n.formalList.elementAt(i).accept(this);
        }

        /* visit local variable declarations */
        for (int i = 0; i < n.varDecls.size(); i++) {
            n.varDecls.elementAt(i).accept(this);
        }

        /*
         * for ( int i = 0; i < n.sl.size(); i++ ) {
         * n.statements.elementAt(i).accept(this);
         * }
         * n.returnExp.accept(this);
         */

        /* leave scope of current method */
        this.leaveMethodScope();
    }

    @Override
    public void visit(Formal n) {

        this.putFormal(n.id, n.type, offset);
        offset = offset + 1;

        /*
         * n.t.accept(this);
         * n.i.accept(this);
         */
    }

    @Override
    public void visit(IntArrayTyping n) {
    }

    @Override
    public void visit(BooleanTyping n) {
    }

    @Override
    public void visit(IntegerTyping n) {
    }

    @Override
    public void visit(IdentifierTyping n) {
    }

    @Override
    public void visit(Block n) {
    }

    @Override
    public void visit(If n) {
    }

    @Override
    public void visit(While n) {
    }

    @Override
    public void visit(Print n) {
    }

    @Override
    public void visit(Assign n) {
    }

    @Override
    public void visit(ArrayAssign n) {
    }

    @Override
    public void visit(And n) {
    }

    @Override
    public void visit(LessThan n) {
    }

    @Override
    public void visit(Plus n) {
    }

    @Override
    public void visit(Minus n) {
    }

    @Override
    public void visit(Times n) {
    }

    @Override
    public void visit(ArrayLookup n) {
    }

    @Override
    public void visit(ArrayLength n) {
    }

    @Override
    public void visit(Call n) {
    }

    @Override
    public void visit(IntegerLiteral n) {
    }

    @Override
    public void visit(True n) {
    }

    @Override
    public void visit(False n) {
    }

    @Override
    public void visit(IdentifierExp n) {
    }

    @Override
    public void visit(This n) {
    }

    @Override
    public void visit(NewArray n) {
    }

    @Override
    public void visit(NewObject n) {
    }

    @Override
    public void visit(Not n) {
    }

    @Override
    public void visit(Identifier n) {
    }

    /*
     * ========================================================================
     * helper methods
     * =====================================================================
     */

    /**
     * enters the scope of the class given by className and extends the
     * symbol table with an entry for this class.
     *
     */
    private void enterClassScope(Identifier className) {

        symbolTable = ((ProgramTable) symbolTable).putClass(className);
        offset = OFFSET_GLOBALS;
    }

    /**
     * leaves the current class scope and restores the scope of the surrounding
     * environment
     */
    private void leaveClassScope() {
        symbolTable.cleanup();
        symbolTable = symbolTable.getParent();
        offset = OFFSET_GLOBALS;
    }

    /**
     * enters the scope of the methode given by methodName and extends the
     * symbol table with an entry for this method.
     */
    private void enterMethodScope(String methodName, Typing type) {

        this.symbolTable = ((ClassTable) symbolTable).putMethod(methodName, convertType(type));

        /*
         * the first parameter of a method call is always "This"
         * so the offset starts with 1
         */
        offset = 1;
    }

    /**
     * leaves the current method scope and restores the scope of the surrounding
     * environment and the offset.
     */
    private void leaveMethodScope() {
        this.symbolTable = this.symbolTable.getParent();
        offset = OFFSET_GLOBALS;
    }

    /**
     * adds the sub/super class relationship to the current symbol table *
     */
    private void setSuperClassname(String superClassname) {
        ((ClassTable) this.symbolTable).setSuperClass(superClassname);
    }

    /**
     * stores the given identifier (and its typing and offset) in the symbol
     * table.
     * If the process is currently within a method scope the identifier is stored
     * as a local variable of the current method. If no method scope is entered
     * the identifier is stored as a global variable of the current class.
     * 
     * TODO (UG): cast should be omitted
     *
     */
    private void putVariable(Identifier identifier, Typing typing, int offset) {

        Type type = this.convertType(typing);

        if (symbolTable instanceof ClassTable) {

            /* defines a global variable */
            ((ClassTable) this.symbolTable).putVariable(identifier.getName(), type);

        } else {

            /* defines a local variable */
            ((MethodTable) this.symbolTable).putVariable(identifier.getName(), type, offset);
        }
    }

    /**
     * stores the given formal parameter identifier (and its type and offset)
     * in the symbol table.
     *
     */
    private void putFormal(Identifier identifier, Typing typing, int offset) {

        String paramName = identifier.getName();
        Type type = this.convertType(typing);
        ((MethodTable) this.symbolTable).putFormalParameter(paramName, type, offset);
    }

    /**
     * converts a node of the syntax tree of typing "Type" into a string. This
     * string will be stored in the symbol table.
     * - If typing corresponds to a user-defined class, the name of the class
     * is returned.
     * - If the typing is a predefined typing then either "IntegerType",
     * "BooleanType", "IntArrayType" is returned.
     * - If no typing is given "VoidType" is returned
     * 
     * @return String representation of the given typing
     */
    private Type convertType(Typing typing) {

        if (typing == null) {
            return Type.VOID;

        } else if (typing instanceof IdentifierTyping) {

            IdentifierTyping identifierTyping = (IdentifierTyping) typing;
            String className = ((IdentifierTyping) typing).getName();
            ClassTable classTable = getSymbolTable().getClass(className);
            if (classTable == null) {
                getSymbolTable().getProgramTable().reserveClass(identifierTyping);
            }
            return getSymbolTable().getClass(className).getClassType();

        } else if (typing instanceof BooleanTyping) {
            return Type.BOOLEAN;

        } else if (typing instanceof IntegerTyping) {
            return Type.INT;

        } else if (typing instanceof IntArrayTyping) {
            return Type.INT_ARRAY;

        } else {
            return Type.VOID;
        }
    }
}
