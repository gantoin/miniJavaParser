/*
 * ProgramTable.java
 *
 */

package de.keine_arme_keine_kekse.visitor.symboltable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.keine_arme_keine_kekse.syntaxtree.Identifier;
import de.keine_arme_keine_kekse.syntaxtree.IdentifierTyping;

/**
 * Objects of this class represent a symbol table of a Minijava program.
 *
 * TODO (UG): Implementation includes no error handling
 */
public class ProgramTable extends SymbolTable {

    /**
     * represents a mapping from class names to their class tables (i.e. all
     * identifiers defined within this class)
     */
    private final HashMap<String, ClassTable> classTables;

    private final Set<String> reservedClassNames;

    /**
     * Create a new instance with an empty mapping from classes to claa tables
     */
    public ProgramTable() {

        /* Create symbol table without surrounding environment (symbol table) */
        super(null);
        classTables = new HashMap<>();
        reservedClassNames = new HashSet<>();
    }

    /**
     * adds a new class with an empty class table
     */
    public ClassTable putClass(Identifier identifier) {

        String className = identifier.getName();
        reservedClassNames.remove(className);

        ClassTable classTable = new ClassTable(this, className);
        classTables.put(className, classTable);

        return classTable;
    }

    /**
     * returns the class table for a given class name, returns null if the
     * mapping does not include classname
     */
    public ClassTable getClass(String classname) {
        return classTables.get(classname);
    }

    public ClassTable reserveClass(IdentifierTyping identifierTyping) {

        String className = identifierTyping.getName();

        reservedClassNames.add(className);

        ClassTable classTable = new ClassTable(this, className);
        classTables.put(className, classTable);

        return classTable;
    }

    @Override
    public void cleanup() {
        for (String className : reservedClassNames) {
            classTables.remove(className);
        }
    }

    /**
     * returns a textual representation of the symbol table
     */
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (String classname : classTables.keySet()) {

            ClassTable classTable = classTables.get(classname);
            string.append(classTable);
        }

        return string.toString();
    }
}
