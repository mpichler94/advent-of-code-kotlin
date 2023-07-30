package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Test

open class Part16A : PartSolution() {
    lateinit var packet: Packet

    override fun parseInput(text: String) {
        val binData = text.map { it.digitToInt(16).toString(2) }
            .joinToString(separator = "") { it.padStart(4, '0') }
        packet = Packet(binData)
    }

    override fun compute(): Long {
        return packet.versionSum.toLong()
    }

    override fun getExampleAnswer(): Int {
        return 16
    }

    override fun getExampleInput(): String? {
        return "8A004A801A8002F478"
    }

    override fun tests(): Sequence<Test> {
        return sequence {
            yield(Test("620080001611562C8802118E34", 12, "Operator packet(v3) with 2 subPackets"))
            yield(Test("C0015000016115A2E0802F182340", 23, "Operator packet"))
            yield(Test("A0016C880162017C3686B18A3D4780", 31, "Multiple nested operator packets"))
        }
    }

    private fun Packet(line: String): Packet {
        val version = line.substring(0, 3).toInt(2)
        val type = line.substring(3, 6).toInt(2)
        val content = line.substring(6)

        return if (type == 4) { // literal packet
            val (length, literal) = parseLiteral(content)
            Packet(version, type, length + 6, literal)
        } else {
            val (length, subPackets) = parseSubPackets(content)
            Packet(version, type, length + 6, subPackets = subPackets)
        }
    }

    private fun parseLiteral(text: String): Pair<Int, Long> {
        var value = ""
        var length = 0
        var txt = text
        while (true) {
            value += txt.substring(1, 5)
            length += 5
            if (txt.first() == '0') {
                break
            }
            txt = txt.substring(5)
        }
        return length to value.toLong(2)
    }

    private fun parseSubPackets(txt: String): Pair<Int, List<Packet>> {
        var length = 1
        var text = txt
        val subPackets = mutableListOf<Packet>()
        val lengthType = text.first()

        val subPacketLength: Int
        if (lengthType == '0') {
            subPacketLength = text.substring(1, 16).toInt(2)
            length += 15
            text = text.substring(16)
        } else {
            subPacketLength = text.substring(1, 12).toInt(2)
            length += 11
            text = text.substring(12)
        }

        var currentLength = 0
        while (currentLength < subPacketLength) {
            val subPacket = Packet(text)
            subPackets.add(subPacket)
            length += subPacket.length
            if (text.length > subPacket.length) {
                text = text.substring(subPacket.length)
            }
            currentLength += if (lengthType == '1') 1 else subPacket.length
        }

        return length to subPackets
    }

    data class Packet(
        val version: Int,
        val type: Int,
        val length: Int,
        val literal: Long = 0,
        val subPackets: List<Packet> = listOf()
    ) {
        val versionSum: Int get() = version + subPackets.sumOf { it.versionSum }

        val value: Long
            get() {
                return when (type) {
                    0 -> subPackets.sumOf { it.value }
                    1 -> subPackets.map { it.value }.reduce { acc, v -> acc * v }
                    2 -> subPackets.minOf { it.value }
                    3 -> subPackets.maxOf { it.value }
                    4 -> literal
                    5 -> if (subPackets[0].value > subPackets[1].value) 1 else 0
                    6 -> if (subPackets[0].value < subPackets[1].value) 1 else 0
                    7 -> if (subPackets[0].value == subPackets[1].value) 1 else 0
                    else -> error("Invalid type")
                }
            }
    }
}

class Part16B : Part16A() {
    override fun compute(): Long {
        return packet.value
    }

    override fun getExampleAnswer(): Int {
        return 15
    }

    override fun tests(): Sequence<Test> {
        return sequence {
            yield(Test("C200B40A82", 3, "sum of 1 + 2"))
            yield(Test("04005AC33890", 54, "product of 6 * 9"))
            yield(Test("880086C3E88112", 7, "min of 7, 8, and 9"))
            yield(Test("CE00C43D881120", 9, "maximum of 7, 8, and 9"))
            yield(Test("D8005AC2A8F0", 1, "produces 1, because 5 is less than 15"))
            yield(Test("F600BC2D8F", 0, "produces 0, because 5 is not greater than 15"))
            yield(Test("9C005AC2F8F0", 0, "produces 0, because 5 is not equal to 15"))
            yield(Test("9C0141080250320F1802104A08", 1, "produces 1, because 1 + 3 = 2 * 2"))
        }
    }
}

fun main() {
    Day(2021, 16, Part16A(), Part16B())
}