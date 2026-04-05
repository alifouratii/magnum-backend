package com.magnum.coffe.document.dao;


import com.magnum.coffe.document.dao.model.Doc;

public interface DocDao {
    Doc findDocById(String id);

    Doc saveDoc(Doc doc);
}
