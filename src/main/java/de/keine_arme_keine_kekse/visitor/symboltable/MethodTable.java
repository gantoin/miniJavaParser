/*
 * MethodTable.java
 *
 */

package de.keine_arme_keine_kekse.visitor.symboltable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Objects of this class represent a symbol table of a Minijava method.
 *
 * TODO (UG): Implementation includes no error handling
 */
public class MethodTable extends SymbolTable {

    /**
     * name of the Minijava method
     */
    private final String name;

    /**
     * list of all types of formal parameters in the order of the source file
     */
    private final ArrayList<Type> formalParameterTypes;

    /**
     * result type of the Minijava method
     */
    private final Type resultType;

    /**
     * represents a mapping from the names of the formal parameters defined in
     * this class to their types.
     */
    private final HashMap<String, VariableEntry> formalParameters;

    /**
     * represents a mapping from the names of the local variables defined in
     * this class to their types.
     */
    private final HashMap<String, VariableEntry> localVariables;

    /**
     * create a new instance for a given method name and a result type of the
     * Minijava method
     */
    public MethodTable(ClassTable parent, String name, Type resultType) {

        super(parent);

        this.name = name;
        this.formalParameterTypes = new ArrayList<>();
        this.resultType = resultType;

        this.formalParameters = new HashMap<>();
        this.localVariables = new HashMap<>();
    }

    /**
     * returns the name of the method
     */
    public String getName() {
        return this.name;
    }

    /**
     * returns the types of the formal parameters.
     */
    public ArrayList<Type> getFormalParameterTypes() {

        return formalParameterTypes;
    }

    /**
     * returns the result type of the method
     */
    public Type getResultType() {
        return this.resultType;
    }

    /**
     * adds a new formal parameter (name, type)
     */
    public void putFormalParameter(String name, Type type, int offset) {

        formalParameters.put(name, new VariableEntry(name, type, offset));
        formalParameterTypes.add(type);
    }

    /**
     * adds a new local variable (name, type)
     */
    public void putVariable(String name, Type type, int offset) {
        localVariables.put(name, new VariableEntry(name, type, offset));
    }

    /**
     * returns the type of the given identifier. First, it is checked
     * whether it is a local variable, if not, the table of formal parameters
     * ist checked. The method returns null, if name is neither a local
     * variable nor a formal parameter.
     */
    public VariableEntry getVariableEntry(String name) {

        VariableEntry type;

        type = localVariables.get(name);
        if (type != null) {
            return type;
        }

        type = formalParameters.get(name);
        if (type != null) {
            return type;
        }

        return this.parent.getVariableEntry(name);
    }

    /**
     * returns a textual representation of the class table
     */
    public String toString() {
        StringBuilder string = new StringBuilder(this.getResultType() + " ");

        string.append(this.getName()).append("(");
        string.append(this.getFormalParameterTypes()).append(");");

        string.append("\n      Formal Parameters\n");

        for (VariableEntry entry : this.formalParameters.values()) {
            string.append("        ").append(entry).append("\n");
        }

        string.append("      Local Variables\n");
        for (VariableEntry entry : this.localVariables.values()) {
            string.append("        ").append(entry).append("\n");
        }

        return string.toString();
    }
}
