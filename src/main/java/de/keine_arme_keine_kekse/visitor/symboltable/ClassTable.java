/*
 * ClassTable.java
 */

package de.keine_arme_keine_kekse.visitor.symboltable;

import java.util.HashMap;

/**
 * Objects of this class represent a symbol table of a Minijava class.
 *
 * TODO (UG): Implementation includes no error handling
 */
public class ClassTable extends SymbolTable {

    /**
     * name of the Minijava class
     */
    private final String classname;

    /**
     * type of the Minijava class
     */
    private final Type classType;

    /**
     * type of the Minijava super class
     */
    private Type superClass;

    /**
     * represents a mapping from the names of the global variables defined in
     * this class to their types.
     */
    private final HashMap<String, VariableEntry> globalVariables;

    /**
     * represents a mapping from the names of the methods defined in this class
     * to their method tables (i.e. all identifiers defined in this method)
     */
    private final HashMap<String, MethodTable> methodTables;

    /**
     * creates a new instance for a given class name. Initially the created
     * instance has no super class, no global variables and methods.
     */
    public ClassTable(ProgramTable parent, String classname) {

        super(parent);

        this.classname = classname;
        this.classType = new Type(classname);
        this.superClass = null;
        this.globalVariables = new HashMap<>();
        this.methodTables = new HashMap<>();
    }

    /**
     * sets the super class name
     */
    public Type getClassType() {
        return this.classType;
    }

    /**
     * sets the super class name
     */
    public void setSuperClass(String superClass) {
        this.superClass = getParent().getClass(superClass).getClassType();
    }

    /**
     * returns the super class name
     */
    public Type getSuperClass() {
        return this.superClass;
    }

    /**
     * adds a new global variable (name, type)
     */
    public void putVariable(String name, Type type) {
        globalVariables.put(name, new VariableEntry(name, type));
    }

    /**
     * adds a new method (methodName, resultType)
     */
    public MethodTable putMethod(String methodName, Type resultType) {

        MethodTable methodTable = new MethodTable(this, methodName, resultType);
        methodTables.put(methodName, methodTable);

        return methodTable;
    }

    /**
     * returns the method table for method named methodName
     */
    public MethodTable getMethod(String methodName) {
        return methodTables.get(methodName);
    }

    /**
     * returns the type of the global variable named name
     */
    public VariableEntry getVariableEntry(String name) {

        VariableEntry type = globalVariables.get(name);
        if (type != null) {
            return type;
        }

        return this.parent.getClass(superClass.getName()).getVariableEntry(name);
    }

    /**
     * returns a textual representation of the class table
     */
    public String toString() {
        StringBuilder string = new StringBuilder(classname + "\n");

        if (this.superClass != null) {
            string.append(" - extends ").append(superClass).append("\n");
        }

        string.append(" - Global variables\n");
        for (String varName : globalVariables.keySet()) {
            VariableEntry type = globalVariables.get(varName);
            string.append("      ").append(type).append("\n");
        }

        string.append(" - Methods\n");
        for (String methodName : methodTables.keySet()) {
            MethodTable methodtable = methodTables.get(methodName);
            string.append("      ").append(methodtable.toString()).append("\n");
        }

        return string.toString();
    }
}
