package co.gov.anm.sgd.mngr;

import co.gov.anm.sgd.response.BrowseResponse;
import co.gov.anm.sgd.response.FileResponse;
import co.gov.anm.sgd.response.FolderResponse;
import co.gov.anm.sgd.response.SearchResponse;
import co.gov.anm.sgd.response.WCCResponse;
import co.gov.anm.sgd.util.Constants;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_BROWSE;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_CREATE_FILE;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_CREATE_FOLDER;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_DELETE;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_EDIT_FOLDER;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_FOLDER_SEARCH;
import static co.gov.anm.sgd.util.Constants.SVR_FLD_MOVE;
import static co.gov.anm.sgd.util.Constants.dID;
import static co.gov.anm.sgd.util.Constants.docName;
import static co.gov.anm.sgd.util.Constants.fileGUID;
import static co.gov.anm.sgd.util.Constants.fileName;
import static co.gov.anm.sgd.util.Constants.folderGUID;
import static co.gov.anm.sgd.util.Constants.folderName;
import static co.gov.anm.sgd.util.Constants.parentGUID;
import static co.gov.anm.sgd.util.Constants.queryText;

import com.oracle.ucm.Field;
import com.oracle.ucm.Generic;
import com.oracle.ucm.ResultSet;
import com.oracle.ucm.Row;
import com.oracle.ucm.Service;

import genericsoap.GenericSoapPortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class FolderMngr {

    public static final Logger logger = Logger.getLogger(FolderMngr.class);
    
    private GenericSoapPortType port;

    public FolderMngr(GenericSoapPortType port) {
        this.port = port;
    }

    public WCCResponse create(String fFolderGUID, String fFolderName) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_CREATE_FOLDER);

        Generic wsResponse = null;
        Map<String, String> metadataMap = new HashMap<String, String>();        
        
        metadataMap.put(parentGUID, fFolderGUID);
        metadataMap.put(folderName, fFolderName);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_FLD_CREATE_FOLDER, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }
    
    public WCCResponse edit (Map<String, String> metadataMap) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_EDIT_FOLDER);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        Generic wsResponse = delegate.executeWS(SVR_FLD_EDIT_FOLDER, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }

    public WCCResponse delete(String fFolderGUID) throws Exception {
        logger.debug("inicia servicio " + Constants.SVR_FLD_DELETE);

        Generic wsResponse = null;
        Map<String, String> metadataMap = new HashMap<String, String>();
        
        metadataMap.put("item1", folderGUID + ":" + fFolderGUID);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_FLD_DELETE, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }

    public WCCResponse createFileLinkInFolder(HashMap<String, String> metadataMap) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_CREATE_FILE);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        Generic wsResponse = delegate.executeWS(SVR_FLD_CREATE_FILE, metadataMap, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }

    public SearchResponse search(String criteria) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_FOLDER_SEARCH);
        
        Generic wsResponse = null;
        SearchResponse searchResponse = null;
        Map<String, String> metadataRequest = new HashMap<String, String>();
        Map<String, String> metadataList = null;    
        
        metadataRequest.put(queryText, criteria);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_FLD_FOLDER_SEARCH, metadataRequest, null);

        Service.Document rspdoc = wsResponse.getService().getDocument();

        searchResponse = new SearchResponse(rspdoc.getField());

        //Get Results Sets from document
        List<ResultSet> resultSetList = rspdoc.getResultSet();

        for (ResultSet rset : resultSetList) {
            //solo procesa el tag search_results
            if ("search_results".equals(rset.getName().toLowerCase())) {
                List<Map<String, String>> resultSetRspList = new ArrayList<Map<String, String>>();

                for (Row row : rset.getRow()) {
                    List<Field> rscols = row.getField();

                    metadataList = new HashMap<String, String>();

                    for (Field field : rscols) {
                        metadataList.put(field.getName(), field.getValue());
                    }
                    searchResponse.setMetadataList(metadataList);

                    resultSetRspList.add(metadataList);
                }
                searchResponse.setResultSetList(resultSetRspList);
            } 
        } 
        
        return searchResponse;
    }
    
    public WCCResponse move (String destination, String item) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_MOVE);

        Generic wsResponse = null;
        Map<String, String> metadataList = null; 
        
        metadataList = new HashMap<String, String>();
        metadataList.put("destination", destination);
        metadataList.put("item", item);
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_FLD_MOVE, metadataList, null);

        return new WCCResponse(wsResponse.getService().getDocument().getField());
    }
    
    public BrowseResponse browse (Map<String, String> metadataMap) throws Exception {
        logger.debug("inicia servicio " + SVR_FLD_BROWSE);
        
        Generic wsResponse = null;
        BrowseResponse browseResponse = new BrowseResponse();
        List<FileResponse> fileList = null;
        List<FolderResponse> folderList = null;
        Map<String, String> metadataList = null;
        
        WCCAPIDelegate delegate = new WCCAPIDelegate(port);
        
        wsResponse = delegate.executeWS(SVR_FLD_BROWSE, metadataMap, null);
        
        browseResponse = new BrowseResponse(wsResponse.getService().getDocument().getField());          
        
        //Get Results Sets from document
        List<ResultSet> resultSetList = wsResponse.getService().getDocument().getResultSet();

        for (ResultSet resultSet : resultSetList) {                
            //trae carpetas y documentos en el primer nivel de la jerarquia consultada
            if ("childfolders".equals(resultSet.getName().toLowerCase())) {                         
                folderList = new ArrayList<FolderResponse>();
                    
                for (Row row : resultSet.getRow()) {    
                    FolderResponse folder = new FolderResponse();                                                    
                    for (Field field : row.getField()) {                                                       
                        if (folderName.equals(field.getName())) {
                            folder.setName(field.getValue());  
                        } else if (folderGUID.equals(field.getName())) {
                            folder.setId(field.getValue());    
                        } else if (parentGUID.equals(field.getName())) {
                            folder.setParentID(field.getValue());                                
                        }
                    }     
                    folderList.add(folder);
                }                    
                browseResponse.setFolderList(folderList);
            } else if ("childfiles".equals(resultSet.getName().toLowerCase())) {
                fileList = new ArrayList<FileResponse>();         
                    
                for (Row row : resultSet.getRow()) {
                    FileResponse file = new FileResponse();
                    for (Field field : row.getField()) {  
                        if (dID.equals(field.getName())) {
                            file.setDID(field.getValue());
                        } else if (fileName.equals(field.getName())) {
                            file.setName(field.getValue());
                        } else if (docName.equals(field.getName())) {
                            file.setDocName(field.getValue());    
                        } else if (parentGUID.equals(field.getName())) {
                            file.setParentID(field.getValue());                                
                        } else if (fileGUID.equals(field.getName())) {
                            file.setFileGUID(field.getValue());   
                        }
                    }  
                    fileList.add(file);
                } 
                browseResponse.setFileList(fileList); 
            } else if ("folderinfo".equalsIgnoreCase(resultSet.getName().toLowerCase())) {
                metadataList = new HashMap<String, String>();
                
                for (Field field : resultSet.getRow().get(0).getField()) {
                    metadataList.put(field.getName(), field.getValue());
                }
                browseResponse.setMetaDataFields(metadataList);
            }
        }
        
        return browseResponse;
    }
}
