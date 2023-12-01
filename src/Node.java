package src;

public class Node<T extends Comparable<T>> {
    public static final char RED = 'r';
    public static final char BLACK = 'b';

    public T data;
    public Node<T> left;
    public Node<T> right;
    public Node<T> parent;
    public char color;

    public Node(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = RED;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

 

    // @Override
    // public String toString() {
    //     return "Node [data=" + data + ", color=" + color + "]";
    // }

    // Método toString mejorado para visualizar el árbol
    @Override
    public String toString() {
        return toStringRecursive(this, "", true);
    }

    // Método auxiliar recursivo para construir la representación del árbol
    private String toStringRecursive(Node<T> node, String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();

        if (node.right != null) {
            sb.append(toStringRecursive(node.right, prefix + (isTail ? "│   " : "    "), false));
        }

        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.data.toString()).append(" (").append(node.color).append(")").append("\n");

        if (node.left != null) {
            sb.append(toStringRecursive(node.left, prefix + (isTail ? "    " : "│   "), true));
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node<T> other = (Node) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (color != other.color)
            return false;
        return true;
    }

    
}