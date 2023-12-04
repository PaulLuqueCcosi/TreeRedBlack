package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import src.InterfaceTreeRedBlack;
import src.TreeRedBlack;

public class TreeRedBlackTest {
    @Test
    void testClear() {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();

        assertTrue(tree.isEmpty());
        tree.insert(Integer.valueOf(12));
        assertFalse(tree.isEmpty());

        tree.clear();
        assertTrue(tree.isEmpty());

    }

    @Test
    void testDelete() {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();

        tree.insert(Integer.valueOf(10));
        tree.insert(Integer.valueOf(20));
        tree.insert(Integer.valueOf(30));

        tree.delete(Integer.valueOf(30));
        assertEquals("10r 20b", tree.inOrder());

        tree.insert(Integer.valueOf(30));
        tree.insert(Integer.valueOf(40));
        assertEquals("10b 20b 30b 40r", tree.inOrder());

        tree.delete(Integer.valueOf(20));
        assertEquals("10r 30b 40r", tree.inOrder());

    }

    @Test
    void testInsert() {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();

        tree.insert(Integer.valueOf(10));
        tree.insert(Integer.valueOf(20));
        tree.insert(Integer.valueOf(30));
        tree.insert(Integer.valueOf(15));
        tree.insert(Integer.valueOf(45));
        tree.insert(Integer.valueOf(5));
        tree.insert(Integer.valueOf(25));
        tree.insert(Integer.valueOf(27));
        System.out.println(tree);
        assertEquals("5r 10b 15r 20b 25b 27r 30r 45b", tree.inOrder());

        assertFalse(tree.insert(5));
        assertEquals("5r 10b 15r 20b 25b 27r 30r 45b", tree.inOrder());

    }

    @Test
    void testIsEmpty() {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();

        assertTrue(tree.isEmpty());

        tree.insert(Integer.valueOf(10));

        assertFalse(tree.isEmpty());

        tree.delete(Integer.valueOf(10));
        assertTrue(tree.isEmpty());

    }

    @Test
    void testSearch() {
        InterfaceTreeRedBlack<Integer> tree = new TreeRedBlack<Integer>();

        tree.insert(Integer.valueOf(10));
        tree.insert(Integer.valueOf(20));

        assertTrue(tree.search(Integer.valueOf(10)));
        assertTrue(tree.search(Integer.valueOf(20)));

        assertFalse(tree.search(Integer.valueOf(30)));

    }
}
