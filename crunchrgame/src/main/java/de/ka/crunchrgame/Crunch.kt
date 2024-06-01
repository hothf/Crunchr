package de.ka.crunchrgame

class Crunch private constructor(val seed: String) {
    var expectedResult: Float = 0f
    var display: String = ""

    init {
        expectedResult = (6 + 7).toFloat()
        display = "6+7"
    }


    fun solve(input: Float): Boolean {
        return true
    }

    companion object {

        fun createNew(): Crunch {
            return Crunch("1234")
        }

        fun createFromSeed(seed: String?): Crunch? {
            seed ?: return null
            return Crunch(seed)
        }
    }

}