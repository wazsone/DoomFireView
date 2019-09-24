package com.example.doomfire

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.max
import kotlin.math.min

class FireView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val firePalette = arrayOf(
        0xff070707,
        0xff1F0707,
        0xff2F0F07,
        0xff470F07,
        0xff571707,
        0xff671F07,
        0xff771F07,
        0xff8F2707,
        0xff9F2F07,
        0xffAF3F07,
        0xffBF4707,
        0xffC74707,
        0xffDF4F07,
        0xffDF5707,
        0xffDF5707,
        0xffD75F07,
        0xffD75F07,
        0xffD7670F,
        0xffCF6F0F,
        0xffCF770F,
        0xffCF7F0F,
        0xffCF8717,
        0xffC78717,
        0xffC78F17,
        0xffC7971F,
        0xffBF9F1F,
        0xffBF9F1F,
        0xffBFA727,
        0xffBFA727,
        0xffBFAF2F,
        0xffB7AF2F,
        0xffB7B72F,
        0xffB7B737,
        0xffCFCF6F,
        0xffDFDF9F,
        0xffEFEFC7,
        0xffFFFFFF
    )

    private lateinit var firePixels: Array<IntArray>

    private val paint = Paint()

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapPixels: IntArray

    private val random = Random()

    private var fireHeight: Int = 0
    private var fireWidth: Int = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val aspectRatio = w.toFloat() / h
        fireWidth = 250
        fireHeight = (fireWidth / aspectRatio).toInt()

        bitmapPixels = IntArray(fireWidth * fireHeight) { 0 }
        bitmap = Bitmap.createBitmap(fireWidth, fireHeight, Bitmap.Config.RGB_565)
        firePixels = Array(fireWidth) { IntArray(fireHeight) { 0 } }
        (0 until fireWidth).forEach { x ->
            firePixels[x][fireHeight - 1] = firePalette.size - 1
        }
    }

    override fun onDraw(canvas: Canvas?) {
        spreadFire()
        drawFire(canvas)
        invalidate()
    }

    private fun spreadFire() {
        for (y in 0 until fireHeight - 1) {
            for (x in 0 until fireWidth) {
                val rand_x = random.nextInt(3)
                val rand_y = random.nextInt(6)
                val dst_x = min(fireWidth - 1, max(0, x + rand_x - 1))
                val dst_y = min(fireHeight - 1, y + rand_y)
                val deltaFire = rand_x and 1 // 0 or 1
                firePixels[x][y] =
                    max(0, firePixels[dst_x][dst_y] - deltaFire) // decrease temperature
            }
        }
    }

    private fun drawFire(canvas: Canvas?) {
        for (x in 0 until fireWidth) {
            for (y in 0 until fireHeight) {
                val temperature = when {
                    firePixels[x][y] < 0 -> 0
                    firePixels[x][y] >= firePalette.size -> firePalette.size - 1
                    else -> firePixels[x][y]
                }
                bitmapPixels[fireWidth * y + x] = firePalette[temperature].toInt()
            }
        }
        bitmap.setPixels(bitmapPixels, 0, fireWidth, 0, 0, fireWidth, fireHeight)
        val scale = width.toFloat() / fireWidth
        canvas?.scale(scale, scale)
        canvas?.drawBitmap(bitmap, 0F, 0F, paint)
    }

}