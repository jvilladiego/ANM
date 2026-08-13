package co.gov.anm.comunicaciones.model.entity;

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
@NamedQueries({ @NamedQuery(name = "Folder.findAll", query = "select o from Folder o") })
@Table(name = "FOLDERS")
public class Folder implements Serializable {
    private static final long serialVersionUID = -5053950531145791128L;
    @Column(length = 30)
    private String dauditperiod;
    @Column(length = 1000)
    private String dcategories;
    @Column(length = 100)
    private String dcategoryid;
    @Column(length = 100)
    private String dclassifiedmarkings;
    @Column(length = 1)
    private String dcontainsonly;
    private Timestamp ddeleteapprovedate;
    private BigDecimal ddepth;
    @Column(length = 1000)
    private String dderivedcategories;
    @Column(length = 100)
    private String dderivedcategoryid;
    @Column(length = 1000)
    private String dderivedfreezeid;
    private Integer dderivedisclosed;
    private Integer dderivedisfrozen;
    private Integer dderivedisvital;
    @Column(length = 1000)
    private String dderivednotifscripts;
    @Column(length = 1000)
    private String dderivedsecurityscripts;
    private BigDecimal dderivedvitalperiod;
    @Column(length = 32)
    private String dderivedvitalperiodunits;
    @Column(length = 200)
    private String dderivedvitalreviewer;
    @Column(length = 80)
    private String ddocaccount;
    @Column(length = 200)
    private String ddocauthor;
    @Column(length = 100)
    private String dexternallocation;
    @Column(length = 100)
    private String dexternallocation2;
    @Column(length = 1)
    private String dfileplantype;
    @Column(length = 1000)
    private String dfolderdescription;
    @Id
    @Column(nullable = false, length = 100)
    private String dfolderid;
    @Column(length = 100)
    private String dfoldername;
    @Column(length = 100)
    private String dfreezeid;
    @Column(length = 1000)
    private String dfreezereason;
    private Integer disclosed;
    private Integer discutoff;
    private Integer disexternal;
    @Column(length = 1)
    private String disfileplan;
    private Integer disfrozen;
    private Integer dissubjecttoaudit;
    private Integer disvital;
    private Timestamp dlastitemaddeddate;
    private Timestamp dlastmodifieddate;
    @Column(length = 1000)
    private String dnotificationscripts;
    @Column(length = 100)
    private String dparentid;
    private Timestamp drecordactivationdate;
    private Timestamp drecordcancelleddate;
    private Timestamp drecordcutoffdate;
    private Timestamp drecorddestroydate;
    private Timestamp drecordexpirationdate;
    private Timestamp drecordfilingdate;
    private Timestamp drecordobsoletedate;
    private Timestamp drecordrescindeddate;
    private Timestamp drecordreviewdate;
    private Timestamp drecordsupersededdate;
    @Column(length = 1)
    private String drmaprocessstate;
    private BigDecimal drmasegmentid;
    @Column(length = 30)
    private String drmprofiletrigger;
    @Column(length = 30)
    private String dsecuritygroup;
    @Column(length = 1000)
    private String dsecurityscripts;
    @Column(length = 100)
    private String dseriesid;
    @Column(length = 100)
    private String dsupplementalmarkings;
    private BigDecimal dvitalperiod;
    @Column(length = 32)
    private String dvitalperiodunits;
    @Column(length = 200)
    private String dvitalreviewer;
    @Column(length = 1)
    private String dvitalstate;
    @Column(length = 1000)
    private String xclbraaliaslist;
    @Column(length = 1000)
    private String xclbrauserlist;

    public Folder() {
    }

    public Folder(String dauditperiod, String dcategories, String dcategoryid, String dclassifiedmarkings,
                  String dcontainsonly, Timestamp ddeleteapprovedate, BigDecimal ddepth, String dderivedcategories,
                  String dderivedcategoryid, String dderivedfreezeid, Integer dderivedisclosed,
                  Integer dderivedisfrozen, Integer dderivedisvital, String dderivednotifscripts,
                  String dderivedsecurityscripts, BigDecimal dderivedvitalperiod, String dderivedvitalperiodunits,
                  String dderivedvitalreviewer, String ddocaccount, String ddocauthor, String dexternallocation,
                  String dexternallocation2, String dfileplantype, String dfolderdescription, String dfolderid,
                  String dfoldername, String dfreezeid, String dfreezereason, Integer disclosed, Integer discutoff,
                  Integer disexternal, String disfileplan, Integer disfrozen, Integer dissubjecttoaudit,
                  Integer disvital, Timestamp dlastitemaddeddate, Timestamp dlastmodifieddate,
                  String dnotificationscripts, String dparentid, Timestamp drecordactivationdate,
                  Timestamp drecordcancelleddate, Timestamp drecordcutoffdate, Timestamp drecorddestroydate,
                  Timestamp drecordexpirationdate, Timestamp drecordfilingdate, Timestamp drecordobsoletedate,
                  Timestamp drecordrescindeddate, Timestamp drecordreviewdate, Timestamp drecordsupersededdate,
                  String drmaprocessstate, BigDecimal drmasegmentid, String drmprofiletrigger, String dsecuritygroup,
                  String dsecurityscripts, String dseriesid, String dsupplementalmarkings, BigDecimal dvitalperiod,
                  String dvitalperiodunits, String dvitalreviewer, String dvitalstate, String xclbraaliaslist,
                  String xclbrauserlist) {
        this.dauditperiod = dauditperiod;
        this.dcategories = dcategories;
        this.dcategoryid = dcategoryid;
        this.dclassifiedmarkings = dclassifiedmarkings;
        this.dcontainsonly = dcontainsonly;
        this.ddeleteapprovedate = ddeleteapprovedate;
        this.ddepth = ddepth;
        this.dderivedcategories = dderivedcategories;
        this.dderivedcategoryid = dderivedcategoryid;
        this.dderivedfreezeid = dderivedfreezeid;
        this.dderivedisclosed = dderivedisclosed;
        this.dderivedisfrozen = dderivedisfrozen;
        this.dderivedisvital = dderivedisvital;
        this.dderivednotifscripts = dderivednotifscripts;
        this.dderivedsecurityscripts = dderivedsecurityscripts;
        this.dderivedvitalperiod = dderivedvitalperiod;
        this.dderivedvitalperiodunits = dderivedvitalperiodunits;
        this.dderivedvitalreviewer = dderivedvitalreviewer;
        this.ddocaccount = ddocaccount;
        this.ddocauthor = ddocauthor;
        this.dexternallocation = dexternallocation;
        this.dexternallocation2 = dexternallocation2;
        this.dfileplantype = dfileplantype;
        this.dfolderdescription = dfolderdescription;
        this.dfolderid = dfolderid;
        this.dfoldername = dfoldername;
        this.dfreezeid = dfreezeid;
        this.dfreezereason = dfreezereason;
        this.disclosed = disclosed;
        this.discutoff = discutoff;
        this.disexternal = disexternal;
        this.disfileplan = disfileplan;
        this.disfrozen = disfrozen;
        this.dissubjecttoaudit = dissubjecttoaudit;
        this.disvital = disvital;
        this.dlastitemaddeddate = dlastitemaddeddate;
        this.dlastmodifieddate = dlastmodifieddate;
        this.dnotificationscripts = dnotificationscripts;
        this.dparentid = dparentid;
        this.drecordactivationdate = drecordactivationdate;
        this.drecordcancelleddate = drecordcancelleddate;
        this.drecordcutoffdate = drecordcutoffdate;
        this.drecorddestroydate = drecorddestroydate;
        this.drecordexpirationdate = drecordexpirationdate;
        this.drecordfilingdate = drecordfilingdate;
        this.drecordobsoletedate = drecordobsoletedate;
        this.drecordrescindeddate = drecordrescindeddate;
        this.drecordreviewdate = drecordreviewdate;
        this.drecordsupersededdate = drecordsupersededdate;
        this.drmaprocessstate = drmaprocessstate;
        this.drmasegmentid = drmasegmentid;
        this.drmprofiletrigger = drmprofiletrigger;
        this.dsecuritygroup = dsecuritygroup;
        this.dsecurityscripts = dsecurityscripts;
        this.dseriesid = dseriesid;
        this.dsupplementalmarkings = dsupplementalmarkings;
        this.dvitalperiod = dvitalperiod;
        this.dvitalperiodunits = dvitalperiodunits;
        this.dvitalreviewer = dvitalreviewer;
        this.dvitalstate = dvitalstate;
        this.xclbraaliaslist = xclbraaliaslist;
        this.xclbrauserlist = xclbrauserlist;
    }

    public String getDauditperiod() {
        return dauditperiod;
    }

    public void setDauditperiod(String dauditperiod) {
        this.dauditperiod = dauditperiod;
    }

    public String getDcategories() {
        return dcategories;
    }

    public void setDcategories(String dcategories) {
        this.dcategories = dcategories;
    }

    public String getDcategoryid() {
        return dcategoryid;
    }

    public void setDcategoryid(String dcategoryid) {
        this.dcategoryid = dcategoryid;
    }

    public String getDclassifiedmarkings() {
        return dclassifiedmarkings;
    }

    public void setDclassifiedmarkings(String dclassifiedmarkings) {
        this.dclassifiedmarkings = dclassifiedmarkings;
    }

    public String getDcontainsonly() {
        return dcontainsonly;
    }

    public void setDcontainsonly(String dcontainsonly) {
        this.dcontainsonly = dcontainsonly;
    }

    public Timestamp getDdeleteapprovedate() {
        return ddeleteapprovedate;
    }

    public void setDdeleteapprovedate(Timestamp ddeleteapprovedate) {
        this.ddeleteapprovedate = ddeleteapprovedate;
    }

    public BigDecimal getDdepth() {
        return ddepth;
    }

    public void setDdepth(BigDecimal ddepth) {
        this.ddepth = ddepth;
    }

    public String getDderivedcategories() {
        return dderivedcategories;
    }

    public void setDderivedcategories(String dderivedcategories) {
        this.dderivedcategories = dderivedcategories;
    }

    public String getDderivedcategoryid() {
        return dderivedcategoryid;
    }

    public void setDderivedcategoryid(String dderivedcategoryid) {
        this.dderivedcategoryid = dderivedcategoryid;
    }

    public String getDderivedfreezeid() {
        return dderivedfreezeid;
    }

    public void setDderivedfreezeid(String dderivedfreezeid) {
        this.dderivedfreezeid = dderivedfreezeid;
    }

    public Integer getDderivedisclosed() {
        return dderivedisclosed;
    }

    public void setDderivedisclosed(Integer dderivedisclosed) {
        this.dderivedisclosed = dderivedisclosed;
    }

    public Integer getDderivedisfrozen() {
        return dderivedisfrozen;
    }

    public void setDderivedisfrozen(Integer dderivedisfrozen) {
        this.dderivedisfrozen = dderivedisfrozen;
    }

    public Integer getDderivedisvital() {
        return dderivedisvital;
    }

    public void setDderivedisvital(Integer dderivedisvital) {
        this.dderivedisvital = dderivedisvital;
    }

    public String getDderivednotifscripts() {
        return dderivednotifscripts;
    }

    public void setDderivednotifscripts(String dderivednotifscripts) {
        this.dderivednotifscripts = dderivednotifscripts;
    }

    public String getDderivedsecurityscripts() {
        return dderivedsecurityscripts;
    }

    public void setDderivedsecurityscripts(String dderivedsecurityscripts) {
        this.dderivedsecurityscripts = dderivedsecurityscripts;
    }

    public BigDecimal getDderivedvitalperiod() {
        return dderivedvitalperiod;
    }

    public void setDderivedvitalperiod(BigDecimal dderivedvitalperiod) {
        this.dderivedvitalperiod = dderivedvitalperiod;
    }

    public String getDderivedvitalperiodunits() {
        return dderivedvitalperiodunits;
    }

    public void setDderivedvitalperiodunits(String dderivedvitalperiodunits) {
        this.dderivedvitalperiodunits = dderivedvitalperiodunits;
    }

    public String getDderivedvitalreviewer() {
        return dderivedvitalreviewer;
    }

    public void setDderivedvitalreviewer(String dderivedvitalreviewer) {
        this.dderivedvitalreviewer = dderivedvitalreviewer;
    }

    public String getDdocaccount() {
        return ddocaccount;
    }

    public void setDdocaccount(String ddocaccount) {
        this.ddocaccount = ddocaccount;
    }

    public String getDdocauthor() {
        return ddocauthor;
    }

    public void setDdocauthor(String ddocauthor) {
        this.ddocauthor = ddocauthor;
    }

    public String getDexternallocation() {
        return dexternallocation;
    }

    public void setDexternallocation(String dexternallocation) {
        this.dexternallocation = dexternallocation;
    }

    public String getDexternallocation2() {
        return dexternallocation2;
    }

    public void setDexternallocation2(String dexternallocation2) {
        this.dexternallocation2 = dexternallocation2;
    }

    public String getDfileplantype() {
        return dfileplantype;
    }

    public void setDfileplantype(String dfileplantype) {
        this.dfileplantype = dfileplantype;
    }

    public String getDfolderdescription() {
        return dfolderdescription;
    }

    public void setDfolderdescription(String dfolderdescription) {
        this.dfolderdescription = dfolderdescription;
    }

    public String getDfolderid() {
        return dfolderid;
    }

    public void setDfolderid(String dfolderid) {
        this.dfolderid = dfolderid;
    }

    public String getDfoldername() {
        return dfoldername;
    }

    public void setDfoldername(String dfoldername) {
        this.dfoldername = dfoldername;
    }

    public String getDfreezeid() {
        return dfreezeid;
    }

    public void setDfreezeid(String dfreezeid) {
        this.dfreezeid = dfreezeid;
    }

    public String getDfreezereason() {
        return dfreezereason;
    }

    public void setDfreezereason(String dfreezereason) {
        this.dfreezereason = dfreezereason;
    }

    public Integer getDisclosed() {
        return disclosed;
    }

    public void setDisclosed(Integer disclosed) {
        this.disclosed = disclosed;
    }

    public Integer getDiscutoff() {
        return discutoff;
    }

    public void setDiscutoff(Integer discutoff) {
        this.discutoff = discutoff;
    }

    public Integer getDisexternal() {
        return disexternal;
    }

    public void setDisexternal(Integer disexternal) {
        this.disexternal = disexternal;
    }

    public String getDisfileplan() {
        return disfileplan;
    }

    public void setDisfileplan(String disfileplan) {
        this.disfileplan = disfileplan;
    }

    public Integer getDisfrozen() {
        return disfrozen;
    }

    public void setDisfrozen(Integer disfrozen) {
        this.disfrozen = disfrozen;
    }

    public Integer getDissubjecttoaudit() {
        return dissubjecttoaudit;
    }

    public void setDissubjecttoaudit(Integer dissubjecttoaudit) {
        this.dissubjecttoaudit = dissubjecttoaudit;
    }

    public Integer getDisvital() {
        return disvital;
    }

    public void setDisvital(Integer disvital) {
        this.disvital = disvital;
    }

    public Timestamp getDlastitemaddeddate() {
        return dlastitemaddeddate;
    }

    public void setDlastitemaddeddate(Timestamp dlastitemaddeddate) {
        this.dlastitemaddeddate = dlastitemaddeddate;
    }

    public Timestamp getDlastmodifieddate() {
        return dlastmodifieddate;
    }

    public void setDlastmodifieddate(Timestamp dlastmodifieddate) {
        this.dlastmodifieddate = dlastmodifieddate;
    }

    public String getDnotificationscripts() {
        return dnotificationscripts;
    }

    public void setDnotificationscripts(String dnotificationscripts) {
        this.dnotificationscripts = dnotificationscripts;
    }

    public String getDparentid() {
        return dparentid;
    }

    public void setDparentid(String dparentid) {
        this.dparentid = dparentid;
    }

    public Timestamp getDrecordactivationdate() {
        return drecordactivationdate;
    }

    public void setDrecordactivationdate(Timestamp drecordactivationdate) {
        this.drecordactivationdate = drecordactivationdate;
    }

    public Timestamp getDrecordcancelleddate() {
        return drecordcancelleddate;
    }

    public void setDrecordcancelleddate(Timestamp drecordcancelleddate) {
        this.drecordcancelleddate = drecordcancelleddate;
    }

    public Timestamp getDrecordcutoffdate() {
        return drecordcutoffdate;
    }

    public void setDrecordcutoffdate(Timestamp drecordcutoffdate) {
        this.drecordcutoffdate = drecordcutoffdate;
    }

    public Timestamp getDrecorddestroydate() {
        return drecorddestroydate;
    }

    public void setDrecorddestroydate(Timestamp drecorddestroydate) {
        this.drecorddestroydate = drecorddestroydate;
    }

    public Timestamp getDrecordexpirationdate() {
        return drecordexpirationdate;
    }

    public void setDrecordexpirationdate(Timestamp drecordexpirationdate) {
        this.drecordexpirationdate = drecordexpirationdate;
    }

    public Timestamp getDrecordfilingdate() {
        return drecordfilingdate;
    }

    public void setDrecordfilingdate(Timestamp drecordfilingdate) {
        this.drecordfilingdate = drecordfilingdate;
    }

    public Timestamp getDrecordobsoletedate() {
        return drecordobsoletedate;
    }

    public void setDrecordobsoletedate(Timestamp drecordobsoletedate) {
        this.drecordobsoletedate = drecordobsoletedate;
    }

    public Timestamp getDrecordrescindeddate() {
        return drecordrescindeddate;
    }

    public void setDrecordrescindeddate(Timestamp drecordrescindeddate) {
        this.drecordrescindeddate = drecordrescindeddate;
    }

    public Timestamp getDrecordreviewdate() {
        return drecordreviewdate;
    }

    public void setDrecordreviewdate(Timestamp drecordreviewdate) {
        this.drecordreviewdate = drecordreviewdate;
    }

    public Timestamp getDrecordsupersededdate() {
        return drecordsupersededdate;
    }

    public void setDrecordsupersededdate(Timestamp drecordsupersededdate) {
        this.drecordsupersededdate = drecordsupersededdate;
    }

    public String getDrmaprocessstate() {
        return drmaprocessstate;
    }

    public void setDrmaprocessstate(String drmaprocessstate) {
        this.drmaprocessstate = drmaprocessstate;
    }

    public BigDecimal getDrmasegmentid() {
        return drmasegmentid;
    }

    public void setDrmasegmentid(BigDecimal drmasegmentid) {
        this.drmasegmentid = drmasegmentid;
    }

    public String getDrmprofiletrigger() {
        return drmprofiletrigger;
    }

    public void setDrmprofiletrigger(String drmprofiletrigger) {
        this.drmprofiletrigger = drmprofiletrigger;
    }

    public String getDsecuritygroup() {
        return dsecuritygroup;
    }

    public void setDsecuritygroup(String dsecuritygroup) {
        this.dsecuritygroup = dsecuritygroup;
    }

    public String getDsecurityscripts() {
        return dsecurityscripts;
    }

    public void setDsecurityscripts(String dsecurityscripts) {
        this.dsecurityscripts = dsecurityscripts;
    }

    public String getDseriesid() {
        return dseriesid;
    }

    public void setDseriesid(String dseriesid) {
        this.dseriesid = dseriesid;
    }

    public String getDsupplementalmarkings() {
        return dsupplementalmarkings;
    }

    public void setDsupplementalmarkings(String dsupplementalmarkings) {
        this.dsupplementalmarkings = dsupplementalmarkings;
    }

    public BigDecimal getDvitalperiod() {
        return dvitalperiod;
    }

    public void setDvitalperiod(BigDecimal dvitalperiod) {
        this.dvitalperiod = dvitalperiod;
    }

    public String getDvitalperiodunits() {
        return dvitalperiodunits;
    }

    public void setDvitalperiodunits(String dvitalperiodunits) {
        this.dvitalperiodunits = dvitalperiodunits;
    }

    public String getDvitalreviewer() {
        return dvitalreviewer;
    }

    public void setDvitalreviewer(String dvitalreviewer) {
        this.dvitalreviewer = dvitalreviewer;
    }

    public String getDvitalstate() {
        return dvitalstate;
    }

    public void setDvitalstate(String dvitalstate) {
        this.dvitalstate = dvitalstate;
    }

    public String getXclbraaliaslist() {
        return xclbraaliaslist;
    }

    public void setXclbraaliaslist(String xclbraaliaslist) {
        this.xclbraaliaslist = xclbraaliaslist;
    }

    public String getXclbrauserlist() {
        return xclbrauserlist;
    }

    public void setXclbrauserlist(String xclbrauserlist) {
        this.xclbrauserlist = xclbrauserlist;
    }
}
