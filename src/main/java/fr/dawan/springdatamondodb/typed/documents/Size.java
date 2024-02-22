package fr.dawan.springdatamondodb.typed.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Getter
@Setter
@ToString
@EqualsAndHashCode*/
@Data // reprends les 4 annotations précédentes
@NoArgsConstructor
@AllArgsConstructor
// @RequiredArgsConstructor constructeur avec uniquement les champs 'requis' (final)
public class Size {
    private double h;
    private double w;
    private String uom;
}
