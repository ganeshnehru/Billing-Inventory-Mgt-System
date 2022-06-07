package Process.ChainOfCommands;

import Entity.Item;
import FileReader.InputReader;
import FileReader.InventoryReader;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Getter
@Setter
public class LuxuryCapLimit implements Chain {

    private Chain nextChain;
    InputReader inputReader = new InputReader();
    InventoryReader inventoryReader = new InventoryReader();

    HashMap<String, Item> inventoryItemsList = inventoryReader.getInventoryItemsList();
    HashMap<String, Item> inputItemsList = inputReader.getInputItemsList();
    HashSet<String> exceptionItems = new HashSet<>();

    public LuxuryCapLimit() throws IOException {
        inventoryReader.readFile();
        inputReader.readFile();
    }

    @Override
    public void setNextChain(Chain nextChain) throws IOException {
        this.nextChain = nextChain;
    }

    @Override
    public void checkCapLimit(Item item) throws IOException {
        if (item.getItemCategory().equalsIgnoreCase("Luxury") && getTotalQuantity() > 4) {
            for (String s : inputItemsList.keySet()) {
                if (inputItemsList.get(s).getItemCategory().equalsIgnoreCase("Luxury")) {
                    exceptionItems.add(inputItemsList.get(s).getItemName());
                }
            }
        } else {
            nextChain.checkCapLimit(item);
        }
    }

    //Returns total quantity in input list for LUXURY items.
    public int getTotalQuantity() {
        int totalQuantity = 0;

        for (String itemName : inputItemsList.keySet()) {
            if (inputItemsList.get(itemName).getItemCategory().equalsIgnoreCase("Luxury")) {
                totalQuantity = totalQuantity + inputItemsList.get(itemName).getItemQuantity();
            }
        }
        return totalQuantity;
    }
}
