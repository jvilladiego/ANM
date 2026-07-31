package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Emp.findAll", query = "select o from Emp o") })
public class Emp implements Serializable {
    private static final long serialVersionUID = -4262748708253947551L;
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(length = 100)
    private String lastname;
    @Column(length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTMENT")
    private Dept dept;

    public Emp() {
    }

    public Emp(Integer id, Dept dept, String lastname, String name) {
        this.id = id;
        this.dept = dept;
        this.lastname = lastname;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
