package fr.dawan.springdatamondodb.typed.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private String warehouse;
    private int qty;
}
