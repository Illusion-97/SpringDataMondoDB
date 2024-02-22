package fr.dawan.springdatamondodb.typed;

import fr.dawan.springdatamondodb.typed.documents.Inventory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface InventoryRepository extends MongoRepository<Inventory, ObjectId> {
    List<Inventory> findByStatus(String status);
}
