package com.example

import java.util.LinkedList
import java.util.Queue
import kotlin.math.min

/**
 * A simple implementation of the AA tree data structure.
 *
 * AA trees are self-balancing binary search trees that maintain a logarithmic height by using a set of simple rules to
 * ensure that the tree remains balanced. This implementation supports insertion, deletion, and searching in O(log n) time
 * on average.
 *
 * For more information on AA trees, see the Wikipedia page at:
 * https://en.wikipedia.org/wiki/AA_tree
 *
 * This implementation is based on the paper "Balanced Search Trees Made Simple" by Arne Andersson, which can be found at:
 * http://user.it.uu.se/~arnea/abs/simp.html
 *
 * @param T the type of values stored in the tree, which must be comparable
 */
class AaTree<T : Comparable<T>> {

    private var treeRoot: Node<T>? = null
    private var size = 0

    /**
     * Adds a new value to the AA tree
     * @param value the value to be added to the tree
     */
    fun add(value: T) {
        treeRoot = insert(value, treeRoot)
    }

    /**
     * Removes the given value from the AA tree
     *
     * @param value the value to be removed from the tree
     */
    fun remove(value: T) {
        treeRoot = delete(value, treeRoot)
    }

    /**
     * Checks if the given value is contained in the AA tree.
     *
     * @param value the value to be checked
     * @return {@code true} if the value is contained in the tree, or {@code false} if the value is not contained in the tree
     */
    fun contains(value: T): Boolean {
        return contains(value, treeRoot)
    }

    /**
     * Returns the number of nodes in the AA tree.
     */
    fun size(): Int {
        return size
    }

    /**
     * Checks if the given value is contained in the given AA tree.
     *
     * @param value the value to be checked
     * @param root the local root of the tree to check the value in
     */
    private fun contains(value: T, root: Node<T>?): Boolean = when {
        root == null -> false
        value == root.value -> true
        value < root.value -> contains(value, root.left)
        else -> contains(value, root.right)
    }

    /**
     * Inserts a new value into the given AA tree and returns a balanced version of the tree.
     *
     * @param value the value to be inserted
     * @param root the local root of the tree to insert the value into
     * @return a balanced version of the local tree including the inserted value
     */
    private fun insert(value: T, root: Node<T>?): Node<T> {
        if (root == null) {
            size++
            return Node(value)
        } else if (value < root.value) {
            root.left = insert(value, root.left)
        } else if (value > root.value) {
            root.right = insert(value, root.right)
        } else {
            // value == localRoot.value
            return root
        }
        var node = skew(root)
        node = split(node)!!
        return node
    }

    /**
     * Deletes the given value from the AA tree rooted at the given node.
     *
     * @param value the value to be deleted from the tree
     * @param root the root of the AA tree from which the value should be deleted
     * @return the root of the tree after the deletion and re-balancing
     */
    private fun delete(value: T, root: Node<T>?): Node<T>? {
        if (root == null) {
            return null
        } else if (value > root.value) {
            root.right = delete(value, root.right)
        } else if (value < root.value) {
            root.left = delete(value, root.left)
        } else {
            // If we're a leaf, easy, otherwise reduce to leaf case.
            if (root.isLeaf()) {
                size--
                return null
            } else if (root.left == null) {
                val successor = root.successor()!!
                root.right = delete(successor.value, root.right)
                root.value = successor.value
            } else {
                val predecessor = root.predecessor()!!
                root.left = delete(predecessor.value, root.left)
                root.value = predecessor.value
            }
        }
        // Re-balance the tree.
        var node = decreaseLevel(root)
        node = skew(node)!!
        node.right = skew(node.right)
        if (node.right != null) {
            node.right!!.right = skew(node.right!!.right)
        }
        node = split(node)!!
        node.right = split(node.right)
        return node
    }

    /**
     *  Decreases the level of the given AA tree node by removing links that skip levels.
     *
     *  @param node a node representing an AA tree for which we want to remove links that skip levels
     *  @return the same node with its level decreased
     */
    private fun decreaseLevel(node: Node<T>?): Node<T>? {
        if (node == null) {
            return null
        }
        val shouldBe = min(node.left?.level ?: 0, node.right?.level ?: 0) + 1
        if (shouldBe < node.level) {
            node.level = shouldBe
            if (shouldBe < (node.right?.level ?: 0)) {
                node.right!!.level = shouldBe
            }
        }
        return node
    }

    /**
     * Re-balances the given AA tree node using a skew operation.
     * Skew is a rotation operation that is performed when the left child of a node
     * has the same level as the node itself.
     *
     * @param node a node representing an AA tree that needs to be rebalanced
     * @return another node representing the rebalanced AA tree
     */
    private fun skew(node: Node<T>?): Node<T>? = when {
        node == null -> null

        node.left == null -> node

        node.left!!.level == node.level -> {
            // Swap the pointers of horizontal left links
            val leftChild = node.left
            node.left = leftChild!!.right
            leftChild.right = node
            leftChild
        }

        else -> node
    }

    /**
     * Re-balances the given AA tree node using a split operation.
     *
     * @param node a node representing an AA tree that needs to be rebalanced
     * @return another node representing the rebalanced AA tree
     */
    private fun split(node: Node<T>?): Node<T>? = when {
        node == null -> null

        node.right == null || node.right!!.right == null -> node

        node.level == node.right!!.right!!.level -> {
            // We have two horizontal right links. Take the middle node, elevate it, and return it.
            val rightChild = node.right
            node.right = rightChild!!.left
            rightChild.left = node
            rightChild.level = rightChild.level + 1
            rightChild
        }

        else -> node
    }

    /**
     * A node in an AA tree.
     */
    internal class Node<T>(var value: T) {
        var left: Node<T>? = null
        var right: Node<T>? = null
        var level: Int = 1

        fun successor(): Node<T>? {
            var node: Node<T>? = this.right
            while (node?.left != null) {
                node = node.left
            }
            return node
        }

        fun predecessor(): Node<T>? {
            var node: Node<T>? = this.left
            while (node?.right != null) {
                node = node.right
            }
            return node
        }

        fun isLeaf(): Boolean {
            return left == null && right == null
        }
    }

    companion object {

        internal fun visualizeTree(tree: AaTree<*>) {
            visualizeTree(tree.treeRoot)
        }

        private fun visualizeTree(root: Node<*>?) {
            if (root == null) {
                println("<empty>")
                return
            }
            val queue: Queue<Node<*>> = LinkedList<Node<*>>()
            queue.add(root)
            var currentLevel = root.level
            while (queue.isNotEmpty()) {
                val levelSize = queue.size
                for (i in 0 until levelSize) {
                    val node = queue.remove()
                    if (node.level != currentLevel) {
                        println()
                        currentLevel = node.level
                    }
                    print(" ${node.value} ")
                    if (node.left != null) {
                        queue.add(node.left!!)
                    }
                    if (node.right != null) {
                        queue.add(node.right!!)
                    }
                }
            }
            println()
        }
    }
}
