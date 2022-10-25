package de.keine_arme_keine_kekse.visitor;

public class SemanticException extends RuntimeException {
    public SemanticException(String errorMessage) {
        super(errorMessage);
    }
}
