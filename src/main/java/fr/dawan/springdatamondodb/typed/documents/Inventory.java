package fr.dawan.springdatamondodb.typed.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Inventory {

    @Id
    private ObjectId id;

    private String item;
    private String status;
    private Size size;
    private List<Stock> instock;
}
