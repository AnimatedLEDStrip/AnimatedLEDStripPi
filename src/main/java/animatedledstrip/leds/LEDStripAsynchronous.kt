package animatedledstrip.leds

/*
 *  Copyright (c) 2019 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */


import com.diozero.ws281xj.rpiws281x.WS281x
import org.pmw.tinylog.Logger


/**
 * Class that represents a led strip.
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 */
open class LEDStripAsynchronous(var numLEDs: Int, pin: Int, private val emulated: Boolean = false) {

    /**
     * The LED Strip. Chooses between WS281x and EmulatedWS281x based on value of emulated.
     */
    var ledStrip = when (emulated) {
        true -> EmulatedWS281x(pin, 255, numLEDs)
        false -> WS281x(pin, 255, numLEDs)
    }

    init {
        Logger.info("numLEDs: $numLEDs")
        Logger.info("using GPIO pin $pin")
    }

    /**
     * Returns true if this is an emulated LED strip
     */
    fun isEmulated() = emulated


    /**
     * Sets a pixel's color with a ColorContainer.
     *
     * @param pixel The pixel to change
     * @param colorValues The color to set the pixel to
     */
    fun setPixelColor(pixel: Int, colorValues: ColorContainer) {
        ledStrip.setPixelColourRGB(pixel, colorValues.r, colorValues.g, colorValues.b)
    }


    /**
     * Set a pixel's color with r, g, b (ranges 0-255).
     *
     * @param pixel The pixel to change
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setPixelColor(pixel: Int, rIn: Int, gIn: Int, bIn: Int) {
        setPixelColor(pixel, ColorContainer(rIn, gIn, bIn))
    }


    /**
     * Set a pixel's color with a Long, such as a 24-bit integer.
     *
     * @param pixel The pixel to change
     * @param hexIn The color to set the pixel to
     */
    fun setPixelColor(pixel: Int, hexIn: Long) {
        setPixelColor(pixel, ColorContainer(hexIn))
    }

    fun setPixelRed(pixel: Int, rIn: Int) {
        ledStrip.setRedComponent(pixel, rIn)
    }

    fun setPixelGreen(pixel: Int, gIn: Int) {
        ledStrip.setGreenComponent(pixel, gIn)
    }

    fun setPixelBlue(pixel: Int, bIn: Int) {
        ledStrip.setBlueComponent(pixel, bIn)
    }


    /**
     * Loops through all pixels and sets their color to colorValues.
     *
     * @param colorValues The color to set the strip to
     */
    fun setStripColor(colorValues: ColorContainer) {
        for (i in 0 until numLEDs) setPixelColor(i, colorValues)
        show()
    }

    /**
     * Set the strip color with a Long, such as a 24-bit integer.
     *
     * @param hexIn The color to set the strip to
     */
    fun setStripColor(hexIn: Long) {
        for (i in 0 until numLEDs) setPixelColor(i, hexIn)
        show()
    }


    /**
     * Set the strip color with r, g, b (ranges 0-255).
     *
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setStripColor(rIn: Int, gIn: Int, bIn: Int) {
        for (i in 0 until numLEDs) setPixelColor(i, rIn, gIn, bIn)
        show()
    }

    /**
     * Set the color of a section of the strip. Loops through all leds between start
     * and end (inclusive) and sets their color to colorValues.
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param colorValues The color to set the section to
     */
    fun setSectionColor(start: Int, end: Int, colorValues: ColorContainer) {
        for (i in start..end) setPixelColor(i, colorValues.r, colorValues.g, colorValues.b)
        show()
    }


    /**
     * Set a section's color with a Long, such as a 24-bit integer.
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param hexIn The color to set the section to
     */
    fun setSectionColor(start: Int, end: Int, hexIn: Long) {
        for (i in start..end) setPixelColor(i, hexIn)
        show()
    }


    /**
     * Set a section's color with r, g, b (ranges 0-255).
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setSectionColor(start: Int, end: Int, rIn: Int, gIn: Int, bIn: Int) {
        for (i in start..end) ledStrip.setPixelColourRGB(i, rIn, gIn, bIn)
        show()
    }


    /**
     * Get the color of a pixel.
     *
     * @param pixel The pixel to find the color of
     * @return The color of the pixel
     */
    fun getPixelColor(pixel: Int): ColorContainer =
        ColorContainer(ledStrip.getPixelColour(pixel).toLong())


    /**
     * Get the color of a pixel as a Long.
     *
     * @param pixel The pixel to find the color of
     * @return The color of the pixel as a Long
     */
    fun getPixelLong(pixel: Int): Long {
        return getPixelColor(pixel).hex
    }


    /**
     * Get the color of a pixel as a hexadecimal string.
     *
     * @param pixel The pixel to find the color of
     * @return A string containing the color of the pixel in hexadecimal
     */
    fun getPixelHexString(pixel: Int): String {
        return getPixelLong(pixel).toString(16)
    }


    /**
     * Get the colors of all pixels as a List of Longs.
     */
    fun getPixelColorList(): List<Long> {
        val temp = mutableListOf<Long>()
        for (i in 0 until numLEDs) temp.add(getPixelLong(i))
        return temp
    }


    /**
     * Set the color of the strip using a map with each pixel index mapped to a
     * ColorContainer.
     *
     * @param palette The map of colors
     * @param offset The index of the pixel that will be set to the color at
     * index 0
     */
    fun setStripColorWithPalette(palette: Map<Int, ColorContainer>, offset: Int = 0) =
        palette.forEach { i, j ->
            setPixelColor((i + offset) % numLEDs, j)
        }


    /**
     * Sets the color of the strip with a list. The list is converted to a map
     * before that map is sent to [setStripColorWithPalette] with an offset of 0.
     *
     * @param colorList The list of colors
     */
    fun setStripColorWithGradient(colorList: List<ColorContainer>) {
        val palette = colorsFromPalette(colorList, numLEDs)
        setStripColorWithPalette(palette)
    }


    /**
     * Send data to the LEDs.
     */
    fun show() {
        ledStrip.render()
    }
}