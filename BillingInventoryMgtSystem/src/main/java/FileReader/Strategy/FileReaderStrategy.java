package FileReader.Strategy;

import java.io.IOException;

public interface FileReaderStrategy<T> {

    public T readFile() throws IOException;

}
