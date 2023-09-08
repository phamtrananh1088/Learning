package jp.co.toukei.log.lib.util

class DynamicTree<T>(root: T) {

    private val rootNode = Node(0, null, root, null)

    class NodeData<T>(
            @JvmField val element: T,
            @JvmField val parent: T?,
            @JvmField val path: IntArray,
            @JvmField val tree: DynamicTree<T>
    ) {

        fun list(): List<NodeData<T>>? {
            return tree.findNode(path)?.let(tree::childrenData)
        }

        fun parent(): NodeData<T>? {
            val s = path.size - 1
            if (s > 0) {
                return tree.findNode(path.copyOf(s))?.let(tree::toNodeData)
            }
            return null
        }

        fun setChildren(children: Iterable<T>, merge: Boolean): Boolean {
            val node = tree.findNode(path) ?: return false
            node.apply {
                val o = if (merge) childrenSet?.associateBy(Node<T>::element) else null
                childrenSet = children.distinct().mapIndexed { index: Int, t: T ->
                    Node(index, this, t, o?.get(t)?.childrenSet)
                }
            }
            return true
        }
    }

    private class Node<T>(
            private val index: Int,
            parent: Node<T>?,
            @JvmField val element: T,
            @JvmField var childrenSet: List<Node<T>>?
    ) {

        private val pe = parent?.element

        private val path: IntArray = parent?.path.let { it?.plus(index) ?: intArrayOf() }

        fun toNodeData(tree: DynamicTree<T>): NodeData<T> {
            return NodeData(element, pe, path, tree)
        }
    }

    private fun childrenData(node: Node<T>) = node.childrenSet?.map(::toNodeData)

    private fun toNodeData(node: Node<T>): NodeData<T> {
        return node.toNodeData(this)
    }

    fun root() = toNodeData(rootNode)

    private fun findNode(path: IntArray): Node<T>? {
        return path.fold(rootNode) { acc, index ->
            acc.childrenSet?.getOrNull(index) ?: return null
        }
    }
}
