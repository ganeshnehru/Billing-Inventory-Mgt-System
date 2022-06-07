package FileReader;

import Entity.Item;
import FileReader.Strategy.FileReaderStrategy;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class InputReader implements FileReaderStrategy {

    InventoryReader inventoryReader = new InventoryReader();
    HashMap<String, Item> inventoryItemsList = inventoryReader.getInventoryItemsList();
    HashMap<String, Item> inputItemsList = new HashMap<>();
    Item item;
    Item unknownItem;
    String cardNumber;
    ArrayList<String> invalidQuantityItems = new ArrayList<>();

    public InputReader() throws IOException {
        inventoryReader.readFile();
    }

    @Override
    public Object readFile() throws IOException {
        //************************//
        //Place INPUT FILE here...//
        //************************//
        Path path = Paths.get("input_file.csv");
        String filePath = path.toAbsolutePath().toString();
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = "";
        String[] lineElements;
        int counter = 0;

        while ((line = br.readLine()) != null) {
            lineElements = line.split(",");

            if (counter < 1) {
                counter++;
                continue;
            }
            if (lineElements.length == 3) {
                cardNumber = lineElements[2];
            }

            String itemName = lineElements[0];
            String category = "";
            int quantity = 0;
            double price = 0;

            item = new Item(itemName, category, quantity, price);

            if (inventoryItemsList.containsKey(itemName.toUpperCase()) && lineElements.length > 1) {
                category = inventoryItemsList.get(item.getItemName().toUpperCase()).getItemCategory();
                price = inventoryItemsList.get(item.getItemName().toUpperCase()).getItemPrice();
                quantity = Integer.parseInt(lineElements[1]);

                item = new Item(itemName, category, quantity, price);

                inputItemsList.put(itemName.toUpperCase(), item);
            } else {
                inputItemsList.put(itemName.toUpperCase(), new Item(lineElements[0], category, quantity, price));
            }
        }
        return inputItemsList;
    }
}
