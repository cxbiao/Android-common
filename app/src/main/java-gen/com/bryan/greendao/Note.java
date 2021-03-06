package com.bryan.greendao;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "NOTE".
 */
@Entity
public class Note {

    @Id
    private Long id;

    @NotNull
    private String text;
    private String comment;
    private java.util.Date date;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    @Generated
    public Note(Long id, String text, String comment, java.util.Date date) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getText() {
        return text;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setText(@NotNull String text) {
        this.text = text;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
