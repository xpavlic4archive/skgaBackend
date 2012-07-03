package com.laurinka.skga.server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nr"))
public class SkgaNumber implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private static final long serialVersionUID = 5974836977140765502L;
    @Basic(optional = false)
    private Date date;


    @Basic(optional = false)
    private String nr;

    public SkgaNumber(String nr) {
        this.nr = nr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @PrePersist
    public void init() {
        setDate(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

}