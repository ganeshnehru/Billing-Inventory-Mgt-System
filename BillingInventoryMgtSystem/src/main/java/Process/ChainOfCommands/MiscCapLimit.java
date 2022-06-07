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
public class MiscCapLimit implements Chain {

    private Chain nextChain;
    InputReader inputReader = new InputReader();
    InventoryReader inventoryReader = new InventoryReader();

    HashMap<String, Item> inventoryItemsList = inventoryReader.getInventoryItemsList();
    HashMap<String, Item> inputItemsList = inputReader.getInputItemsList();
    HashSet<String> exceptionItems = new HashSet<>();

    public MiscCapLimit() throws IOException {
        inputReader.readFile();
        inventoryReader.readFile();
    }

    @Override
    public void setNextChain(Chain nextChain) throws IOException {
        this.nextChain = nextChain;
    }

    @Override
    public void checkCapLimit(Item item) throws IOException {
        if (item.getItemCategory().equalsIgnoreCase("Misc") && getTotalQuantity() > 6) {
            for (String s : inputItemsList.keySet()) {
                if (inputItemsList.get(s).getItemCategory().equalsIgnoreCase("Misc")) {
                    exceptionItems.add(inputItemsList.get(s).getItemName());
                }
            }
        }
    }

    //Returns total quantity in input list for MISC items.
    public int getTotalQuantity() {
        int totalQuantity = 0;

        for (String itemName : inputItemsList.keySet()) {
            if (inputItemsList.get(itemName).getItemCategory().equalsIgnoreCase("Misc")) {
                totalQuantity = totalQuantity + inputItemsList.get(itemName).getItemQuantity();
            }
        }
        return totalQuantity;
    }
}
