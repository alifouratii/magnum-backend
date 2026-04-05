package com.magnum.coffe.document.dao.repositories;

import com.magnum.coffe.document.dao.model.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepository extends MongoRepository<Doc,String> {

}
