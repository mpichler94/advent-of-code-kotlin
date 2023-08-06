package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.*

open class Part19A : PartSolution() {
    private lateinit var scanners: List<Scanner>
    lateinit var positions: MutableList<Vector3i>

    override fun parseInput(text: String) {
        val scanners = mutableListOf<Scanner>()
        var scanner = mutableListOf<Vector3i>()

        for (line in text.split("\n")) {
            if (line == "") {
                continue
            }
            if (line.startsWith("--- scanner")) {
                if (scanner.isNotEmpty()) {
                    scanners.add(Scanner(scanner))
                }
                scanner = mutableListOf()
            } else {
                val coords = line.split(",")
                scanner.add(Vector3i(coords[0].toInt(), coords[1].toInt(), coords[2].toInt()))
            }
        }
        if (scanner.isNotEmpty()) {
            scanners.add(Scanner(scanner))
        }

        this.scanners = scanners
    }

    override fun compute(): Int {
        val matched = mutableListOf(0)
        positions = MutableList(scanners.size) { Vector3i() }

        var i = 1
        val scanner0 = scanners.first()
        while (matched.size < scanners.size) {
            if (i in matched) {
                i = (i + 1) % scanners.size
                continue
            }
            if (scanner0.isOverlapping(scanners[i])) {
                val pos = scanner0.integrateFrom(scanners[i])
                positions.add(pos)
                matched.add(i)
            }

            i = (i + 1) % scanners.size
        }
        return scanner0.numBeacons
    }

    override fun getExampleAnswer(): Int {
        return 79
    }

    override fun getExampleInput(): String? {
        return """
            --- scanner 0 ---
            404,-588,-901
            528,-643,409
            -838,591,734
            390,-675,-793
            -537,-823,-458
            -485,-357,347
            -345,-311,381
            -661,-816,-575
            -876,649,763
            -618,-824,-621
            553,345,-567
            474,580,667
            -447,-329,318
            -584,868,-557
            544,-627,-890
            564,392,-477
            455,729,728
            -892,524,684
            -689,845,-530
            423,-701,434
            7,-33,-71
            630,319,-379
            443,580,662
            -789,900,-551
            459,-707,401

            --- scanner 1 ---
            686,422,578
            605,423,415
            515,917,-361
            -336,658,858
            95,138,22
            -476,619,847
            -340,-569,-846
            567,-361,727
            -460,603,-452
            669,-402,600
            729,430,532
            -500,-761,534
            -322,571,750
            -466,-666,-811
            -429,-592,574
            -355,545,-477
            703,-491,-529
            -328,-685,520
            413,935,-424
            -391,539,-444
            586,-435,557
            -364,-763,-893
            807,-499,-711
            755,-354,-619
            553,889,-390

            --- scanner 2 ---
            649,640,665
            682,-795,504
            -784,533,-524
            -644,584,-595
            -588,-843,648
            -30,6,44
            -674,560,763
            500,723,-460
            609,671,-379
            -555,-800,653
            -675,-892,-343
            697,-426,-610
            578,704,681
            493,664,-388
            -671,-858,530
            -667,343,800
            571,-461,-707
            -138,-166,112
            -889,563,-600
            646,-828,498
            640,759,510
            -630,509,768
            -681,-892,-333
            673,-379,-804
            -742,-814,-386
            577,-820,562

            --- scanner 3 ---
            -589,542,597
            605,-692,669
            -500,565,-823
            -660,373,557
            -458,-679,-417
            -488,449,543
            -626,468,-788
            338,-750,-386
            528,-832,-391
            562,-778,733
            -938,-730,414
            543,643,-506
            -524,371,-870
            407,773,750
            -104,29,83
            378,-903,-323
            -778,-728,485
            426,699,580
            -438,-605,-362
            -469,-447,-387
            509,732,623
            647,635,-688
            -868,-804,481
            614,-800,639
            595,780,-596

            --- scanner 4 ---
            727,592,562
            -293,-554,779
            441,611,-461
            -714,465,-776
            -743,427,-804
            -660,-479,-426
            832,-632,460
            927,-485,-438
            408,393,-506
            466,436,-512
            110,16,151
            -258,-428,682
            -393,719,612
            -211,-452,876
            808,-476,-593
            -575,615,604
            -485,667,467
            -680,325,-822
            -627,-443,-432
            872,-547,-609
            833,512,582
            807,604,487
            839,-516,451
            891,-625,532
            -652,-548,-490
            30,-46,-14
        """.trimIndent()
    }

    data class Scanner(val initialBeacons: List<Vector3i>) {
        private var beacons = initialBeacons.toList()
        val numBeacons get() = beacons.size
        private val interBeaconDistances: List<List<Int>>
            get() {
                return beacons.map { outer -> beacons.filter { it != outer }.map { (it - outer).norm(Order.L1) } }
            }

        fun isOverlapping(other: Scanner): Boolean {
            return interBeaconDistances.count { beacon ->
                other.interBeaconDistances.any { otherBeacon -> beacon.count { it in otherBeacon } >= 11 } } >= 12
        }

        private fun rotatedBeacons(rotation: Int): List<Vector3i> {
            return when (rotation) {
                0 -> beacons
                1 -> beacons.map { Vector3i(it.x, -it.z, it.y) }
                2 -> beacons.map { Vector3i(it.x, -it.y, -it.z) }
                3 -> beacons.map { Vector3i(it.x, it.z, -it.y) }
                4 -> beacons.map { Vector3i(-it.x, -it.y, it.z) }
                5 -> beacons.map { Vector3i(-it.x, -it.z, -it.y) }
                6 -> beacons.map { Vector3i(-it.x, it.y, -it.z) }
                7 -> beacons.map { Vector3i(-it.x, it.z, it.y) }
                8 -> beacons.map { Vector3i(-it.y, it.z, -it.x) }
                9 -> beacons.map { Vector3i(it.y, it.x, -it.z) }
                10 -> beacons.map { Vector3i(it.y, it.z, it.x) }
                11 -> beacons.map { Vector3i(-it.y, it.x, it.z) }
                12 -> beacons.map { Vector3i(it.y, -it.z, -it.x) }
                13 -> beacons.map { Vector3i(it.y, -it.x, it.z) }
                14 -> beacons.map { Vector3i(-it.y, -it.z, it.x) }
                15 -> beacons.map { Vector3i(-it.y, -it.x, -it.z) }
                16 -> beacons.map { Vector3i(-it.z, -it.x, it.y) }
                17 -> beacons.map { Vector3i(it.z, -it.y, it.x) }
                18 -> beacons.map { Vector3i(it.z, it.x, it.y) }
                19 -> beacons.map { Vector3i(-it.z, it.y, it.x) }
                20 -> beacons.map { Vector3i(it.z, it.y, -it.x) }
                21 -> beacons.map { Vector3i(-it.z, it.x, -it.y) }
                22 -> beacons.map { Vector3i(-it.z, -it.y, -it.x) }
                23 -> beacons.map { Vector3i(it.z, -it.x, -it.y) }

                else -> error("Invalid rotation")
            }
        }

        private fun isMatching(otherBeacons: List<Vector3i>): Vector3i? {
            return beacons.flatMap { beacon -> otherBeacons.map { it - beacon } }.groupingBy { it }.eachCount()
                .filter { it.value >= 12 }.keys.firstOrNull()
        }

        fun integrateFrom(other: Scanner): Vector3i {
            for (r in 0..23) {
                val otherBeacons = other.rotatedBeacons(r)
                val distance = isMatching(otherBeacons)
                if (distance != null) {
                    beacons = (beacons + otherBeacons.map { it - distance }).toSet().toList()
                    return distance
                }
            }

            error("Cannot align scanners")
        }
    }
}

class Part19B : Part19A() {
    override fun compute(): Int {
        super.compute()
        return positions.combinations().maxOf { (it.first - it.second).norm(Order.L1) }
    }

    override fun getExampleAnswer(): Int {
        return 3621
    }
}

fun main() {
    Day(2021, 19, Part19A(), Part19B())
}