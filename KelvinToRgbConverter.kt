import kotlin.math.*

object KelvinToRgb {
    private const val MAX_RGB_VALUE = 255.0
    private const val MIN_RGB_VALUE = 0.0
    private const val KELVIN_SCALE_FACTOR = 100.0
    private const val RED_GREEN_THRESHOLD = 66.0
    private const val BLUE_THRESHOLD = 19.0
    private const val TEMPERATURE_OFFSET = 60.0
    private const val BLUE_OFFSET = 10.0

    private object GreenCalculation {
        const val MULTIPLIER = 99.4708025861
        const val OFFSET = 161.1195681661
    }

    private object BlueCalculation {
        const val MULTIPLIER = 138.5177312231
        const val OFFSET = 305.0447927307
    }

    private object RedCalculation {
        const val MULTIPLIER = 329.698727446
        const val EXPONENT = -0.1332047592
    }

    private object GreenHighTempCalculation {
        const val MULTIPLIER = 288.1221695283
        const val EXPONENT = -0.0755148492
    }

    fun Float.toRgb() = convertKelvinToRgb(this)

    fun convertKelvinToRgb(kelvin: Float): Triple<Float, Float, Float> {
        val temp = kelvin / KELVIN_SCALE_FACTOR

        val (red, green, blue) = when {
            temp <= RED_GREEN_THRESHOLD -> calculateLowTemp(temp)
            else -> calculateHighTemp(temp)
        }

        return Triple(
            clamp(red, MIN_RGB_VALUE, MAX_RGB_VALUE),
            clamp(green, MIN_RGB_VALUE, MAX_RGB_VALUE),
            clamp(blue, MIN_RGB_VALUE, MAX_RGB_VALUE)
        )
    }

    private fun calculateLowTemp(temp: Double): Triple<Double, Double, Double> {
        val red = MAX_RGB_VALUE
        val green = (GreenCalculation.MULTIPLIER * ln(temp)) - GreenCalculation.OFFSET
        val blue = if (temp <= BLUE_THRESHOLD) {
            MIN_RGB_VALUE
        } else {
            (BlueCalculation.MULTIPLIER * ln(temp - BLUE_OFFSET)) - BlueCalculation.OFFSET
        }
        return Triple(red, green, blue)
    }

    private fun calculateHighTemp(temp: Double): Triple<Double, Double, Double> {
        val adjustedTemp = temp - TEMPERATURE_OFFSET
        val red = RedCalculation.MULTIPLIER * adjustedTemp.pow(RedCalculation.EXPONENT)
        val green = GreenHighTempCalculation.MULTIPLIER * adjustedTemp.pow(GreenHighTempCalculation.EXPONENT)
        val blue = MAX_RGB_VALUE
        return Triple(red, green, blue)
    }

    private fun clamp(value: Double, min: Double, max: Double): Float {
        return when {
            value < min -> min.toFloat()
            value > max -> max.toFloat()
            else -> value.toFloat()
        }
    }
}
