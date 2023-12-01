package src;

public class Main {
    public static void main(String[] args) {
        TreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();
        tree.insert(55);
        tree.insert(10);
        tree.insert(90);
        tree.insert(20);

        System.out.println(tree);
    }
}
