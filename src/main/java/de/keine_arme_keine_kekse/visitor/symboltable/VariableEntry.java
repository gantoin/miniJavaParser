/*
 * VariableType.java
 *
 * Created on 14. Dezember 2007, 14:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.keine_arme_keine_kekse.visitor.symboltable;

/**
 * Class Type
 */
public class VariableEntry {

    /**
     * Offset für globale Variablen
     */
    private static final int OFFSET_GLOBALS = -1;

    /**
     * Name of variable
     */
    private String name;

    /**
     * Type of variable
     */
    private Type type;

    /**
     * Offset of variable
     */
    private int offset;

    /**
     * Creates a new instance for a global variable
     */
    public VariableEntry(String name, Type type) {
        this.name = name;
        this.type = type;
        this.offset = OFFSET_GLOBALS;
    }

    /**
     * Creates a new instance for a local variable
     */
    public VariableEntry(String name, Type type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    /**
     * Returns the type of the variable
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Returns the number of variable
     */
    public int getOffset() {
        return this.offset;
    }

    /** 
     * 
     */
    public Boolean isGlobalVariable() {
        return (this.offset == OFFSET_GLOBALS);
    }

    public String toString() {
        return this.name + " : " + this.type + " (" + this.offset + ")";
    }
}
