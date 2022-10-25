package de.keine_arme_keine_kekse.visitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import de.keine_arme_keine_kekse.syntaxtree.Statement;
import de.keine_arme_keine_kekse.syntaxtree.This;
import de.keine_arme_keine_kekse.syntaxtree.Times;
import de.keine_arme_keine_kekse.syntaxtree.True;
import de.keine_arme_keine_kekse.syntaxtree.VarDecl;
import de.keine_arme_keine_kekse.syntaxtree.While;
import de.keine_arme_keine_kekse.visitor.symboltable.ClassTable;
import de.keine_arme_keine_kekse.visitor.symboltable.MethodTable;
import de.keine_arme_keine_kekse.visitor.symboltable.ProgramTable;
import de.keine_arme_keine_kekse.visitor.symboltable.SymbolTable;
import de.keine_arme_keine_kekse.visitor.symboltable.Type;
import de.keine_arme_keine_kekse.visitor.symboltable.VariableEntry;

/**
 * Objects of the class JasminVisitor traverse an abstract syntax tree for some
 * Minijava program (in a depth-first manner) in order to create Jasmin files
 * for each Minijava class represented in the abstract syntax tree. The
 * generation of Jasmin files is based on the syntax tree, the symbol table, and
 * the information on types for each expression node of the syntax tree.
 *
 * Note: the implementation includes no error handling
 */
public class JasminVisitor implements Visitor {

    /**
     * Enumeration that provides all kinds of Labels to be created
     */
    enum Label {
        NEXT, BEGIN, TRUE, FALSE
    };

    /**
     * specifies the directory to which the writer writes the Jasmin file
     */
    private final String jasminDir;

    /**
     * symbol table corresponding to the actually visited Minijava program
     */
    private SymbolTable symbolTable;

    /**
     * holds a unique number for the next label to be generated
     */
    private int nextLabelNo;

    /**
     * holds the connection to the file to be written for the current class
     */
    private PrintWriter writer;

    /**
     * write Jasmin code additionally to console if set to true
     */
    private boolean verbose;

    /**
     * Creates a new instance of class JasminVisitor with a path of a directory
     * to which the Jasmin file will be generated, a symbol table and type
     * mapping (mapping from nodes of the abstract syntax tree to their
     * corresponding types).
     *
     * The initial state indicates that neither a class nor a method scope has
     * yet been entered and no labels have been generated.
     *
     * @param jasminDir   path to the Jasmin file
     * @param symbolTable symbol table derived from the correspinding Minijava
     *                    program
     */
    public JasminVisitor(
            String jasminDir,
            SymbolTable symbolTable) {

        this.jasminDir = jasminDir;
        this.symbolTable = symbolTable;

        /* initialise labelling */
        this.nextLabelNo = 0;

        this.verbose = false;
    }

    /**
     * Sets JasminVisitor to verbose mode
     */
    public void setVerbose() {
        this.verbose = true;
    }

    /*
     * ========================================================================
     * visit methods
     * =====================================================================
     */
    /**
     * creates Jasmin code for a Minijava program.
     *
     * @param node
     */
    @Override
    public void visit(Program node) {
        node.mainClass.accept(this);
        for (int i = 0; i < node.classes.size(); i++) {
            node.classes.elementAt(i).accept(this);
        }
    }

    /**
     * creates Jasmin code for the main class declaration.
     *
     * @param node
     */
    @Override
    public void visit(MainClass node) {

        /* Jasmin-Datei f�r aktuelle Klasse �ffnen */
        writer = openWriter(node.classId.getName());

        /* enter scope of main class */
        enterClassScope(node.classId.getName());

        /* emit Jasmin directives */
        node.classId.accept(this);
        emitCode(".class public " + node.classId.getName());
        emitCode(".super java/lang/Object");

        /* emit standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial java/lang/Object/<init>()V");
        emitCode("return");
        emitCode(".end method");

        /* emit main method declaration */
        node.formalId.accept(this);
        emitCode(".method public static main([Ljava/lang/String;)V");
        emitCode(".limit stack 20"); // TODO (UG): calc minimal stacksize
        emitCode(".limit locals 1");

        /* emit statement of main method */
        node.statement.accept(this);

        emitCode("return");
        emitCode(".end method");

        /* enter scope of main class */
        leaveClassScope();

        writer.close();
    }

    /**
     * creates Jasmin code for a normal class declaration.
     *
     * @param node
     */
    @Override
    public void visit(ClassDeclSimple node) {

        /* Jasmin-Datei f�r aktuelle Klasse �ffnen */
        writer = openWriter(node.classId.getName());

        /* enter scope of class */
        enterClassScope(node.classId.getName());

        /* emit Jasmin directives */
        node.classId.accept(this);
        emitCode(".class public " + node.classId.getName());
        emitCode(".super java/lang/Object");

        /* emit code for global variable declarations */
        for (int i = 0; i < node.varDecls.size(); i++) {
            node.varDecls.elementAt(i).accept(this);
        }

        /* emit code for standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial java/lang/Object/<init>()V");
        emitCode("return");
        emitCode(".end method");

        /* emit code for method declarations */
        for (int i = 0; i < node.methodDecls.size(); i++) {
            node.methodDecls.elementAt(i).accept(this);
        }

        /* enter scope of class */
        leaveClassScope();

        writer.close();
    }

    /**
     * creates Jasmin code for a subclass declaration.
     *
     * @param node
     */
    @Override
    public void visit(ClassDeclExtends node) {

        /* opens Jasmin file for current class */
        writer = openWriter(node.subclassId.getName());

        /* enter scope of class */
        enterClassScope(node.subclassId.getName());

        /* emit Jasmin directives */
        node.subclassId.accept(this);
        emitCode(".class public " + node.subclassId.getName());
        node.superclassId.accept(this);
        emitCode(".super " + node.superclassId.getName());

        /* emit code for global variable declarations */
        for (int i = 0; i < node.varDecls.size(); i++) {
            node.varDecls.elementAt(i).accept(this);
        }

        /* emit code for standard initialization */
        emitCode(".method public <init>()V");
        emitCode("aload_0");
        emitCode("invokespecial " + node.superclassId.getName() + "/<init>()V");
        emitCode("return");
        emitCode(".end method");

        /* emit code for method declarations */
        for (int i = 0; i < node.methodDecls.size(); i++) {
            node.methodDecls.elementAt(i).accept(this);
        }

        /* enter scope of class */
        leaveClassScope();

        writer.close();
    }

    /**
     * creates Jasmin code for a variable declaration.
     *
     * @param node
     */
    @Override
    public void visit(VarDecl node) {
        node.type.accept(this);
        node.id.accept(this);

        String variableName = node.id.getName();
        if (this.isGlobalVariable(variableName)) {

            VariableEntry variableEntry = symbolTable.getVariableEntry(variableName);
            Type type = variableEntry.getType();

            emitCode(".field public " + variableName + " " + this.getJasminType(type));
        }
    }

    /**
     * creates Jasmin code for a method declaration.
     *
     * @param node
     */
    @Override
    public void visit(MethodDecl node) {

        enterMethodScope(node.methodId.getName());

        /* emit Jasmin directives */
        Type classname = this.getCurrentClassname();
        emitCode(".method public " + this.getJasminMethod(classname, node.methodId.getName()));
        emitCode(".limit stack 20"); // TODO (UG): calc minimal stack size
        emitCode(".limit locals " + (node.formalList.size() + node.varDecls.size() + 1));

        // Do not visit result type and method id
        // n.resultType.accept(this);
        // n.methodId.accept(this);
        //
        // Do not visit formal parameters
        // for ( int i = 0; i < n.formalList.size(); i++ ) {
        // n.formalList.elementAt(i).accept(this);
        // }
        //
        // Do not visit variable declarations
        // for ( int i = 0; i < n.varDecls.size(); i++ ) {
        // n.varDecls.elementAt(i).accept(this);
        // }
        //
        // emit code for method body
        for (int i = 0; i < node.statements.size(); i++) {

            /* get one statement */
            Statement s = node.statements.elementAt(i);
            /* create a new label for each statement */
            s.setNextLabel(this.newLabel(Label.NEXT));
            /* emit code for statement */
            s.accept(this);
            /* emit label after statement */
            this.emitLabel(s.getNextLabel());
        }

        // emit code for return expression
        node.returnExp.accept(this);

        emitCode(this.getJasminInstructionType(node.returnExp.getType()) + "return");
        emitCode(".end method");

        // leave current method scope
        leaveMethodScope();
    }

    /**
     * creates Jasmin code for a formal method parameter.
     *
     * @param node
     */
    @Override
    public void visit(Formal node) {
        node.type.accept(this);
        node.id.accept(this);
    }

    /**
     * nothing to do
     *
     * @param node
     */
    @Override
    public void visit(IntArrayTyping node) {
    }

    /**
     * nothing to do
     *
     * @param node
     */
    @Override
    public void visit(BooleanTyping node) {
    }

    /**
     * nothing to do
     *
     * @param node
     */
    @Override
    public void visit(IntegerTyping node) {
    }

    /**
     * empty function.
     *
     * @param node
     */
    @Override
    public void visit(IdentifierTyping node) {
    }

    /*
     * ========================================================================
     * GENERATE TAC FOR STATEMENTS
     * =====================================================================
     */

    /**
     * creates Jasmin code for a block of statements.
     *
     * @param node
     */
    @Override
    public void visit(Block node) {
        // TODO (Blatt 04)

        for (int i = 0; i < node.statements.size(); i++) {

            // get a statement
            Statement s = node.statements.elementAt(i);
            s.accept(this);
        }
    }

    /**
     * creates Jasmin code for an if statement.
     *
     * @param node
     */
    @Override
    public void visit(If node) {
        // TODO (Blatt 04)

        node.exp.accept(this);
        node.thenStatement.accept(this);
        node.elseStatement.accept(this);
    }

    /**
     * creates Jasmin code for a while statement.
     *
     * @param node
     */
    @Override
    public void visit(While node) {
        // TODO (Blatt 04)

        node.exp.accept(this);
        node.statement.accept(this);
    }

    /**
     * creates Jasmin code for a println statement.
     *
     * @param node
     */
    @Override
    public void visit(Print node) {

        this.emitCode("getstatic java/lang/System/out Ljava/io/PrintStream;");

        /* emit code for expression */
        node.exp.accept(this);

        /* emit code for printing print top stack element as int */
        this.emitCode("invokevirtual java/io/PrintStream/println(I)V");
    }

    /**
     * creates Jasmin code for an assignment.
     *
     * @param node
     */
    @Override
    public void visit(Assign node) {
        // TODO (Blatt 04)

        node.id.accept(this);
        node.exp.accept(this);
    }

    /**
     * creates Jasmin code for an assignment to an array element.
     *
     * @param node
     */
    @Override
    public void visit(ArrayAssign node) {
        // TODO (Blatt 04)

        node.arrayId.accept(this);
        node.index.accept(this);
        node.exp.accept(this);
    }

    /*
     * ========================================================================
     * GENERATE TAC FOR EXPRESSIONS
     * =====================================================================
     */

    /**
     * creates Jasmin code for a boolean conjunction.
     *
     * @param node
     */
    @Override
    public void visit(And node) {
        // TODO (Blatt 04)

        node.left.accept(this);
        node.right.accept(this);
    }

    /**
     * creates Jasmin code for a less than relation.
     *
     * @param node
     */
    @Override
    public void visit(LessThan node) {
        // TODO (Blatt 04)

        node.left.accept(this);
        node.right.accept(this);
    }

    /**
     * creates Jasmin code for an addition.
     *
     * @param node
     */
    @Override
    public void visit(Plus node) {
        // TODO (Blatt 04)

        node.left.accept(this);
        node.right.accept(this);
    }

    /**
     * creates Jasmin code for a subtraction.
     *
     * @param node
     */
    @Override
    public void visit(Minus node) {
        // TODO (Blatt 04)

        node.left.accept(this);
        node.right.accept(this);
    }

    /**
     * creates Jasmin code for a multiplication.
     *
     * @param node
     */
    @Override
    public void visit(Times node) {
        // TODO (Blatt 04)

        node.left.accept(this);
        node.right.accept(this);
    }

    /**
     * creates Jasmin code for accessing an elements of an array.
     *
     * @param node
     */
    @Override
    public void visit(ArrayLookup node) {
        // TODO (Blatt 04)

        node.arrayId.accept(this);
        node.index.accept(this);
    }

    /**
     * creates Jasmin code for retrieving the length of an array.
     *
     * @param node
     */
    @Override
    public void visit(ArrayLength node) {
        // TODO (Blatt 04)

        node.arrayId.accept(this);
    }

    /**
     * creates Jasmin code for a method call.
     *
     * @param node
     */
    @Override
    public void visit(Call node) {
        // TODO (Blatt 04)

        node.exp.accept(this);
        node.methodId.accept(this);

        for (int i = 0; i < node.expList.size(); i++) {
            node.expList.elementAt(i).accept(this);
        }
    }

    /**
     * creates Jasmin code for an integer literal.
     *
     * @param node
     */
    @Override
    public void visit(IntegerLiteral node) {
        // TODO (Blatt 04)
    }

    /**
     * creates Jasmin code for the boolean value true.
     *
     * @param node
     */
    @Override
    public void visit(True node) {
        // TODO (Blatt 04)
    }

    /**
     * creates Jasmin code for the boolean value false.
     *
     * @param node
     */
    @Override
    public void visit(False node) {
        // TODO (Blatt 04)
    }

    /**
     * creates Jasmin code for the access to an identifier.
     *
     * @param node
     */
    @Override
    public void visit(IdentifierExp node) {
        // TODO (Blatt 04)
    }

    /**
     * creates Jasmin code for an access to 'this' object.
     *
     * @param node
     */
    @Override
    public void visit(This node) {
        // TODO (Blatt 04)
    }

    /**
     * creates Jasmin code for an array creation.
     *
     * @param node
     */
    @Override
    public void visit(NewArray node) {
        // TODO (Blatt 04)

        node.exp.accept(this);
    }

    /**
     * creates Jasmin code for an object creation.
     *
     * @param node
     */
    @Override
    public void visit(NewObject node) {
        // TODO (Blatt 04)

    }

    /**
     * creates Jasmin code for a negation.
     *
     * @param node
     */
    @Override
    public void visit(Not node) {
        // TODO (Blatt 04)

        node.exp.accept(this);
    }

    /**
     * creates Jasmin code for an identifier.
     *
     * @param node node of an identifier
     */
    @Override
    public void visit(Identifier node) {
        // TODO (Blatt 04)

    }

    /*
     * ========================================================================
     * helpers
     * =====================================================================
     */
    /**
     * Opens the Jasmin output file for a given class.
     *
     * @param classname name of a Minijava class
     * @return PrintWriter, if the file could be opened successfully, null
     *         otherwise
     */
    private PrintWriter openWriter(String classname) {

        PrintWriter newWriter = null;

        try {
            String full_filename = jasminDir + "/" + classname + ".j";
            newWriter = new PrintWriter(new FileOutputStream(full_filename));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return newWriter;
    }

    /*
     * ========================================================================
     * handling scope
     * =====================================================================
     */
    /**
     * enters the scope of the class given by classname.
     *
     * @param classname name of a Minijava class
     */
    private void enterClassScope(String classname) {
        this.symbolTable = ((ProgramTable) this.symbolTable).getClass(classname);
    }

    /**
     * leaves the current class scope.
     */
    private void leaveClassScope() {
        this.symbolTable = this.symbolTable.getParent();
    }

    /**
     * enters the scope of the class given by methodname.
     *
     * @param methodname name of a Minjava method
     */
    private void enterMethodScope(String methodname) {
        this.symbolTable = ((ClassTable) this.symbolTable).getMethod(methodname);
    }

    /**
     * leaves the current method scope.
     */
    private void leaveMethodScope() {
        this.symbolTable = this.symbolTable.getParent();
    }

    /*
     * ========================================================================
     * methods for code generation
     * =====================================================================
     */
    /**
     * Writes the given code string to the current Jasmin file and to standard
     * output. Jasmin directives start at column 1, instructions at column 5
     *
     * @param code
     */
    private void emitCode(String code) {

        if (code.charAt(0) != '.') {
            writer.print("    ");
            if (verbose) {
                System.out.print("    ");
            }
        }
        writer.println(code);
        if (verbose) {
            System.out.println(code);
        }
    }

    /**
     * Writes the given string as a label to the current Jasmin file and to
     * standard output. Labels start at column 3.
     *
     * @param label Label to be written
     */
    private void emitLabel(String label) {
        writer.println("  " + label + ":");
        if (verbose) {
            System.out.println("  " + label + ":");
        }
    }

    /*
     * ========================================================================
     * methods for label handling
     * =====================================================================
     */
    /**
     * Generates a new label composed from a unique label number and the String
     * "kind", e.g. L1_NEXT or L2_FALSE.
     *
     * @param kind String from enumeration Label
     * @return Label
     */
    private String newLabel(Label kind) {

        nextLabelNo++;

        return "L" + nextLabelNo + "_" + kind;
    }

    /*
     * ========================================================================
     * methods for type handling
     * =====================================================================
     */
    /**
     * For a given Minijava type this method computes the prefix 'i' or 'a' used
     * in Jasmin load and store instructions, i.e. for IntegerType and
     * BooleanType the prefix "i" and for all other types the prefix "a" is
     * delivered.
     *
     * @param type Minijava type
     * @return Jasmin prefix
     */
    private String getJasminInstructionType(Type type) {

        if (type == Type.INT) {
            return "i";
        } else if (type == Type.BOOLEAN) {
            return "i";
        } else {
            return "a";
        }
    }

    /**
     * translates a Minijava type to a Jasmin type.
     *
     * @param type name of a Minijava type
     * @return Jasmin type
     */
    private String getJasminType(Type type) {

        if (type == Type.INT) {
            return "I";

        } else if (type == Type.BOOLEAN) {
            return "Z";

        } else if (type == Type.INT_ARRAY) {
            return "[I";

        } else if (type == Type.VOID) {
            return "";

        } else {
            return "L" + type + ";";
            /* class name */
        }
    }

    /**
     * retrieves the method signature in Jasmin syntax of Minijava class and
     * method.
     *
     * @param classname  name of Minijava class
     * @param methodname name of Minijava method
     * @return Jasmin method signature
     */
    private String getJasminMethod(Type classname, String methodname) {
        MethodTable methodTable = this.getMethodTable(classname, methodname);

        String string = methodTable.getName() + "(";
        for (Type type : methodTable.getFormalParameterTypes()) {
            string += this.getJasminType(type);
        }
        string += ")" + this.getJasminType(methodTable.getResultType());

        return string;
    }

    /*
     * ========================================================================
     * accessing symbol table
     * =====================================================================
     */
    /**
     * retrieves the class name of the current Minijava class.
     */
    private Type getCurrentClassname() {
        /*
         * the current scope is method scope, so the parent is the current
         * class scope
         */
        return ((ClassTable) this.symbolTable.getParent()).getClassType();
    }

    /**
     * retrieves the method table specified by class and method name.
     *
     * @param type       name of a Minijava class
     * @param methodname name of a Minijava method
     *
     * @return method table specified by class and method name
     */
    private MethodTable getMethodTable(Type type, String methodname) {

        return this.symbolTable.getClass(type.getName()).getMethod(methodname);
    }

    /**
     * returns the number of the Minijava variable named varName that
     * corresponds to the Jasmin variable for varName.
     *
     * @param varName name of a Minijava variable
     * @return number of corresponding Jasmin variable
     */
    private int getVariableNumber(String varName) {

        return this.symbolTable.getVariableEntry(varName).getOffset();
    }

    /**
     * checks if varname is a global variable or not.
     *
     * @param varname name of a Minijava variable
     * @return true if varname is a global variable and false otherwise
     */
    private boolean isGlobalVariable(String varname) {

        return this.symbolTable.getVariableEntry(varname).isGlobalVariable();
    }
}
