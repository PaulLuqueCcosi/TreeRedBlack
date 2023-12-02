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

    // delete
    public boolean delete(T key) {
        Node<T> node = search(key);

        // Node not found?
        if (node == null) {
            return false;
        }

        // At this point, "node" is the node to be deleted

        // In this variable, we'll store the node at which we're going to start to fix
        // the R-B
        // properties after deleting a node.
        Node<T> movedUpNode;
        int deletedNodeColor;

        // Node has zero or one child
        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        }

        // Node has two children
        else {
            // Find minimum node of right subtree ("inorder successor" of current node)
            Node<T> inOrderSuccessor = findMinimum(node.right);

            // Copy inorder successor's data to current node (keep its color!)
            node.data = inOrderSuccessor.data;

            // Delete inorder successor just as we would delete a node with 0 or 1 child
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.color;
        }

        if (deletedNodeColor == Node.BLACK) {
            // corrigiendo
            fixRedBlackPropertiesAfterDelete(movedUpNode);

            // Remove the temporary NIL node
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }else{
            System.out.println("Nodo elimiando ROjo no se corrige");
        }

        return true;
    }

    private Node<T> findMinimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node<T> deleteNodeWithZeroOrOneChild(Node<T> node) {
        // Node has ONLY a left child --> replace by its left child
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        }

        // Node has ONLY a right child --> replace by its right child
        else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        }

        // Node has no children -->
        // * node is red --> just remove it
        // * node is black --> replace it by a temporary NIL node (needed to fix the R-B
        // rules)
        else {
            Node<T> newChild = (node.color == Node.BLACK) ? new NilNode() : null;
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private void fixRedBlackPropertiesAfterDelete(Node<T> movedUpNode) {
        fixDeleteCase0(movedUpNode);
    }

    private void fixDeleteCase0(Node<T> node) {

        System.out.println("Caso 0 : " + node.data);
        if (node == root) {
            node.color = Node.BLACK;
            return;
        } else {
            fixDeleteCase1(node);
        }
    }

    private void fixDeleteCase1(Node<T> node) {
        System.out.println("Caso 1 : " + node.data);

        Node<T> sibling = getSibling(node);

        if (sibling.color == Node.RED && node.parent.color == Node.BLACK) {
            sibling.color = Node.BLACK;
            node.parent.color = Node.RED;

            if (node == node.parent.left) {
                rotateLeft(node.parent);
            } else {
                rotateRight(node.parent);
            }
            System.out.println(this.toString());
            fixDeleteCase1(node);

        } else {
            fixDeleteCase2(node);
        }

    }

    private void fixDeleteCase2(Node<T> node) {
        System.out.println("Caso 2 : " + node.data);

        Node<T> sibling = getSibling(node);

        if (sibling != null && sibling.color == Node.BLACK && node.parent.color == Node.BLACK
                && isBlackOrNull(sibling.left) && isBlackOrNull(sibling.right)) {
            sibling.color = Node.RED;
            fixDeleteCase0(node.parent);
        } else {
            fixDeleteCase3(node);
        }
    }

    private void fixDeleteCase3(Node<T> node) {
        System.out.println("Caso 3 : " + node.data);

        Node<T> sibling = getSibling(node);

        if (sibling != null && sibling.color == Node.BLACK && node.parent.color == Node.RED
                && isBlackOrNull(sibling.left) && isBlackOrNull(sibling.right)) {
            sibling.color = Node.RED;
            node.parent.color = Node.BLACK;
            // comple todas las condiciones

            // fixDeleteCase0(node.parent);
        } else {
            fixDeleteCase4(node);
        }
    }

    private void fixDeleteCase4(Node<T> node) {
        System.out.println("Caso 4 : " + node.data);

        Node<T> sibling = getSibling(node);

        // x in left
        if (node == node.parent.left) {
            if (sibling != null && sibling.color == Node.BLACK && node.parent != null && isRed(sibling.left)
                    && isBlackOrNull(sibling.right)) {

                sibling.color = Node.RED;
                sibling.left.color = Node.BLACK;

                rotateRight(sibling);
                fixDeleteCase5(node);

            } else {
                fixDeleteCase5(node);
            }
        } else {
            if (sibling != null && sibling.color == Node.BLACK && node.parent != null && isRed(sibling.right)
                    && isBlackOrNull(sibling.left)) {

                sibling.color = Node.RED;
                sibling.right.color = Node.BLACK;

                rotateLeft(sibling);
                fixDeleteCase5(node);

            } else {
                fixDeleteCase5(node);
            }
        }

    }

    private void fixDeleteCase5(Node<T> node) {
        System.out.println("Caso 5 : " + node.data);

        Node<T> sibling = getSibling(node);

        // x in left
        if (node == node.parent.left) {
            if (sibling != null && sibling.color == Node.BLACK && node.parent != null && isRed(sibling.right)) {

                sibling.color = node.parent.color;
                node.parent.color = Node.BLACK;
                sibling.right.color = Node.BLACK;
                rotateLeft(node.parent);
                // sibling.left.color = Node.BLACK;

            }

        } else {
            if (sibling != null && sibling.color == Node.BLACK && node.parent != null && isRed(sibling.left)) {

                sibling.color = node.parent.color;
                node.parent.color = Node.BLACK;
                sibling.left.color = Node.BLACK;
                rotateRight(node.parent);
                // sibling.left.color = Node.BLACK;

            }
        }
    }

    private boolean isBlackOrNull(Node<T> node) {
        return node == null || node.color == Node.BLACK;
    }

    private boolean isRed(Node<T> node) {
        return node != null || node.color == Node.RED;
    }

    private Node<T> getSibling(Node<T> node) {
        Node<T> parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private static class NilNode<T> extends Node {
        private NilNode() {
            super(0);
            this.color = BLACK;
        }
    }
}
