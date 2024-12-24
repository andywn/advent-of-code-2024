package uk.andrewnorman

class PlaneRange: ArrayList<Coords> {
    constructor(lengthX: Int, lengthY: Int): super(lengthX * lengthY) {
        this.addAll(
            IntRange(0, lengthX - 1).flatMap { x -> IntRange(0, lengthY - 1).map { y -> Coords(x, y) } }.toList()
        )
    }
}