package co.gov.anm.comunicaciones.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Folders.findAll", query = "select o from Folders o"), 
                @NamedQuery(name = "Folders.findByParentGuid", 
                            query = "select o from Folders o where o.fparentguid = :p_parent_guid order by o.ffoldername"),
                @NamedQuery(name = "Folders.findByFolderName", 
                            query = "select o from Folders o where o.ffoldername = :p_folder_name order by o.ffoldername")
				})
@Table(name = "FOLDERFOLDERS")
public class Folders implements Serializable {
    private static final long serialVersionUID = -8257002226204242404L;
    private BigDecimal fallocatedfoldersize;
    private String fallocatorparentfolderguid;
    @Column(nullable = false, length = 100)
    private String fapplication;
    @Column(length = 292)
    private String fapplicationguid;
    private BigDecimal fchildfilescount;
    private BigDecimal fchildfolderscount;
    @Column(length = 2000)
    private String fclbraaliaslist;
    @Column(length = 2000)
    private String fclbrarolelist;
    @Column(length = 2000)
    private String fclbrauserlist;
    @Column(nullable = false)
    private Timestamp fcreatedate;
    @Column(nullable = false, length = 200)
    private String fcreator;
    @Column(length = 80)
    private String fdocaccount;
    @Column(length = 2000)
    private String fdocclasses;
    @Column(length = 2000)
    private String ffolderdescription;
    @Id
    @Column(nullable = false)
    private String ffolderguid;
    @Column(nullable = false)
    private String ffoldername;
    private BigDecimal ffoldersize;
    @Column(length = 50)
    private String ffoldertype;
    @Column(nullable = false)
    private BigDecimal finhibitpropagation;
    @Column(nullable = false)
    private BigDecimal fiscontribution;
    private BigDecimal fisintrash;
    private BigDecimal fislibrary;
    private BigDecimal fisreadonly;
    @Column(nullable = false)
    private Timestamp flastmodifieddate;
    @Column(nullable = false, length = 200)
    private String flastmodifier;
    private BigDecimal flibrarytype;
    @Column(nullable = false, length = 200)
    private String fowner;
    @Column(nullable = false)
    private String fparentguid;
    private BigDecimal fpromptformetadata;
    private String frealitemguid;
    @Column(nullable = false, length = 30)
    private String fsecuritygroup;
    private String ftargetguid;

    public Folders() {
    }

    public Folders(BigDecimal fallocatedfoldersize, String fallocatorparentfolderguid, String fapplication,
                   String fapplicationguid, BigDecimal fchildfilescount, BigDecimal fchildfolderscount,
                   String fclbraaliaslist, String fclbrarolelist, String fclbrauserlist, Timestamp fcreatedate,
                   String fcreator, String fdocaccount, String fdocclasses, String ffolderdescription,
                   String ffolderguid, String ffoldername, BigDecimal ffoldersize, String ffoldertype,
                   BigDecimal finhibitpropagation, BigDecimal fiscontribution, BigDecimal fisintrash,
                   BigDecimal fislibrary, BigDecimal fisreadonly, Timestamp flastmodifieddate, String flastmodifier,
                   BigDecimal flibrarytype, String fowner, String fparentguid, BigDecimal fpromptformetadata,
                   String frealitemguid, String fsecuritygroup, String ftargetguid) {
        this.fallocatedfoldersize = fallocatedfoldersize;
        this.fallocatorparentfolderguid = fallocatorparentfolderguid;
        this.fapplication = fapplication;
        this.fapplicationguid = fapplicationguid;
        this.fchildfilescount = fchildfilescount;
        this.fchildfolderscount = fchildfolderscount;
        this.fclbraaliaslist = fclbraaliaslist;
        this.fclbrarolelist = fclbrarolelist;
        this.fclbrauserlist = fclbrauserlist;
        this.fcreatedate = fcreatedate;
        this.fcreator = fcreator;
        this.fdocaccount = fdocaccount;
        this.fdocclasses = fdocclasses;
        this.ffolderdescription = ffolderdescription;
        this.ffolderguid = ffolderguid;
        this.ffoldername = ffoldername;
        this.ffoldersize = ffoldersize;
        this.ffoldertype = ffoldertype;
        this.finhibitpropagation = finhibitpropagation;
        this.fiscontribution = fiscontribution;
        this.fisintrash = fisintrash;
        this.fislibrary = fislibrary;
        this.fisreadonly = fisreadonly;
        this.flastmodifieddate = flastmodifieddate;
        this.flastmodifier = flastmodifier;
        this.flibrarytype = flibrarytype;
        this.fowner = fowner;
        this.fparentguid = fparentguid;
        this.fpromptformetadata = fpromptformetadata;
        this.frealitemguid = frealitemguid;
        this.fsecuritygroup = fsecuritygroup;
        this.ftargetguid = ftargetguid;
    }

    public BigDecimal getFallocatedfoldersize() {
        return fallocatedfoldersize;
    }

    public void setFallocatedfoldersize(BigDecimal fallocatedfoldersize) {
        this.fallocatedfoldersize = fallocatedfoldersize;
    }

    public String getFallocatorparentfolderguid() {
        return fallocatorparentfolderguid;
    }

    public void setFallocatorparentfolderguid(String fallocatorparentfolderguid) {
        this.fallocatorparentfolderguid = fallocatorparentfolderguid;
    }

    public String getFapplication() {
        return fapplication;
    }

    public void setFapplication(String fapplication) {
        this.fapplication = fapplication;
    }

    public String getFapplicationguid() {
        return fapplicationguid;
    }

    public void setFapplicationguid(String fapplicationguid) {
        this.fapplicationguid = fapplicationguid;
    }

    public BigDecimal getFchildfilescount() {
        return fchildfilescount;
    }

    public void setFchildfilescount(BigDecimal fchildfilescount) {
        this.fchildfilescount = fchildfilescount;
    }

    public BigDecimal getFchildfolderscount() {
        return fchildfolderscount;
    }

    public void setFchildfolderscount(BigDecimal fchildfolderscount) {
        this.fchildfolderscount = fchildfolderscount;
    }

    public String getFclbraaliaslist() {
        return fclbraaliaslist;
    }

    public void setFclbraaliaslist(String fclbraaliaslist) {
        this.fclbraaliaslist = fclbraaliaslist;
    }

    public String getFclbrarolelist() {
        return fclbrarolelist;
    }

    public void setFclbrarolelist(String fclbrarolelist) {
        this.fclbrarolelist = fclbrarolelist;
    }

    public String getFclbrauserlist() {
        return fclbrauserlist;
    }

    public void setFclbrauserlist(String fclbrauserlist) {
        this.fclbrauserlist = fclbrauserlist;
    }

    public Timestamp getFcreatedate() {
        return fcreatedate;
    }

    public void setFcreatedate(Timestamp fcreatedate) {
        this.fcreatedate = fcreatedate;
    }

    public String getFcreator() {
        return fcreator;
    }

    public void setFcreator(String fcreator) {
        this.fcreator = fcreator;
    }

    public String getFdocaccount() {
        return fdocaccount;
    }

    public void setFdocaccount(String fdocaccount) {
        this.fdocaccount = fdocaccount;
    }

    public String getFdocclasses() {
        return fdocclasses;
    }

    public void setFdocclasses(String fdocclasses) {
        this.fdocclasses = fdocclasses;
    }

    public String getFfolderdescription() {
        return ffolderdescription;
    }

    public void setFfolderdescription(String ffolderdescription) {
        this.ffolderdescription = ffolderdescription;
    }

    public String getFfolderguid() {
        return ffolderguid;
    }

    public void setFfolderguid(String ffolderguid) {
        this.ffolderguid = ffolderguid;
    }

    public String getFfoldername() {
        return ffoldername;
    }

    public void setFfoldername(String ffoldername) {
        this.ffoldername = ffoldername;
    }

    public BigDecimal getFfoldersize() {
        return ffoldersize;
    }

    public void setFfoldersize(BigDecimal ffoldersize) {
        this.ffoldersize = ffoldersize;
    }

    public String getFfoldertype() {
        return ffoldertype;
    }

    public void setFfoldertype(String ffoldertype) {
        this.ffoldertype = ffoldertype;
    }

    public BigDecimal getFinhibitpropagation() {
        return finhibitpropagation;
    }

    public void setFinhibitpropagation(BigDecimal finhibitpropagation) {
        this.finhibitpropagation = finhibitpropagation;
    }

    public BigDecimal getFiscontribution() {
        return fiscontribution;
    }

    public void setFiscontribution(BigDecimal fiscontribution) {
        this.fiscontribution = fiscontribution;
    }

    public BigDecimal getFisintrash() {
        return fisintrash;
    }

    public void setFisintrash(BigDecimal fisintrash) {
        this.fisintrash = fisintrash;
    }

    public BigDecimal getFislibrary() {
        return fislibrary;
    }

    public void setFislibrary(BigDecimal fislibrary) {
        this.fislibrary = fislibrary;
    }

    public BigDecimal getFisreadonly() {
        return fisreadonly;
    }

    public void setFisreadonly(BigDecimal fisreadonly) {
        this.fisreadonly = fisreadonly;
    }

    public Timestamp getFlastmodifieddate() {
        return flastmodifieddate;
    }

    public void setFlastmodifieddate(Timestamp flastmodifieddate) {
        this.flastmodifieddate = flastmodifieddate;
    }

    public String getFlastmodifier() {
        return flastmodifier;
    }

    public void setFlastmodifier(String flastmodifier) {
        this.flastmodifier = flastmodifier;
    }

    public BigDecimal getFlibrarytype() {
        return flibrarytype;
    }

    public void setFlibrarytype(BigDecimal flibrarytype) {
        this.flibrarytype = flibrarytype;
    }

    public String getFowner() {
        return fowner;
    }

    public void setFowner(String fowner) {
        this.fowner = fowner;
    }

    public String getFparentguid() {
        return fparentguid;
    }

    public void setFparentguid(String fparentguid) {
        this.fparentguid = fparentguid;
    }

    public BigDecimal getFpromptformetadata() {
        return fpromptformetadata;
    }

    public void setFpromptformetadata(BigDecimal fpromptformetadata) {
        this.fpromptformetadata = fpromptformetadata;
    }

    public String getFrealitemguid() {
        return frealitemguid;
    }

    public void setFrealitemguid(String frealitemguid) {
        this.frealitemguid = frealitemguid;
    }

    public String getFsecuritygroup() {
        return fsecuritygroup;
    }

    public void setFsecuritygroup(String fsecuritygroup) {
        this.fsecuritygroup = fsecuritygroup;
    }

    public String getFtargetguid() {
        return ftargetguid;
    }

    public void setFtargetguid(String ftargetguid) {
        this.ftargetguid = ftargetguid;
    }
}
