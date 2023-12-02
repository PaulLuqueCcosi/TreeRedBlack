package src;

public class TreeRedBlack<T extends Comparable<T>> {

    private static int ROOT_SMALL = -1;
    private static int ROOT_BIG = 1;
    private static int ROOT_EQUALS = 0;
    private Node<T> root;

    // rotations
    public void rotateRight(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node<T> parent, Node<T> oldChild, Node<T> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    // recorridos
    // Preorder
    private void preOrderHelper(Node<T> node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderHelper(node.left);
            preOrderHelper(node.right);
        }
    }

    // Inorder
    private void inOrderHelper(Node node) {
        if (node != null) {
            inOrderHelper(node.left);
            System.out.print(node.data + " ");
            inOrderHelper(node.right);
        }
    }

    // Post order
    private void postOrderHelper(Node node) {
        if (node != null) {
            postOrderHelper(node.left);
            postOrderHelper(node.right);
            System.out.print(node.data + " ");
        }
    }

    // search
    public Node<T> search(T key) {
        Node<T> node = root;
        while (node != null) {
            if (key.equals(node.data)) {
                return node;
            } else if (key.compareTo(node.data) == ROOT_SMALL) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    // insert
    public boolean insert(T key) {

        // new node reference
        Node<T> newNode;

        // check root
        if (root == null) {
            newNode = new Node<T>(key);
            root = newNode;
        } else {
            try {
                newNode = insertDataAndGetNode(root, key);
            } catch (IllegalStateException e) {
                return false;
            }

        }

        fixRedBlackPropertiesAfterInsert(newNode);
        return true;
    }

    private Node<T> insertDataAndGetNode(Node<T> node, T value) {
        if (node == null) {
            Node<T> newNode = new Node<T>(value);
            return newNode;
        }

        if (value.compareTo(node.data) == ROOT_SMALL) {
            if (node.left == null) {
                Node<T> newNode = new Node<>(value);
                node.left = newNode;
                newNode.parent = node;
                return newNode;
            } else {
                return insertDataAndGetNode(node.left, value);
            }
        } else if (value.compareTo(node.data) == ROOT_BIG) {
            if (node.right == null) {
                Node<T> newNode = new Node<>(value);
                node.right = newNode;
                newNode.parent = node;
                return newNode;
            } else {
                return insertDataAndGetNode(node.right, value);
            }
        } else {
            throw new IllegalStateException("Repeat value");
        }
    }

    private Node<T> getUncle(Node<T> node) {
        Node<T> parent = node.parent;
        if (parent == null) {
            return null;
        }
        Node<T> grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private void fixRedBlackPropertiesAfterInsert(Node<T> node) {
        // https://es.wikipedia.org/wiki/%C3%81rbol_rojo-negro
        // https://github.com/SvenWoltmann/binary-tree/blob/main/src/main/java/eu/happycoders/binarytree/RedBlackTree.java#L345C15-L345C15

        fixInsertCase1(node);

        
    }

    private void fixInsertCase1(Node<T> node) {
        if (node.parent == null) {
            // System.out.println("Raiz roja, cambiamos a negro "+ node.data);
            node.color = Node.BLACK;
            return;
        } else {
            fixInsertCase2(node);
        }
    }

    private void fixInsertCase2(Node<T> node) {
        if (node.parent.color == Node.BLACK) {
            return;
        } else {
            fixInsertCase3(node);
        }
    }

    private void fixInsertCase3(Node<T> node) {

        Node<T> uncle = getUncle(node);

        if (uncle != null && uncle.color == Node.RED) {
            node.parent.color = Node.BLACK;
            uncle.color = Node.BLACK;
            node.parent.parent.color = Node.RED;
            fixInsertCase1(node.parent.parent);
        } else {
            fixInsertCase4(node);
        }
    }

    private void fixInsertCase4(Node<T> node) {
        Node<T> grandparent = node.parent.parent;

        if (node == node.parent.right && node.parent == grandparent.left) {
            rotateLeft(node.parent);
            node = node.left;
        } else if (node == node.left && node.parent == grandparent.right) {
            rotateRight(node.parent);
            node = node.right;
        }

        fixInsertCase5(node);
    }

    private void fixInsertCase5(Node<T> node) {
        Node<T> grandparent = node.parent.parent;

        node.parent.color = Node.RED;
        grandparent.color = Node.BLACK;
        if (node == node.parent.left && node.parent == grandparent.left) {
            rotateRight(grandparent);
        } else {
            rotateLeft(grandparent);
        }

    }

    @Override
    public String toString() {
        return root.toString();
    }

}
