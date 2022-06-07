package Process.ChainOfCommands;

import Entity.Item;

import java.io.IOException;

public interface Chain {

    public void setNextChain(Chain nextChain) throws IOException;
    public void checkCapLimit(Item item) throws IOException;
}
