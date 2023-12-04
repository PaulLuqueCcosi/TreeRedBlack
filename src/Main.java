package src;

public class Main {
    public static void main(String[] args) {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();
        tree.insert(55);
        tree.insert(10);
        tree.insert(90);
        tree.insert(20);
        tree.insert(5);
        tree.insert(2);
        tree.insert(30);
        tree.insert(15);

        System.out.println(tree);
        System.out.println("Eliminacion");
        tree.delete(55);
        System.out.println(tree);

    }
}
