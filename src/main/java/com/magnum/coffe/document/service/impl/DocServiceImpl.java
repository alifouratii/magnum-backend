package com.magnum.coffe.document.service.impl;



import com.magnum.coffe.document.dao.DocDao;
import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.document.service.DocService;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl implements DocService {

    private final DocDao docDao;

    public DocServiceImpl(DocDao docDao) {
        this.docDao = docDao;

    }


    @Override
    public Doc getDocById(String id) {
        return docDao.findDocById(id);
    }

    @Override
    public Doc createDoc(Doc doc) {

        return docDao.saveDoc(doc);

    }


}
