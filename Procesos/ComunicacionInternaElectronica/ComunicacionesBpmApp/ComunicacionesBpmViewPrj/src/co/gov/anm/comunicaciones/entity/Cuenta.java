package co.gov.anm.comunicaciones.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Cuenta.findAll", query = "select o from Cuenta o") })
@Table(name = "DOCUMENTACCOUNTS")
public class Cuenta implements Serializable {
    private static final long serialVersionUID = 6502341045313523061L;
    @Id
    @Column(nullable = false, length = 80)
    private String ddocaccount;

    public Cuenta() {
    }

    public Cuenta(String ddocaccount) {
        this.ddocaccount = ddocaccount;
    }

    public String getDdocaccount() {
        return ddocaccount;
    }

    public void setDdocaccount(String ddocaccount) {
        this.ddocaccount = ddocaccount;
    }
}
