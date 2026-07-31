package model;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "SessionEJB", mappedName = "ComunicacionesSalFisica-ComunicacionesSalFisicaADF-SessionEJB")
public class SessionEJBBean4 implements SessionEJBRemote4, SessionEJBLocal4 {
    @Resource
    SessionContext sessionContext;
    
    @PersistenceContext(unitName = "ComunicacionesSalFisicaADF")
    private EntityManager em;
    @PersistenceContext(unitName = "ComunicacionesWCC")
    private EntityManager emWcc;

    public SessionEJBBean4() {
    }
    
    
    /********************************   AnmTramiteTb  ****************************************************/
    public AnmTramiteTb persistTramite(AnmTramiteTb tramite) {
         emWcc.persist(tramite);
         return tramite;
     }

     public AnmTramiteTb mergeTramite(AnmTramiteTb tramite) {
         return emWcc.merge(tramite);
     }

     public void removeTramite(AnmTramiteTb tramite) {
         tramite = emWcc.find(AnmTramiteTb.class, tramite.getIdtramite());
         emWcc.remove(tramite);
     }

     @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     public List<AnmTramiteTb> getTramiteFindAll() {
         return emWcc.createNamedQuery("AnmTramiteTb.findAll", AnmTramiteTb.class).getResultList();
     }
    /********************************   AnmTramiteTb  ****************************************************/
    
    /********************************   AnmTipodtalseguntramite  ****************************************************/
    public AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        emWcc.persist(tipoDocumentalTramite);
        return tipoDocumentalTramite;
    }

    public AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        return emWcc.merge(tipoDocumentalTramite);
    }

    public void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        tipoDocumentalTramite = emWcc.find(AnmTipodtalseguntramite.class, tipoDocumentalTramite.getIdTipodtalseguntramite());
        emWcc.remove(tipoDocumentalTramite);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll() {
        return emWcc.createNamedQuery("AnmTipodtalseguntramite.findAll", AnmTipodtalseguntramite.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite){
        return emWcc.createNamedQuery("AnmTipodtalseguntramite.tiposDocumentalesTramites", AnmTipodtalseguntramite.class).setParameter("p_id_tramite", idTramite).getResultList();
    }
    /********************************   AnmTipodtalseguntramite  ****************************************************/
    
    /*************************************SgdMunicipio**************************************************/
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdDepartamento> getSgdDepartamentoFindAll() {
        return em.createNamedQuery("SgdDepartamento.findAll", SgdDepartamento.class).getResultList();
    }    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdMunicipio> getSgdMunicipioFindByDepartamento(Long idDpto) {
        return em.createNamedQuery("SgdMunicipio.findByDepartamento", SgdMunicipio.class).setParameter("param", 
                                                                                                       idDpto).getResultList();
    }
    /*************************************SgdMunicipio**************************************************/
    
    /*************************************folderfolders**************************************************/
    
    /** <code>select o from SgdTipoSolicitud o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<folderfolders> getfolderfoldersFindAll() {
        return emWcc.createNamedQuery("folderfolders.findAll", folderfolders.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<folderfolders> getfolderfoldersFindParent() {
        return emWcc.createNamedQuery("folderfolders.findParent", folderfolders.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<folderfolders> getfolderfoldersFindByPArentGuid(String param) {
        return emWcc.createNamedQuery("folderfolders.findByParentGuid", folderfolders.class).setParameter("param", 
                                                                                                     param).getResultList();
    }
    /**************************************folderfolders****************************************************/

    public SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        em.persist(sgdAnexoComunciacion);
        return sgdAnexoComunciacion;
    }

    public SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        return em.merge(sgdAnexoComunciacion);
    }

    public void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        sgdAnexoComunciacion = em.find(SgdAnexoComunciacion.class, sgdAnexoComunciacion.getIdAnexoComunicacion());
        em.remove(sgdAnexoComunciacion);
    }

    /** <code>select o from SgdAnexoComunciacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll() {
        return em.createNamedQuery("SgdAnexoComunciacion.findAll", SgdAnexoComunciacion.class).getResultList();
    }

    public AnmUnidadAdministrativaTb persistAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb anmUnidadAdministrativaTb) {
        emWcc.persist(anmUnidadAdministrativaTb);
        return anmUnidadAdministrativaTb;
    }

    public AnmUnidadAdministrativaTb mergeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb anmUnidadAdministrativaTb) {
        return emWcc.merge(anmUnidadAdministrativaTb);
    }

    public void removeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb anmUnidadAdministrativaTb) {
        anmUnidadAdministrativaTb = em.find(AnmUnidadAdministrativaTb.class, anmUnidadAdministrativaTb.getIdUnidadadministrativa());
        emWcc.remove(anmUnidadAdministrativaTb);
    }

    /** <code>select o from AnmUnidadAdministrativaTb o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindAll() {
        return emWcc.createNamedQuery("AnmUnidadAdministrativaTb.findAll", AnmUnidadAdministrativaTb.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindByCodigo(Integer param) {
        return emWcc.createNamedQuery("AnmUnidadAdministrativaTb.findByCodigo", AnmUnidadAdministrativaTb.class).setParameter("param", 
                                                                                                                              param).getResultList();
    }
    /***************************************************************************************************/
    


    /***************************************************************************************************/
    public SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario) {
        em.persist(sgdUsuario);
        return sgdUsuario;
    }

    public SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario) {
        return em.merge(sgdUsuario);
    }

    public void removeSgdUsuario(SgdUsuario sgdUsuario) {
        sgdUsuario = em.find(SgdUsuario.class, sgdUsuario.getIdUsuario());
        em.remove(sgdUsuario);
    }

    /** <code>select o from SgdUsuario o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioFindAll() {
        return em.createNamedQuery("SgdUsuario.findAll", SgdUsuario.class).getResultList();
    }
    
    public List<SgdUsuario> getSgdUsuarioFindById(String param) {
        return em.createNamedQuery("SgdUsuario.findById", SgdUsuario.class).setParameter("param", 
                                                                                         param).getResultList();
    }
    
    public List<SgdUsuario> getSgdUsuarioFindByCodDependencia(Long param){
        return em.createNamedQuery("SgdUsuario.findByCodDependencia", SgdUsuario.class).setParameter("param", 
                                                                                         param).getResultList();
    }
    /***************************************************************************************************/
    
    

    public SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        em.persist(sgdInteresadoInt);
        return sgdInteresadoInt;
    }

    public SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        return em.merge(sgdInteresadoInt);
    }

    public void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        sgdInteresadoInt = em.find(SgdInteresadoInt.class, sgdInteresadoInt.getIdInteresadoInt());
        em.remove(sgdInteresadoInt);
    }

    /** <code>select o from SgdInteresadoInt o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdInteresadoInt> getSgdInteresadoIntFindAll() {
        return em.createNamedQuery("SgdInteresadoInt.findAll", SgdInteresadoInt.class).getResultList();
    }

    public SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        em.persist(sgdTipoIdentificacion);
        return sgdTipoIdentificacion;
    }

    public SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        return em.merge(sgdTipoIdentificacion);
    }

    public void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        sgdTipoIdentificacion = em.find(SgdTipoIdentificacion.class, sgdTipoIdentificacion.getCodigo());
        em.remove(sgdTipoIdentificacion);
    }

    /** <code>select o from SgdTipoIdentificacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll() {
        return em.createNamedQuery("SgdTipoIdentificacion.findAll", SgdTipoIdentificacion.class).getResultList();
    }

    public SgdRol persistSgdRol(SgdRol sgdRol) {
        em.persist(sgdRol);
        return sgdRol;
    }

    public SgdRol mergeSgdRol(SgdRol sgdRol) {
        return em.merge(sgdRol);
    }

    public void removeSgdRol(SgdRol sgdRol) {
        sgdRol = em.find(SgdRol.class, sgdRol.getIdRol());
        em.remove(sgdRol);
    }

    /** <code>select o from SgdRol o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdRol> getSgdRolFindAll() {
        return em.createNamedQuery("SgdRol.findAll", SgdRol.class).getResultList();
    }

    public AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla) {
        em.persist(anmPlantilla);
        return anmPlantilla;
    }

    public AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla) {
        return em.merge(anmPlantilla);
    }

    public void removeAnmPlantilla(AnmPlantilla anmPlantilla) {
        anmPlantilla = em.find(AnmPlantilla.class, anmPlantilla.getIdAnmPlantilla());
        em.remove(anmPlantilla);
    }

    /** <code>select o from AnmPlantilla o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmPlantilla> getAnmPlantillaFindAll() {
        return em.createNamedQuery("AnmPlantilla.findAll", AnmPlantilla.class).getResultList();
    }

    public Dept persistDept(Dept dept) {
        em.persist(dept);
        return dept;
    }

    public Dept mergeDept(Dept dept) {
        return em.merge(dept);
    }

    public void removeDept(Dept dept) {
        dept = em.find(Dept.class, dept.getId());
        em.remove(dept);
    }

    /** <code>select o from Dept o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Dept> getDeptFindAll() {
        return em.createNamedQuery("Dept.findAll", Dept.class).getResultList();
    }

    public SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion) {
        em.persist(sgdComunicacion);
        return sgdComunicacion;
    }

    public SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion) {
        return em.merge(sgdComunicacion);
    }

    public void removeSgdComunicacion(SgdComunicacion sgdComunicacion) {
        sgdComunicacion = em.find(SgdComunicacion.class, sgdComunicacion.getIdComunicacion());
        em.remove(sgdComunicacion);
    }

    /** <code>select o from SgdComunicacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdComunicacion> getSgdComunicacionFindAll() {
        return em.createNamedQuery("SgdComunicacion.findAll", SgdComunicacion.class).getResultList();
    }

    public PsTxn persistPsTxn(PsTxn psTxn) {
        em.persist(psTxn);
        return psTxn;
    }

    public PsTxn mergePsTxn(PsTxn psTxn) {
        return em.merge(psTxn);
    }

    public void removePsTxn(PsTxn psTxn) {
        psTxn = em.find(PsTxn.class, new PsTxnPK(psTxn.getCollid(), psTxn.getId()));
        em.remove(psTxn);
    }

    /** <code>select o from PsTxn o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<PsTxn> getPsTxnFindAll() {
        return em.createNamedQuery("PsTxn.findAll", PsTxn.class).getResultList();
    }

    public SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        em.persist(sgdUsuarioRol);
        return sgdUsuarioRol;
    }

    public SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        return em.merge(sgdUsuarioRol);
    }

    public void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        sgdUsuarioRol = em.find(SgdUsuarioRol.class, sgdUsuarioRol.getIdUsuarioRol());
        em.remove(sgdUsuarioRol);
    }

    /** <code>select o from SgdUsuarioRol o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuarioRol> getSgdUsuarioRolFindAll() {
        return em.createNamedQuery("SgdUsuarioRol.findAll", SgdUsuarioRol.class).getResultList();
    }

    public Emp persistEmp(Emp emp) {
        em.persist(emp);
        return emp;
    }

    public Emp mergeEmp(Emp emp) {
        return em.merge(emp);
    }

    public void removeEmp(Emp emp) {
        emp = em.find(Emp.class, emp.getId());
        em.remove(emp);
    }

    /** <code>select o from Emp o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Emp> getEmpFindAll() {
        return em.createNamedQuery("Emp.findAll", Emp.class).getResultList();
    }
}
