package com.magnum.coffe.document.dao.impl;



import com.magnum.coffe.document.dao.DocDao;
import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.document.dao.repositories.DocRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;
@Slf4j
@Repository
public class DocDaoImpl implements DocDao {

    private final DocRepository docRepository;
    @Autowired
    public DocDaoImpl(DocRepository docRepository, MongoTemplate mongoTemplate) {
        this.docRepository = docRepository;

    }


    @Override
    public Doc findDocById(String id) {
        log.debug("[DocumentDaoImpl] find by id");
        Optional<Doc> doc = docRepository.findById(id);

        return docRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found"));
    }

    @Override
    public Doc saveDoc(Doc doc) {
        docRepository.save(doc);
        return doc;
    }
}
