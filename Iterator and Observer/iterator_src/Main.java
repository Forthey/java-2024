import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // Примеры с собственным списком
        CustomList<Integer> listNumbers = new CustomList<>();

        listNumbers.add(6);
        listNumbers.add(5);
        listNumbers.add(4);
        listNumbers.add(3);
        listNumbers.add(2);
        listNumbers.add(1);

        System.out.println("Явное использование итератора");

        Iterator<Integer> iter = listNumbers.iterator();
        while (iter.hasNext()) {
            System.out.printf("\t%d\n", iter.next());
        }

        System.out.println("\nОбычный Iterator в цикле for-each:");

        for (int number : listNumbers) {
            System.out.printf("\t%d\n", number);
        }

        System.out.println("\nОбратный перебор с ListIterator");

        ListIterator<Integer> listIter = listNumbers.listIterator(listNumbers.size() - 1);
        while (listIter.hasPrevious()) {
            System.out.printf("\t%d\n", listIter.previous());
        }

        System.out.println("\nПример со Spliterator:");

        Spliterator<Integer> spliterator = listNumbers.spliterator();

        // Разделяем Spliterator
        Spliterator<Integer> firstHalf = spliterator.trySplit();

        Consumer<Integer> printElement = (Integer el) -> System.out.printf("\t%d\n", el);

        System.out.println("\nПервая половина:");
        firstHalf.forEachRemaining(printElement);

        System.out.println("\nВторая половина:");
        spliterator.forEachRemaining(printElement);

        // Используем Stream API с Spliterator
        System.out.println("\nОбработка через Stream API:");
        Stream<Integer> stream = listNumbers.stream();
        stream.forEach(printElement);
    }
}
