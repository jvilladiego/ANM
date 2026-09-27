package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "folderfolders.findAll", query = "select o from folderfolders o"),
                @NamedQuery(name = "folderfolders.findParent", 
                            query = "select o from folderfolders o where o.FFOLDERNAME = 'ANM' order by o.FFOLDERNAME"), 
                @NamedQuery(name = "folderfolders.findByParentGuid", 
                            query = "select o from folderfolders o where o.FPARENTGUID = :param order by o.FFOLDERNAME") 
                })
public class folderfolders implements Serializable {
    private static final long serialVersionUID = 1649400392500617343L;

    @Id
    @Column(name = "FFOLDERGUID", nullable = false)
    private String FFOLDERGUID;
    @Column(name = "FPARENTGUID")
    private String FPARENTGUID;
    @Column(name = "FFOLDERNAME")
    private String FFOLDERNAME;

    public folderfolders() {
    }

    public void setFFOLDERGUID(String FFOLDERGUID) {
        this.FFOLDERGUID = FFOLDERGUID;
    }

    public String getFFOLDERGUID() {
        return FFOLDERGUID;
    }

    public void setFPARENTGUID(String FPARENTGUID) {
        this.FPARENTGUID = FPARENTGUID;
    }

    public String getFPARENTGUID() {
        return FPARENTGUID;
    }

    public void setFFOLDERNAME(String FFOLDERNAME) {
        this.FFOLDERNAME = FFOLDERNAME;
    }

    public String getFFOLDERNAME() {
        return FFOLDERNAME;
    }
}
