package com.aplikacja.Aplikacja.firmowa.model;

import lombok.Builder;

import javax.persistence.*;


//this class is only for admin
@Entity
@Table(name="documents")
public class Document {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    public Document(){

    }
    @Builder
    public Document(String title, String description){
        this.title=title;
        this.description=description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString(){
        return "Document [[id=" + id + ", title=" + title + ", desc=" +
                description+"]";
    }
}
