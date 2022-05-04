package com.example.app_movie;

import java.io.Serializable;
import java.sql.Array;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.List;

public class FilmClass implements Serializable {
    public String name;
    private String url_img;
    private String urlfilm;
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private List<String> actor;
    private List<String> director;
    private String description;
    private String document;
    private List<String> genre;
    public FilmClass(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public List<String> getActor() {
        return actor;
    }

    public void setActor(List<String> actor) {
        this.actor = actor;
    }

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getUrlfilm() {
        return urlfilm;
    }

    public void setUrlfilm(String urlfilm) {
        this.urlfilm = urlfilm;
    }

    public FilmClass(List<String>director, List<String> genre, String name, String description, String url_img,String url_film,List<String> Actor)
    {
        this.description=description;
        this.director=director;
        this.genre=genre;
        this.name=name;
        this.url_img=url_img;
        this.urlfilm=url_film;
        this.actor=Actor;
    }
    public FilmClass(String name,List<String >director,List<String> actor,List<String>genre,String description,String url_img, String url_film,String uri){
        this.description=description;
        this.director=director;
        this.genre=genre;
        this.name=name;
        this.url_img=url_img;
        this.urlfilm=url_film;
        this.actor=actor;
        this.uri=uri;
    }
}
