package com.magnum.coffe.document.dao.repositories;

import com.magnum.coffe.document.dao.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<File,String> {
}
