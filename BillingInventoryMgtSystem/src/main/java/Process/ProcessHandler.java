package Process;

import Entity.Item;
import FileReader.InputReader;
import FileReader.InventoryReader;
import Process.ChainOfCommands.EssentialsCapLimit;
import Process.ChainOfCommands.LuxuryCapLimit;
import Process.ChainOfCommands.MiscCapLimit;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

@Getter
@Setter
public class ProcessHandler {

    InventoryReader inventoryReader = new InventoryReader();
    InputReader inputReader = new InputReader();

    HashMap<String, Item> inventoryItemsList = inventoryReader.getInventoryItemsList();
    HashSet<String> inventoryCardsList = inventoryReader.getInventoryCardsList();
    ArrayList<Item> inventoryItemsArrList = new ArrayList<>();

    HashMap<String, Item> inputItemsList = inputReader.getInputItemsList();
    String inputCardNumber;

    //Exceptions lists.
    ArrayList<String> allExceptions = new ArrayList<>();
    ArrayList<String> capLimitException = new ArrayList<>();
    ArrayList<String> availabilityException = new ArrayList<>();
    String noCardException = "";

    public ProcessHandler() throws IOException {
        inventoryReader.readFile();
        inputReader.readFile();
        inputCardNumber = inputReader.getCardNumber();
    }

    public void process() throws IOException {

        File output = new File("");
        double totalInvoiceCost = 0;

        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        StringBuilder s3 = new StringBuilder();

        if (isValidCart() == true) {
            hasCard(inputCardNumber);

            s1.append("Item").append(",").append("Quantity").append(",").append("Price").append(",").append("TotalPrice").append("\n");
            int index = 0;

            for (String inputItem : inputItemsList.keySet()) {
                String itemName = inputItemsList.get(inputItem).getItemName();
                int requestedQty = inputItemsList.get(inputItem).getItemQuantity();
                int availableInvQuantity = inventoryItemsList.get(inputItem).getItemQuantity();
                double itemPriceByQuantity = inputItemsList.get(inputItem).getItemPrice() * inputItemsList.get(inputItem).getItemQuantity();

                if (index == 0) {
                    s2.append(itemName).append(",").append(requestedQty).append(",").append(itemPriceByQuantity).append(",");
                } else {
                    s3.append(itemName).append(",").append(requestedQty).append(",").append(itemPriceByQuantity).append("\n");
                }
                index++;

                int qtyChangeInInventory = availableInvQuantity - requestedQty;

                inventoryItemsList.get(inputItem).setItemQuantity(qtyChangeInInventory);

                totalInvoiceCost = totalInvoiceCost + itemPriceByQuantity;
            }

            StringBuilder s4 = s2.append(totalInvoiceCost).append("\n");
            String outputFile = s1.toString() + s4.toString() + s3.toString();

            output = new File("invoice.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(output, true));
            StringBuilder sb = new StringBuilder();

            bw.write(outputFile);
            bw.close();

            System.out.println("Input successfully processed! Check invoice.csv for more details.");

        } else {
            if (capLimitException.size() > 0) {
                allExceptions.add(capLimitException.toString());
            }
            if (availabilityException.size() > 0) {
                allExceptions.add(availabilityException.toString());
            }
            if (!noCardException.isEmpty()) {
                allExceptions.add(noCardException);
            }

            output = new File("output.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(output, true));

            for (Iterator exceptionIterator = getIterator(); exceptionIterator.hasNext(); ) {
                String exception = (String) exceptionIterator.next();
                bw.write(exception + "\n");
            }
            bw.close();

            System.out.println("Error in input file. Check output.txt for more details.");
        }
    }

    //Validates cart to see if each item in input file does not violate cap-limit, as well as checking its inventory stock.
    public ArrayList<String> checkItemCapLimit() throws IOException {

        ArrayList<String> capLimitExceededItems = new ArrayList<>();

        //Check cap limit for each input item.
        EssentialsCapLimit essentialsCapLimit = new EssentialsCapLimit();
        LuxuryCapLimit luxuryCapLimit = new LuxuryCapLimit();
        MiscCapLimit miscCapLimit = new MiscCapLimit();

        //Linking the categorical chains.
        essentialsCapLimit.setNextChain(luxuryCapLimit);
        luxuryCapLimit.setNextChain(miscCapLimit);

        for (String s : inputItemsList.keySet()) {
            essentialsCapLimit.checkCapLimit(inputItemsList.get(s));
        }

        if (!essentialsCapLimit.getExceptionItems().isEmpty()) {
            capLimitExceededItems.add(String.join(", ", "Essentials: " + essentialsCapLimit.getExceptionItems()));
        }
        if (!luxuryCapLimit.getExceptionItems().isEmpty()) {
            capLimitExceededItems.add(String.join(", ", "Luxury: " + luxuryCapLimit.getExceptionItems()));
        }
        if (!miscCapLimit.getExceptionItems().isEmpty()) {
            capLimitExceededItems.add(String.join(", ", "Misc: " + miscCapLimit.getExceptionItems()));
        }

        if (!capLimitExceededItems.isEmpty()) {
            capLimitException.add("Following items have exceeded their categorical cap limit: " + String.join(", ", capLimitExceededItems));
        }
        return capLimitException;
    }

    //Checks to see if requested item quantity is within range of inventory quantity.
    public ArrayList<String> checkItemAvailability() {
        ArrayList<String> lowStockItems = new ArrayList<>();
        ArrayList<String> invalidItems = new ArrayList<>();

        for (String s : inputItemsList.keySet()) {

            if (inventoryItemsList.containsKey(s) && inputItemsList.get(s).getItemQuantity() > inventoryItemsList.get(s).getItemQuantity()) {
                lowStockItems.add(inputItemsList.get(s).getItemName());
            } else if (!inventoryItemsList.containsKey(s) || inputItemsList.get(s).getItemQuantity() == 0) {
                invalidItems.add(inputItemsList.get(s).getItemName());
            }
        }

        if (!lowStockItems.isEmpty()) {
            availabilityException.add("Requested quantity for the following item(s) is not available in the inventory: " + String.join(",", lowStockItems) + "\n");
        }
        if (!invalidItems.isEmpty()) {
            availabilityException.add("Following item(s) either have an invalid quantity or does not exist in inventory: " + String.join(",", invalidItems));
        }
        return availabilityException;
    }

    //Checks if there is no card on file.
    public String checkNoCard() {
        if (inputReader.getCardNumber() == null) {
            noCardException = "[There is no card on file. Please add a card and try again.]";
        }
        return noCardException;
    }

    //Verifies if input card is already in inventory database. If card is not in database, it adds the card to the database.
    public boolean hasCard(String cardNumber) {
        if (inventoryCardsList.contains(inputCardNumber)) {
            return true;
        }
        inventoryCardsList.add(cardNumber);
        return false;
    }

    //Checks to see if the input file has valid input values.
    public boolean isValidCart() throws IOException {
        checkItemCapLimit();
        checkItemAvailability();
        checkNoCard();

        if (capLimitException.size() == 0 && availabilityException.size() == 0 && noCardException.isEmpty()) {
            return true;
        }
        return false;
    }

    public Iterator getIterator() {
        return new ExceptionsListIterator();
    }

    private class ExceptionsListIterator implements Iterator {
        int index = 0;

        @Override
        public boolean hasNext() {
            if (index < allExceptions.size()) {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return allExceptions.get(index++);
            }
            return null;
        }
    }
}