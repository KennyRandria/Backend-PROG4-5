package com.patrimoine.patrimoine.model;

import java.time.LocalDateTime;

public class Patrimoine {
    private String id; // L'ID doit être ici
    private String possesseur;
    private LocalDateTime derniereModification;

    // Constructeurs
    public Patrimoine(String id, String possesseur, LocalDateTime derniereModification) {
        this.id = id;
        this.possesseur = possesseur;
        this.derniereModification = derniereModification;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPossesseur() {
        return possesseur;
    }

    public void setPossesseur(String possesseur) {
        this.possesseur = possesseur;
    }

    public LocalDateTime getDerniereModification() {
        return derniereModification;
    }

    public void setDerniereModification(LocalDateTime derniereModification) {
        this.derniereModification = derniereModification;
    }
}
