package src;

public class Main {
    public static void main(String[] args) {
        TreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();
        tree.insert(55);
        tree.insert(10);
        tree.insert(90);
        tree.insert(20);
        tree.insert(5);
        tree.insert(2);
        tree.insert(30);
        tree.insert(15);

        System.out.println(tree);

        tree.delete(10);
        System.out.println(tree);

    }
}
