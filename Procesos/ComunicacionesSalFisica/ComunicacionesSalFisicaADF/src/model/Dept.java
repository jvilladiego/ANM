package model;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = "Dept.findAll", query = "select o from Dept o") })
public class Dept implements Serializable {
    private static final long serialVersionUID = -6991004515599731257L;
    @Column(length = 100)
    private String department;
    @Id
    @Column(nullable = false)
    private Integer id;
    @OneToMany(mappedBy = "dept", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Emp> empList;

    public Dept() {
    }

    public Dept(String department, Integer id) {
        this.department = department;
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Emp> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Emp> empList) {
        this.empList = empList;
    }

    public Emp addEmp(Emp emp) {
        getEmpList().add(emp);
        emp.setDept(this);
        return emp;
    }

    public Emp removeEmp(Emp emp) {
        getEmpList().remove(emp);
        emp.setDept(null);
        return emp;
    }
}
