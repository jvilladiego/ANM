package model;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "PsTxn.findAll", query = "select o from PsTxn o") })
@Table(name = "PS_TXN")
@IdClass(PsTxnPK.class)
public class PsTxn implements Serializable {
    private static final long serialVersionUID = -6797382636394963053L;
    @Id
    @Column(nullable = false)
    private Long collid;
    private byte[] content;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    @Id
    @Column(nullable = false)
    private BigDecimal id;
    private BigDecimal parentid;

    public PsTxn() {
    }

    public PsTxn(Long collid, Date creationDate, BigDecimal id, BigDecimal parentid) {
        this.collid = collid;
        this.creationDate = creationDate;
        this.id = id;
        this.parentid = parentid;
    }

    public Long getCollid() {
        return collid;
    }

    public void setCollid(Long collid) {
        this.collid = collid;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getParentid() {
        return parentid;
    }

    public void setParentid(BigDecimal parentid) {
        this.parentid = parentid;
    }
}
