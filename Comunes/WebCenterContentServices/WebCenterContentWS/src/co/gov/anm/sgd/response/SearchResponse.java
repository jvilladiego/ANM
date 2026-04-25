package co.gov.anm.sgd.response;

import com.oracle.ucm.Field;

import java.util.List;
import java.util.Map;

public class SearchResponse extends WCCResponse {
    
    private Map<String, String> metadataList;

    public void setResultSetList(List<Map<String, String>> resultSetList) {
        this.resultSetList = resultSetList;
    }

    public List<Map<String, String>> getResultSetList() {
        return resultSetList;
    }
    private List<Map<String, String>> resultSetList;
    
    public SearchResponse(List<Field> list) {
        super(list);
    }

    public SearchResponse() {
        super();
    }

    public void setMetadataList(Map<String, String> metadataList) {
        this.metadataList = metadataList;
    }

    public Map<String, String> getMetadataList() {
        return metadataList;
    }
}
