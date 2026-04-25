package co.gov.anm.sgd.bean;

import co.gov.anm.sgd.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface WCContent3 {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    void removeUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    List<UnidadAdministrativa> getUnidadAdministrativaFindAll();

    List<UnidadAdministrativa> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa);
}
