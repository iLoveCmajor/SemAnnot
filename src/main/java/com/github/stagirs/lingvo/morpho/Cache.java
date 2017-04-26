package com.github.stagirs.lingvo.morpho;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Integer.min;
import static java.lang.Math.abs;

/**
 * Created by Igor on 17.04.2017.
 */
public class Cache {
    public class node {
        String key;
        int hash;
        int value;
        node next = null;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private node[] hash_table = null;
    private Lock[] locks = null;
    private int max_list_lize;

    public Cache(int hash_table_size, int max_list_lize) {
        hash_table = new node[hash_table_size];
        locks = new Lock[hash_table_size];

        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
            this.max_list_lize = max_list_lize;
        }
    }

    Integer get(String s) {
        int hash = hash(s.hashCode());
        int index = hash & (hash_table.length - 1);
        node prev = hash_table[index];
        node current = prev;
        int counter = 0;

        while (current != null) {
            if (++counter == max_list_lize) {
                current.next = null;
            }
            if (current.hash == hash && current.key.equals(s)) {
                if (current == hash_table[index])
                    return current.value;
                node curr_next = current.next;
                current.next = hash_table[index];
                prev.next = curr_next;
                hash_table[index] = current;
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    void put(String key, int value) {
        int hash = hash(key.hashCode());
        int index = hash & (hash_table.length - 1);
        node n = new node();
        n.value = value;
        n.key = key;
        n.hash = hash;
        n.next = hash_table[index];
        hash_table[index] = n;
    }
}
