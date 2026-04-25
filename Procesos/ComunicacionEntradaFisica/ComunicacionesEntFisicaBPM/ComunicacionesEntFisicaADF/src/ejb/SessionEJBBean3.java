package ejb;

import entities.AnmPlantilla;

import entities.AnmTipodtalseguntramite;
import entities.AnmTramiteTb;
import entities.AnmUnidadAdministrativaTb;
import entities.SgdAnexoComunciacion;
import entities.SgdComunicacion;

import entities.SgdDependencia;
import entities.SgdInteresadoInt;
import entities.SgdRol;
import entities.SgdTipoIdentificacion;
import entities.SgdUsuario;
import entities.SgdUsuarioRol;

import entities.SgdPais;
import entities.SgdDepartamento;
import entities.SgdMunicipio;

import entities.SgdTipoSolicitud;

import entities.TipoSolicitante;
import entities.folderfolders;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;


@Stateless(name = "SessionEJB", mappedName = "ComunicacionesEntFisicaBPM-ComunicacionesEntFisicaADF-SessionEJB")
public class SessionEJBBean3 implements SessionEJBRemote3, SessionEJBLocal3 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "ComunicacionesEntFisicaADF")
    private EntityManager em;
    @PersistenceContext(unitName = "ComunicacionesWCC")
    private EntityManager emWcc;
    

    public SessionEJBBean3() {
    }
    
    
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
    
    
    
    /*************************************SgdTipoSolicitud**************************************************/
    
    /** <code>select o from SgdTipoSolicitud o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdTipoSolicitud> getSgdTipoSolicitudFindAll() {
        return em.createNamedQuery("SgdTipoSolicitud.findAll", SgdTipoSolicitud.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdTipoSolicitud getSgdTipoSolicitudFindById(Integer param) {
        return em.createNamedQuery("SgdTipoSolicitud.findById", SgdTipoSolicitud.class).setParameter("param", 
                                                                                                     param).getSingleResult();
    }
    /**************************************SgdTipoSolicitud****************************************************/
    
    
    /***********************************************************************************************************/
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdPais> getSgdPaisFindAll() {
        return em.createNamedQuery("SgdPais.findAll", SgdPais.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdDepartamento> getSgdDepartamentoFindAll() {
        return em.createNamedQuery("SgdDepartamento.findAll", SgdDepartamento.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdMunicipio> getSgdMunicipioFindByDepartamento(Long idDpto) {
        return em.createNamedQuery("SgdMunicipio.findByDepartamento", SgdMunicipio.class).setParameter("param", 
                                                                                                       idDpto).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdMunicipio> getSgdMunicipioFindById(Long idMunicipio){
        return em.createNamedQuery("SgdMunicipio.findById", SgdMunicipio.class).setParameter("param", 
                                                                                            idMunicipio).getResultList();
    }
    /**************************************************************************************************************/
    
    
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
    
    
    /********************************   AnmUnidadAdministrativaTb  ****************************************************/
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
    /******************************************************************************************************************/
    
    
    

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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioByCodDependencia(Long codDependencia) {
        return em.createNamedQuery("SgdUsuario.findByCodDependencia", SgdUsuario.class)
                 .setParameter("param", codDependencia)
                 .getResultList();
    }
    
    
    
    

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



    /**************************************************************************************************/
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindByComunicacion(Long idComunicacion) {
        return em.createNamedQuery("SgdAnexoComunciacion.findByIdCom", SgdAnexoComunciacion.class).setParameter("param", 
                                                                                                    idComunicacion).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindByNumeroRadicado(String numeroRadicado) {
        return em.createNamedQuery("SgdAnexoComunciacion.findByNumRadicado", SgdAnexoComunciacion.class).setParameter("param", 
                                                                                                    numeroRadicado).getResultList();
    }
    /**************************************************************************************************/



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
    /*************************************************************************************************/
    
    
    

    public SgdDependencia persistSgdDependencia(SgdDependencia sgdDependencia) {
        em.persist(sgdDependencia);
        return sgdDependencia;
    }

    public SgdDependencia mergeSgdDependencia(SgdDependencia sgdDependencia) {
        return em.merge(sgdDependencia);
    }

    public void removeSgdDependencia(SgdDependencia sgdDependencia) {
        sgdDependencia = em.find(SgdDependencia.class, sgdDependencia.getIdSgdDependencia());
        em.remove(sgdDependencia);
    }

    /** <code>select o from SgdDependencia o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdDependencia> getSgdDependenciaFindAll() {
        return em.createNamedQuery("SgdDependencia.findAll", SgdDependencia.class).getResultList();
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoSolicitante> getTipoSolicitanteFindAll() {
        return em.createNamedQuery("TipoSolicitante.findAll", TipoSolicitante.class).getResultList();
    }
}
