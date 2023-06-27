package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution


open class Part7A : PartSolution() {
    private lateinit var commands: List<Command>
    internal lateinit var tree: File

    override fun parseInput(text: String) {
        val commands: MutableList<Command> = mutableListOf()

        for (command in text.trim().split("$ ")) {
            if (command.isEmpty()) {
                continue
            }

            if (command.startsWith("ls")) {
                val output = command.split("\n").drop(1).filter { it.isNotEmpty() }
                commands.add(Ls(output))
            } else {
                commands.add(Cd(command.drop(3).trim()))
            }
        }

        commands.removeFirst()
        this.commands = commands.toList()
    }

    override fun config() {
        val root = File("/")
        var currentDir = root
        for (command in commands) {
            if (command is Ls) {
                command.apply(currentDir)
            } else if (command is Cd) {
                currentDir = command.apply(currentDir)
            }
        }

        tree = root
    }

    override fun compute(): Int {
        return sumSizes(tree)
    }

    private fun sumSizes(dir: File): Int {
        val dirSize = dir.getTotalSize()
        var size = 0
        if (dirSize < 100_000 && dir.size == 0) {
            size += dirSize
        }

        size += dir.files.sumOf { sumSizes(it) }
        return size
    }

    override fun getExampleInput(): String? {
        return """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent()
    }

    override fun getExampleAnswer(): Int {
        return 95_437
    }

    sealed class Command

    data class Cd(val arg: String): Command() {
        fun apply(currentDir: File): File {
            val result = if (arg.endsWith("..")) {
                currentDir.parent as File
            } else {
                currentDir.getFile(arg)
            }
            return result
        }
    }

    data class Ls(val output: List<String>): Command() {
        fun apply(dir: File) {
            for (line in output) {
                if (line.startsWith("dir")) {
                    dir.addFile(File(line.drop(4), parent=dir))
                } else {
                    val parts = line.split(" ")
                    dir.addFile(File(parts[1], parts[0].toInt(), dir))
                }
            }
        }
    }

    data class File(val name: String, val size: Int = 0, val parent: File? = null) {
        val files: MutableList<File> = mutableListOf()

        fun addFile(file: File) {
            files.add(file)
        }

        fun getFile(name: String): File {
            return files.first { it.name == name }
        }

        fun getTotalSize(): Int {
            return files.sumOf { it.getTotalSize() } + size
        }
    }
}

class Part7B : Part7A() {
    override fun compute(): Int {
        val needed = 30_000_000 - (70_000_000 - tree.getTotalSize())
        val (minSize, _) = getMinDir(tree, needed)

        return minSize
    }

    private fun getMinDir(dir: File, needed: Int): Pair<Int, File> {
        var minSize = dir.getTotalSize()
        var minDir = dir

        for (file in dir.files) {
            if (file.size != 0) {
                continue
            }
            val (childMinSize, childMinDir) = getMinDir(file, needed)
            if (childMinSize in (needed + 1)..minSize) {
                minDir = childMinDir
                minSize = childMinSize
            }
        }

        return Pair(minSize, minDir)
    }

    override fun getExampleAnswer(): Int {
        return 24_933_642
    }
}

fun main() {
    Day(2022, 7, Part7A(), Part7B())
}