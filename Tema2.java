import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Tema2 {

    public static List<TaskMap> tasks = new ArrayList<TaskMap>();
    public static List<TaskReduce> tasksReduce = new ArrayList<TaskReduce>();
    public static int workers;

    public static List<String> fileList = new ArrayList<String>();
    public static List<Integer> fileListSizes = new ArrayList<Integer>();
    public static int size;
    public static int filesNumber;

    public static void main(String args[]) throws FileNotFoundException, IOException {
        workers = Integer.parseInt(args[0]);
        String inputFile = args[1];
        String outputFile = args[2];

        // read first file
        try (FileInputStream file = new FileInputStream("./" + inputFile);
                InputStreamReader isr = new InputStreamReader(file);
                BufferedReader br = new BufferedReader(isr);) {
            size = Integer.parseInt(br.readLine());
            filesNumber = Integer.parseInt(br.readLine());

            for (int i = 0; i < filesNumber; i++) {
                // read the files inside file
                String fileExtr = br.readLine();
                int position = 0;
                String fileString = "";
                int fileSize = 0;

                RandomAccessFile file2 = new RandomAccessFile("./" + fileExtr, "r");
                while (position < file2.length() - size) {
                    file2.seek(position);
                    byte[] bytes = new byte[size];
                    file2.read(bytes);
                    String string = new String(bytes, "UTF-8");
                    fileString += string;
                    fileSize += size;
                    tasks.add(new TaskMap(fileExtr, position, size, i));
                    position += size;
                }

                file2.seek(position);
                byte[] bytes = new byte[size];
                file2.read(bytes);
                String string = new String(bytes, "UTF-8");
                fileString += string;
                fileSize += file2.length() - position;
                tasks.add(new TaskMap(fileExtr, position, file2.length() - position, i));
                fileList.add(new String(fileString));
                fileListSizes.add(fileSize);
                file2.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //creare workeri map
        Thread[] threads = new Thread[workers];
        for (int i = 0; i < workers; i++) {

            threads[i] = new Thread(new MyThreadMap(i));
            threads[i].start();

        }
        for (int i = 0; i < workers; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //preluare rezultate workersMap
        String fileCheck = tasks.get(0).file;
        TaskReduce aux = new TaskReduce(fileCheck);
        for (int i = 0; i < tasks.size();) {
            if (tasks.get(i).file == fileCheck) {
                aux.maxWord.addAll(tasks.get(i).maxWord);
                aux.cacheList.add(tasks.get(i).cache);
                i++;
            } else {
                tasksReduce.add(aux);
                fileCheck = tasks.get(i).file;
                aux = new TaskReduce(fileCheck);

            }
        }
        tasksReduce.add(aux);

        //creare workeri reduced
        threads = new Thread[filesNumber];
        for (int i = 0; i < filesNumber; i++) {

            threads[i] = new Thread(new MyThreadReduce(i));
            threads[i].start();

        }
        for (int i = 0; i < filesNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //sortare rezultate task reduce
        Collections.sort(tasksReduce, new SortByRang());


        //afisare in fisier
        try {
            FileWriter myWriter = new FileWriter("./" + outputFile);
            for (int i = 0; i < tasksReduce.size(); i++)
                myWriter.write(tasksReduce.get(i).file.replace("tests/files/", "") + ","
                        + String.format("%.2f", tasksReduce.get(i).rang) + "," + tasksReduce.get(i).maxLenght + ","
                        + tasksReduce.get(i).nrOfMax + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}