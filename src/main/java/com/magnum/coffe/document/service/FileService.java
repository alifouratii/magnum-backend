package com.magnum.coffe.document.service;


import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.document.dao.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    File getFileById(String id);
    Doc createFileFromUpload(MultipartFile file, String fileName, String docType) throws IOException;
    Doc downloadFile(String id) throws IOException;


}
