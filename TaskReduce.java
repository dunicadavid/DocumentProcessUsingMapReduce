import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskReduce {
    List<HashMap<Integer, Integer>> cacheList;
    public List<String> maxWord;
    public String file;

    public int maxLenght;
    public int nrOfMax;
    public double rang;

    public TaskReduce(String text) {
        this.file = text;
        this.maxWord = new ArrayList<>(Arrays.asList());
        this.cacheList = new ArrayList<>(Arrays.asList());
    }

    static int getFibonacci(int n) {
        if (n <= 2)
            return n;
        return getFibonacci(n - 1) + getFibonacci(n - 2);
    }
}