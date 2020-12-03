package com.example.sentry.model;

public class Article {
    String Name;
    String Email;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getArticle_Link() {
        return Article_Link;
    }

    public void setArticle_Link(String article_Link) {
        Article_Link = article_Link;
    }

    String Title;
    String Description;
    String Article_Link;
}
