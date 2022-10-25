package de.keine_arme_keine_kekse.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import de.keine_arme_keine_kekse.syntaxtree.Exp;
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
import de.keine_arme_keine_kekse.syntaxtree.Statement;
import de.keine_arme_keine_kekse.syntaxtree.This;
import de.keine_arme_keine_kekse.syntaxtree.Times;
import de.keine_arme_keine_kekse.syntaxtree.True;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.While;
import de.keine_arme_keine_kekse.visitor.symboltable.ClassTable;
import de.keine_arme_keine_kekse.visitor.symboltable.SymbolTable;
import de.keine_arme_keine_kekse.visitor.symboltable.Type;

/**
 * Objects of the class TypeVisitor are used to traverse an abstract syntax tree
 * for some Minijava program (in a depth-first manner) in order to provide a
 * mapping from the nodes of the abstract syntax tree to their corresponding
 * types.
 * 
 * Types of identifiers (i.e. leaf nodes of the tree) are retrieved from the
 * symbol table provided by the SymbolTableVisitor. The types of internal nodes
 * are derived by means of the type system of MiniJava.
 * 
 * TODO (UG): the implementation includes no error handling
 */
public class TypeVisitor implements Visitor {

    /**
     * symbol table of the currently visited MiniJava program
     */
    private SymbolTable symbolTable;

    /**
     * mapping from nodes of the abstract syntax tree to their type
     * 
     * TODO (UG): types should not be Strings
     */
    private final HashMap<Node, String> types;

    /**
     * Creates a new instance with the given symbol table and an empty mapping
     * from nodes of the abstract syntax tree to their types.
     *
     * @param symbolTable symbol table created by SymbolTableVisitor
     */
    public TypeVisitor(SymbolTable symbolTable) {

        this.symbolTable = symbolTable;
        this.types = new HashMap<>();
    }

    /**
     * returns the entire mapping from nodes to types
     *
     * @return mapping from nodes to types
     */
    public HashMap<Node, String> getTypes() {
        return types;
    }

    /*
     * ========================================================================
     * visit methods
     * =====================================================================
     */
    @Override
    public void visit(Program node) {

        // main class declaration
        node.mainClass.accept(this);

        // list of class declarations
        for (int i = 0; i < node.classes.size(); i++) {
            node.classes.elementAt(i).accept(this);
        }

        node.setType(Type.VOID);
    }

    @Override
    public void visit(MainClass node) {

        // node.classId.accept(this);
        // node.formalId.accept(this);

        this.enterClassScope(node.classId.getName());
        this.enterMethodScope("main");

        node.statement.accept(this);
        node.setType(Type.VOID);

        this.leaveMethodScope();
        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclSimple node) {

        this.enterClassScope(node.classId.getName());

        // node.classId.accept(this);
        // global variable declaration
        for (int i = 0; i < node.varDecls.size(); i++) {
            node.varDecls.elementAt(i).accept(this);
        }

        // list of method declarations
        for (int i = 0; i < node.methodDecls.size(); i++) {
            node.methodDecls.elementAt(i).accept(this);
        }

        node.setType(Type.VOID);

        this.leaveClassScope();
    }

    @Override
    public void visit(ClassDeclExtends node) {

        this.enterClassScope(node.subclassId.getName());

        // node.superclassId.accept(this);
        // node.subclassId.accept(this);
        // global variable declaration
        for (int i = 0; i < node.varDecls.size(); i++) {
            node.varDecls.elementAt(i).accept(this);
        }

        // list of method declarations
        for (int i = 0; i < node.methodDecls.size(); i++) {
            node.methodDecls.elementAt(i).accept(this);
        }

        node.setType(Type.VOID);

        this.leaveClassScope();
    }

    @Override
    public void visit(VarDecl node) {

        node.type.accept(this);
        node.setType(Type.VOID);
    }

    @Override
    public void visit(MethodDecl node) {

        this.enterMethodScope(node.methodId.getName());

        node.resultType.accept(this);
        // node.methodId.accept(this);

        for (int i = 0; i < node.formalList.size(); i++) {
            node.formalList.elementAt(i).accept(this);
        }

        // local variables
        for (int i = 0; i < node.varDecls.size(); i++) {
            node.varDecls.elementAt(i).accept(this);
        }

        // statement list of method
        for (int i = 0; i < node.statements.size(); i++) {
            Statement s = node.statements.elementAt(i);
            s.accept(this);
        }

        // expression to be returned from method
        node.returnExp.accept(this);

        // Type checking for method return value
        Type className = getThisClassname();
        String methodName = node.methodId.getName();

        Type requiredType = this.getResultType(className, methodName);
        if (node.returnExp.getType() == requiredType) {

            node.setType(Type.VOID);

        } else {
            throw new SemanticException("return value of "
                    + node.methodId.getName()
                    + " has wrong type, required " + requiredType.getName()
                    + " but found " + node.returnExp.getType());
        }

        this.leaveMethodScope();
    }

    @Override
    public void visit(Formal node) {
        node.type.accept(this);
        node.id.setType(node.getType());
    }

    @Override
    public void visit(IntArrayTyping node) {
        node.setType(Type.INT_ARRAY);
    }

    @Override
    public void visit(BooleanTyping node) {
        node.setType(Type.BOOLEAN);
    }

    @Override
    public void visit(IntegerTyping node) {
        node.setType(Type.INT);
    }

    @Override
    public void visit(IdentifierTyping node) {

        ClassTable classTable = symbolTable.getClass(node.getName());
        if (classTable == null) {
            throw new SemanticException("Symbol " + node.getName() + " is unknown.");
        }
        node.setType(classTable.getClassType());
    }

    @Override
    public void visit(Block node) {
        // TODO (Blatt 03)

        // statement list of block
        for (int i = 0; i < node.statements.size(); i++) {
            node.statements.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(If node) {

        node.exp.accept(this);
        if (node.exp.getType() != Type.BOOLEAN) {
            throw new SemanticException("Expression of if statement is not boolean");
        }

        node.thenStatement.accept(this);
        node.elseStatement.accept(this);

        node.setType(Type.VOID);
    }

    @Override
    public void visit(While node) {
        // TODO (Blatt 03)

        node.exp.accept(this);
        node.statement.accept(this);
    }

    @Override
    public void visit(Print node) {
        // TODO (Blatt 03)

        node.exp.accept(this);
    }

    @Override
    public void visit(Assign node) {
        // TODO (Blatt 03)

        node.id.accept(this);
        node.exp.accept(this);
    }

    @Override
    public void visit(ArrayAssign node) {
        // TODO (Blatt 03)

        node.arrayId.accept(this);
        node.index.accept(this);
        node.exp.accept(this);
    }

    @Override
    public void visit(And node) {
        // TODO (Blatt 03)

        node.left.accept(this);
        node.right.accept(this);
    }

    @Override
    public void visit(LessThan node) {
        // TODO (Blatt 03)

        node.left.accept(this);
        node.right.accept(this);
    }

    @Override
    public void visit(Plus node) {
        // TODO (Blatt 03)

        node.left.accept(this);
        node.right.accept(this);
    }

    @Override
    public void visit(Minus node) {
        // TODO (Blatt 03)

        node.left.accept(this);
        node.right.accept(this);
    }

    @Override
    public void visit(Times node) {
        // TODO (Blatt 03)

        node.left.accept(this);
        node.right.accept(this);
    }

    @Override
    public void visit(ArrayLookup node) {
        // TODO (Blatt 03)

        node.arrayId.accept(this);
        node.index.accept(this);
    }

    @Override
    public void visit(ArrayLength node) {
        // TODO (Blatt 03)

        node.arrayId.accept(this);
    }

    @Override
    public void visit(Call node) {
        // TODO (Blatt 03)

        node.exp.accept(this);
        node.methodId.accept(this);

        // visit list of parameters
        for (int i = 0; i < node.expList.size(); i++) {
            Exp param = node.expList.elementAt(i);
            param.accept(this);
        }
    }

    @Override
    public void visit(IntegerLiteral node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(True node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(False node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(IdentifierExp node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(This node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(NewArray node) {
        // TODO (Blatt 03)

        node.exp.accept(this);
    }

    @Override
    public void visit(NewObject node) {
        // TODO (Blatt 03)
    }

    @Override
    public void visit(Not node) {
        // TODO (Blatt 03)

        node.exp.accept(this);
    }

    @Override
    public void visit(Identifier node) {
        // TODO (Blatt 03)
    }

    // ========================================================================
    // helper methods
    // ========================================================================
    // ========================================================================
    // handling scopes
    // ========================================================================

    /**
     * enters the scope of the class given by className.
     *
     * @param className name of class
     */
    private void enterClassScope(String className) {

        this.symbolTable = this.symbolTable.getClass(className);
    }

    /**
     * leaves the current class scope
     */
    private void leaveClassScope() {

        this.symbolTable = this.symbolTable.getParent();
    }

    /**
     * enters the scope of the class given by methodName.
     *
     * @param methodName name of method
     */
    private void enterMethodScope(String methodName) {

        this.symbolTable = ((ClassTable) this.symbolTable).getMethod(methodName);
    }

    /**
     * leaves the current method scope
     */
    private void leaveMethodScope() {

        this.symbolTable = this.symbolTable.getParent();
    }

    // ========================================================================
    // handling types
    // ========================================================================

    /**
     * returns the class name of the current class
     */
    private Type getThisClassname() {
        /*
         * the current scope is method scope, so the parent is the current
         * class scope
         */
        return ((ClassTable) this.symbolTable.getParent()).getClassType();
    }

    /**
     * returns the type of the given variable name from the symbol table.
     *
     * @param varName name of identifier
     * @return type of the given variable
     */
    private Type getVariableType(String varName) {

        return this.symbolTable.getVariableEntry(varName).getType();
    }

    /**
     * returns the result type of the method specified by type and
     * methodName.
     *
     * @param type       name of class
     * @param methodName name of method *
     * @return result type of the method as string
     */
    private Type getResultType(Type type, String methodName) {

        return this.symbolTable
                .getClass(type.getName()).getMethod(methodName)
                .getResultType();
    }

    /**
     * Validates if class with given name exists.
     * If not, an Exception is thrown
     *
     * @param type Name of the class
     */
    private void validateIfClassExists(Type type) {
        ClassTable classTable = this.symbolTable.getClass(type.getName());

        if (classTable == null) {
            throw new SemanticException("Class " + type + " does not exist");
        }
    }

    /**
     * returns a list of all types, that can be used to store a variable of the
     * given type
     *
     * @param type type that should be checked
     * @return list of all allowed types
     */
    private List<Type> getAllAllowedTypes(Type type) {
        ArrayList<Type> allowedTypes = new ArrayList<>();
        allowedTypes.add(type);

        ClassTable classTable = this.symbolTable.getClass(type.getName());
        if (classTable != null) {
            Type superClassName = classTable.getSuperClass();
            if (superClassName != null) {
                allowedTypes.addAll(this.getAllAllowedTypes(superClassName));
            }
        }
        return allowedTypes;
    }
}
