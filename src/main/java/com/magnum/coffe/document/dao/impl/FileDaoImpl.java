package com.magnum.coffe.document.dao.impl;



import com.magnum.coffe.document.dao.FileDao;
import com.magnum.coffe.document.dao.model.File;
import com.magnum.coffe.document.dao.repositories.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Slf4j
@Repository
public class FileDaoImpl implements FileDao {

    private final FileRepository fileRepository;

    public FileDaoImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File findFileById(String id) {
        log.debug("[PhotoDaoImpl] find by id");
        Optional<File> file = fileRepository.findById(id);
        if(file.isPresent()){
            return  file.get();
        }else{
            return  null;
        }
    }

    @Override
    public String saveFile(File file) {


        fileRepository.save(file);
        return file.getId();
    }

}
