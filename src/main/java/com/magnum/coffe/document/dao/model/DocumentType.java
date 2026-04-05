package com.magnum.coffe.document.dao.model;

public enum DocumentType {
    PERMIS,
    CIN,
    AUTORISATION_TAXI,
    CARTE_GRISE,
    ASSURANCE,
    CARTE_VIGNETTE,
    CARTE_PROFESSIONNELLE,
    photoChauffeur;
    public static DocumentType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (DocumentType type : DocumentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null; // ou tu peux lancer une exception IllegalArgumentException
    }
}
