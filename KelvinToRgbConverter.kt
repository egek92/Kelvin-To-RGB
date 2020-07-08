fun convertKelvinToRgb(kelvin: Float): Triple<Float, Float, Float> {

        val temp = (kelvin / 100.0)

        var red: Double?
        var green: Double?
        var blue: Double?

        if (temp <= 66.0) {
            red = 255.0
            green = temp
            green = 99.4708025861 * ln(green) - 161.1195681661


            if (temp <= 19.0) {
                blue = 0.0
            } else {
                blue = temp - 10.0
                blue = 138.5177312231 * ln(blue) - 305.0447927307
            }
        } else {
            red = temp - 60.0
            red = 329.698727446 *  red.pow(-0.1332047592)
            green = temp - 60.0
            green = 288.1221695283 * green.pow(-0.0755148492)
            blue = 255.0
        }


        return Triple(clamp(red, 0.0, 255.0), clamp(green, 0.0, 255.0), clamp(blue, 0.0, 255.0))

    }

    private fun clamp(value: Double, min: Double, max: Double): Float {
        if (value < min) return min.toFloat()

        if (value > max) return max.toFloat()

        return value.toFloat()
    }
