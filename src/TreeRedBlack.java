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
    public void insert(T key) {
        Node<T> node = root;
        Node<T> parent = null;

        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (key.compareTo(node.data) == ROOT_SMALL) {
                node = node.left;
            } else if (key.compareTo(node.data) == ROOT_BIG) {
                node = node.right;
            } else {
                throw new IllegalArgumentException("BST already contains a node with key " + key);
            }
        }

        // Insert new node
        Node<T> newNode = new Node<T>(key);
        newNode.color = Node.RED;
        if (parent == null) {
            root = newNode;
        } else if (key.compareTo(parent.data) == ROOT_SMALL) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private Node<T> getUncle(Node<T> parent) {
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
        

        Node<T> parent = node.parent;

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        // Parent is black --> nothing to do
        if (parent.color == Node.BLACK) {
            return;
        }

        // From here on, parent is red
        Node grandparent = parent.parent;

        // Case 2:
        // Not having a grandparent means that parent is the root. If we enforce black
        // roots
        // (rule 2), grandparent will never be null, and the following if-then block can
        // be
        // removed.
        if (grandparent == null) {
            // As this method is only called on red nodes (either on newly inserted ones -
            // or -
            // recursively on red grandparents), all we have to do is to recolor the root
            // black.
            parent.color = Node.BLACK;
            return;
        }

        // Get the uncle (may be null/nil, in which case its color is BLACK)
        Node<T> uncle = getUncle(parent);

        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == Node.RED) {
            parent.color = Node.BLACK;
            grandparent.color = Node.RED;
            uncle.color = Node.BLACK;

            // Call recursively for grandparent, which is now red.
            // It might be root or have a red parent, in which case we need to fix more...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Note on performance:
        // It would be faster to do the uncle color check within the following code.
        // This way
        // we would avoid checking the grandparent-parent direction twice (once in
        // getUncle()
        // and once in the following else-if). But for better understanding of the code,
        // I left the uncle color check as a separate step.

        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 4a: Uncle is black and node is left->right "inner child" of its
            // grandparent
            if (node == parent.right) {
                rotateLeft(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5a: Uncle is black and node is left->left "outer child" of its
            // grandparent
            rotateRight(grandparent);

            // Recolor original parent and grandparent
            parent.color = Node.BLACK;
            grandparent.color = Node.RED;
        }

        // Parent is right child of grandparent
        else {
            // Case 4b: Uncle is black and node is right->left "inner child" of its
            // grandparent
            if (node == parent.left) {
                rotateRight(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5b: Uncle is black and node is right->right "outer child" of its
            // grandparent
            rotateLeft(grandparent);

            // Recolor original parent and grandparent
            parent.color = node.BLACK;
            grandparent.color = Node.RED;
        }
    }

    
    

    @Override
    public String toString() {
        return root.toString();
    }

    
}
