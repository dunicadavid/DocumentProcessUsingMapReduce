
public class MyThreadMap implements Runnable {
    public int thread_id;

    MyThreadMap(int id) {
        this.thread_id = id;
    }

    @Override
    public void run() {

        for (int i = thread_id; i < Tema2.tasks.size(); i += Tema2.workers) {
            int position = 0;
            int lenght = 0;
            String word = "";
            int fileIndex = Tema2.tasks.get(i).fileIndex;
            
            //ajung la inceputul unui cuvant nou din
            if (Tema2.tasks.get(i).offset != 0) {
                if (TaskMap.CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset))) {
                    // scap de spatii goale
                    while (TaskMap
                            .CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position))
                            && position < Tema2.tasks.get(i).size) {
                        position++;
                    }
                } else if (!TaskMap.CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset - 1))) {
                    // scap de cuvantul precedent
                    while (!TaskMap
                            .CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position))
                            && position < Tema2.tasks.get(i).size) {
                        position++;
                    }
                    //dupa cuv precedent scap de spatii
                    while (TaskMap
                            .CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position))
                            && position < Tema2.tasks.get(i).size) {
                        position++;
                    }
                }
            }

            //fac operatiile din zona workerului de cautare
            while (position < Tema2.tasks.get(i).size) {
                if (TaskMap.CheckEndOfWord(Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position))) {
                    if (lenght > 0) {
                        Tema2.tasks.get(i).checkAndAddInMap(lenght);
                        if (lenght == Tema2.tasks.get(i).maxLenght) {
                            Tema2.tasks.get(i).maxWord.add(word);
                        } else if (lenght > Tema2.tasks.get(i).maxLenght) {
                            Tema2.tasks.get(i).maxWord.clear();
                            Tema2.tasks.get(i).maxWord.add(word);
                            Tema2.tasks.get(i).maxLenght = lenght;
                        }
                        lenght = 0;
                        word = "";
                    }
                } else {
                    lenght++;
                    word += Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position);
                }
                position++;
            }

            //daca cuv nu se termina continui sa-l masor si peste limitele sale de cautare
            if (lenght > 0 && i < Tema2.tasks.size() - 1) {
                while (lenght != 0 && Tema2.tasks.get(i).offset + position < Tema2.fileListSizes.get(fileIndex)) {
                    if (TaskMap.CheckEndOfWord(
                            Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position))) {
                        Tema2.tasks.get(i).checkAndAddInMap(lenght);
                        if (lenght == Tema2.tasks.get(i).maxLenght) {
                            Tema2.tasks.get(i).maxWord.add(word);
                        } else if (lenght > Tema2.tasks.get(i).maxLenght) {
                            Tema2.tasks.get(i).maxWord.clear();
                            Tema2.tasks.get(i).maxWord.add(word);
                            Tema2.tasks.get(i).maxLenght = lenght;
                        }
                        lenght = 0;
                        word = "";
                        position = Tema2.fileList.get(fileIndex).length();
                    } else {
                        lenght++;
                        word += Tema2.fileList.get(fileIndex).charAt(Tema2.tasks.get(i).offset + position);
                    }
                    position++;
                }
            }

            //verificare speciala (intra aici numai cand se ajunge la finalul ultimului cuvant din fisierului)
            if (lenght > 0) {
                Tema2.tasks.get(i).checkAndAddInMap(lenght);
                if (lenght == Tema2.tasks.get(i).maxLenght) {
                    Tema2.tasks.get(i).maxWord.add(word);
                } else if (lenght > Tema2.tasks.get(i).maxLenght) {
                    Tema2.tasks.get(i).maxWord.clear();
                    Tema2.tasks.get(i).maxWord.add(word);
                    Tema2.tasks.get(i).maxLenght = lenght;
                }
            }
        }

    }
}