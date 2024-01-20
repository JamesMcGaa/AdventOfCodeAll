import java.io.File

fun main(args: Array<String>) {

    val lines = File("inputs/input7.txt").readLines()
    val root = Node("/", false, 0, null)
    var current = root
    var counter = 1
    while(counter < lines.size) {
        val line = lines[counter]
        val parsed = line.split(" ")

        if (parsed[1] == "cd") {
            if (parsed[2] == "..") {
                current = current.parent!!
            }
            else {
                current = current.children[parsed[2]]!!
            }
            counter += 1
        }

        else { // ls
            counter += 1
            while (counter < lines.size && lines[counter].split(" ")[0] != "$") {
                val s = lines[counter].split(" ")
                val isFolder = s[0] == "dir"
                if (isFolder) {
                    current.children[s[1]] = Node(s[1], false, 0, current)
                }
                else {
                    current.children[s[1]] = Node(s[1], true, Integer.parseInt(s[0]), current)
                }
                counter += 1
            }
        }
    }

    var counterSize = 0
    Node.allNodes.forEach {
        if (it.getSize() <= 100000 && !it.isFile) {
            counterSize += it.getSize()
        }
    }
    println(counterSize)

    val excess = root.getSize() - 40000000
    Node.allNodes.sortBy { it.getSize() }
    Node.allNodes.forEach {
        if (it.getSize() > excess) {
            println("${it.getSize()}")
            return
        }
    }
}

class Node(val name: String, val isFile: Boolean, val fileSize: Int, val parent: Node?) {
    companion object {
        val allNodes = mutableListOf<Node>()
    }

    init {
        allNodes.add(this)
    }

    val children = HashMap<String, Node>()

    fun addChild(node: Node) {
        children[node.name] = node
    }

    fun getSize(): Int {
        if(isFile) {
            return fileSize
        }
        return children.values.map { node -> node.getSize() }.sum()
    }
}


