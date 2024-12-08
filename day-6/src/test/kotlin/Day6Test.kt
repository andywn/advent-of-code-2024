import uk.andrewnorman.Direction
import uk.andrewnorman.Guard
import uk.andrewnorman.GuardedArea
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {

    @Test
    fun test() {
        val lines = listOf(
            "....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "..........",
            ".#..^.....",
            "........#.",
            "#.........",
            "......#..."
        )
        val area = GuardedArea(lines)
        val guard = Guard(area, lines)

        assertEquals(4, guard.guardLocation.x)
        assertEquals(6, guard.guardLocation.y)
        assertEquals(Direction.UP, guard.guardLocation.direction)

        var count = 0
        while (!guard.move().outsideOfMap) {
            count++
        }
        println(count)
        assertEquals(41, guard.getCount())
    }

    @Test
    fun testChallenge2() {
        val lines = listOf(
            "....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "..........",
            ".#..^.....",
            "........#.",
            "#.........",
            "......#..."
        )
        val area = GuardedArea(lines)
        val guard = Guard(area, lines)

        while (!guard.move().outsideOfMap) {
        }
        assertEquals(41, guard.getCount())
        assertEquals(6, guard.getObstructions())
    }

}