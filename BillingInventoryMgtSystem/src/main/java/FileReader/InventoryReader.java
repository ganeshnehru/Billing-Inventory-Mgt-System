package FileReader;

import Entity.Item;
import FileReader.Strategy.FileReaderStrategy;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

@Getter
@Setter
public class InventoryReader implements FileReaderStrategy {

    //In-memory db tables to hold items and cards.
    HashMap<String, Item> inventoryItemsList = new HashMap<>();
    HashSet<String> inventoryCardsList = new HashSet<>();


    @Override
    public Object readFile() throws IOException {
        //****************************//
        //Place INVENTORY FILE here...//
        //****************************//
        Path path = Paths.get("inventory_file.csv");
        String filePath = path.toAbsolutePath().toString();
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = "";
        String[] lineElements;
        int counter = 0;

        while ((line = br.readLine()) != null) {
            lineElements = line.split(",");

            //Conditions to skip line in CSV file.
            if (lineElements.length == 0 || lineElements[0].contains("Item") || lineElements[0].contains("Cards") || lineElements[0].contains("CardNumber")) {
                counter++;
                continue;
            }

            //Stores existing card numbers in the card hash set.
            if (lineElements.length == 1) {
                inventoryCardsList.add(lineElements[0]);
            } else {
                Item item = new Item(lineElements[0], lineElements[1], Integer.parseInt(lineElements[2]), Double.parseDouble(lineElements[3]));
                inventoryItemsList.put(lineElements[0].toUpperCase(), item);
            }
        }
        return inventoryItemsList;
    }
}

