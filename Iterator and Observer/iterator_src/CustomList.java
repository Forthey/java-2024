import java.util.*;
import java.util.function.Consumer;


public class CustomList<T> implements List<T> {
    private T[] elements;
    private int size = 0;


    public CustomList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        elements = (T[]) new Object[initialCapacity];
    }

    public CustomList() {
        this(0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U[] toArray(U[] a) {
        if (a.length < size) {
            return (U[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public T remove(int index) {
        rangeCheck(index);
        T oldElement = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Удаляем последний элемент
        return oldElement;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        ensureCapacity(size + c.size());
        for (T item : c) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        rangeCheckForAdd(index);
        int numNew = c.size();
        ensureCapacity(size + numNew);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        int i = index;
        for (T item : c) {
            elements[i++] = item;
        }
        size += numNew;
        return numNew != 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            modified |= remove(item);
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i--);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    @Override
    public T set(int index, T element) {
        rangeCheck(index);
        T oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public void add(int index, T element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList not implemented");
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new CustomListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new CustomListIterator(index);
    }

    @Override
    public Spliterator<T> spliterator() {
        return new CustomListSpliterator(0, size);
    }


    class CustomIterator implements Iterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            return elements[cursor++];
        }
    };

    private class CustomListIterator implements ListIterator<T> {
        private int cursor;
        private int lastRet = -1;

        public CustomListIterator() {
            this(0);
        }

        public CustomListIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return elements[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor >= 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            T value = elements[cursor];
            lastRet = --cursor;
            return value;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            CustomList.this.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
        }

        @Override
        public void set(T e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            CustomList.this.set(lastRet, e);
        }

        @Override
        public void add(T e) {
            CustomList.this.add(cursor++, e);
            lastRet = -1;
        }
    }

    private class CustomListSpliterator implements Spliterator<T> {
        private int current;
        private final int end;

        public CustomListSpliterator(int start, int end) {
            this.current = start;
            this.end = end;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (current < end) {
                action.accept(elements[current++]);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int mid = (current + end) >>> 1;
            if (mid <= current) {
                return null;
            }
            CustomListSpliterator split = new CustomListSpliterator(current, mid);
            current = mid;
            return split;
        }

        @Override
        public long estimateSize() {
            return end - current;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}