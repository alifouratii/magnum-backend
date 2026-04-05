package com.magnum.coffe.document.dao;


import com.magnum.coffe.document.dao.model.File;

public interface FileDao {
    File findFileById(String id);

    String saveFile(File fileDto);
}
