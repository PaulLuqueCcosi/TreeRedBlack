package src;

public interface InterfaceTreeRedBlack<T extends Comparable<T>> {

    // search
    boolean search(T key);

    // insert
    boolean insert(T key);

    // delete
    boolean delete(T key);

    void clear();

    boolean isEmpty();

    String preOrder();

    String inOrder();

    String postOrder();

}