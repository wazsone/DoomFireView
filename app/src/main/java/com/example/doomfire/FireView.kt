package com.example.doomfire

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View



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

    private val firePixels = Array(height) {
        IntArray(width) { 0 }
    }.toMutableList()

    private val paint = Paint()

    private lateinit var bitmap: Bitmap

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        (0..w).forEach { x ->
            firePixels[x][h - 1] = firePalette.size - 1
        }
    }

    override fun onDraw(canvas: Canvas?) {
        drawFire(canvas);
    }

    private fun drawFire(canvas: Canvas?) {
        for (x in 0..width) {
            for (y in 0..height) {
                val temperature = when {
                    firePixels[x][y] < 0 -> 0
                    firePixels[x][y] >= firePalette.size -> firePalette.size - 1
                    else -> firePixels[x][y]
                }
                bitmap.setPixel(x, y, firePalette[temperature].toInt())
                canvas?.drawBitmap(bitmap, 0F, 0F, paint)
            }
        }
    }

}