import java.util.Comparator;

class SortByRang implements Comparator<TaskReduce>
{
    @Override
    public int compare(TaskReduce o1, TaskReduce o2) {
        return (o1.rang > o2.rang) ? -1 : ((o1.rang == o2.rang) ? 0 : 1);
    }
}