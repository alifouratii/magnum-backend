package com.magnum.coffe.waiterCall.repositories;


import com.magnum.coffe.waiterCall.model.WaiterCall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaiterCallRepository extends MongoRepository<WaiterCall, String> {
}