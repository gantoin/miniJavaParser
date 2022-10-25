package de.keine_arme_keine_kekse.visitor.symboltable;

import java.util.Objects;

public class Type {

    /**
     * Name of a class
     */
    String lexeme;

    /**
     * Creates a new instance
     *
     * @param lexeme
     */
    public Type(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getName() {
        return lexeme;
    }

    /**
     * Predefined types
     */
    public static final Type BOOLEAN = new Type("boolean");
    public static final Type INT = new Type("int");
    public static final Type VOID = new Type("void");
    public static final Type INT_ARRAY = new Type("intarray");
    public static final Type STRING = new Type("java/lang/String;");

    @Override
    public String toString() {
        return this.lexeme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Type type = (Type) o;
        return Objects.equals(lexeme, type.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme);
    }
}
