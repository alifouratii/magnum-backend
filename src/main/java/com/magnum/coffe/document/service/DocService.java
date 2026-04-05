package com.magnum.coffe.document.service;


import com.magnum.coffe.document.dao.model.Doc;

public interface DocService {
    Doc getDocById(String id);
    Doc createDoc(Doc doc) ;


}
