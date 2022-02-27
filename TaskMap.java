import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskMap {
    public String file;
    public int fileIndex;
    public long size;
    public int offset;
    public int maxLenght;
    public List<String> maxWord;
    HashMap<Integer, Integer> cache;

    public TaskMap(String text, int offset, long size, int index) {
        this.file = text;
        this.fileIndex = index;
        this.offset = offset;
        this.size = size;
        this.maxLenght = 0;
        this.maxWord = new ArrayList<>(Arrays.asList());
        this.cache = new HashMap<Integer, Integer>();
    }

    public static boolean CheckEndOfWord(char c) {
        String check = ";:/?~\\.,><`[]{}()!@#$%^&- +'=*\"|   \n\r";
        if (check.contains(String.valueOf(c)))
            return true;
        else
            return false;
    }

    public void checkAndAddInMap(int value) {
        int count = this.cache.containsKey(value) ? this.cache.get(value) : 0;
        this.cache.put(value, count + 1);
    }
}
