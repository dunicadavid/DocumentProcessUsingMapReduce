import java.util.Map;

public class MyThreadReduce implements Runnable {
    public int thread_id;

    MyThreadReduce(int id) {
        this.thread_id = id;
    }

    @Override
    public void run() {

        int aux = 0;
        int nrWords = 0;

        //caut lungimea cuvantului maxim
        for (int j = 0; j < Tema2.tasksReduce.get(thread_id).maxWord.size(); j++) {
            if (Tema2.tasksReduce.get(thread_id).maxWord.get(j).length() > aux)
                aux = Tema2.tasksReduce.get(thread_id).maxWord.get(j).length();
        }
        Tema2.tasksReduce.get(thread_id).maxLenght = aux;
        aux = 0;

        //caut cate cuvinte de lungime maxima sunt
        for (int j = 0; j < Tema2.tasksReduce.get(thread_id).cacheList.size(); j++)
            if (Tema2.tasksReduce.get(thread_id).cacheList.get(j)
                    .containsKey(Tema2.tasksReduce.get(thread_id).maxLenght))
                aux += Tema2.tasksReduce.get(thread_id).cacheList.get(j)
                        .get(Tema2.tasksReduce.get(thread_id).maxLenght);

        Tema2.tasksReduce.get(thread_id).nrOfMax = aux;
        aux = 0;

        //calculez rangul
        for (int j = 0; j < Tema2.tasksReduce.get(thread_id).cacheList.size(); j++)
            for (Map.Entry<Integer, Integer> entry : Tema2.tasksReduce.get(thread_id).cacheList.get(j).entrySet()) {
                aux += TaskReduce.getFibonacci(entry.getKey()) * entry.getValue();
                nrWords += entry.getValue();
            }

        Tema2.tasksReduce.get(thread_id).rang = (double) aux / nrWords;

    }
}