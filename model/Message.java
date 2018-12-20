package com.melmo.androidchat.model;

/**
 * Created by quentin for MyChat on 19/12/2018.
 */
public class Message {
    String message;
    //Id de l'utilisateur qui a créé ce message
    Utilisateur utilisateur;
    String creationdate;

    public Message(String message, Utilisateur utilisateur, String creationdate) {
        this.message = message;
        this.utilisateur = utilisateur;
        this.creationdate = creationdate;
    }

    public Message(String message, Utilisateur utilisateur) {
        this.message = message;
        this.utilisateur = utilisateur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }
}
