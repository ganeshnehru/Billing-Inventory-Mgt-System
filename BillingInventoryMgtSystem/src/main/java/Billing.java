import FileReader.InputReader;
import FileReader.InventoryReader;
import Process.ProcessHandler;

import java.io.IOException;

public class Billing {

    public static void main(String[] args) throws IOException {
        InventoryReader inventoryReader = new InventoryReader();
        inventoryReader.readFile();

        InputReader inputReader = new InputReader();
        inputReader.readFile();

        ProcessHandler processHandler = new ProcessHandler();
        processHandler.process();

    }
}
