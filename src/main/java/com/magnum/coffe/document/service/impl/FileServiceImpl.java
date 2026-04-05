package com.magnum.coffe.document.service.impl;



import com.magnum.coffe.document.dao.FileDao;
import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.document.dao.model.DocumentType;
import com.magnum.coffe.document.dao.model.File;
import com.magnum.coffe.document.service.DocService;
import com.magnum.coffe.document.service.FileService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final FileDao fileDao;
    private final DocService docService;
    private final GridFsOperations gridFsOperations;


    @Override
    public File getFileById(String id) {
        return fileDao.findFileById(id);
    }

    public String createFile(MultipartFile file) throws IOException {
        File fileDto=new File();
        fileDto.setFileBinary(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

        return fileDao.saveFile(fileDto);
    }

    @Override
    public Doc createFileFromUpload(MultipartFile file, String fileName, String docType) throws IOException {

        String idFile = createFile(file);
        String fName = file.getOriginalFilename();
        Doc doc = new Doc();
        doc.setBigData(false);
        if (fileName != null)
            fName = fileName;

        doc.setFileName(fName);
        doc.setContentType(file.getContentType());
        doc.setSize(String.valueOf(file.getSize()));
        doc.setIdFile(idFile);
        DocumentType documentType = DocumentType.fromString(docType);

        if(documentType != null){
            doc.setDocType(documentType);

        }
        doc.setCreationDate(LocalDateTime.now());

        return docService.createDoc(doc);
    }

    @Override
    public Doc downloadFile(String id) throws IOException {


        Doc doc = docService.getDocById(id);

        if (!doc.getBigData()) {

            doc.setFile(fileDao.findFileById(doc.getIdFile()).getFileBinary().getData());
        }
        if (doc.getBigData()) {
            GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(doc.getIdFile())));


            doc.setFile(IOUtils.toByteArray(gridFsOperations.getResource(dbFile).getInputStream()));

        }



        return doc;



    }


}
