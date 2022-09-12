package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val painter = Paint().apply{
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.default_text_size)
    }
    var buttonText = "DOWNLOAD"
    private var dwnldProg = 0
    private var valueAnimator = ValueAnimator()

    private var textEnabled = 0
    private var textDisabled = 0
    private var backgroundEnabled = 0
    private var backgroundDisabled = 0

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading->{
                buttonText = "DOWNLOADING"
            valueAnimator = ValueAnimator.ofInt(0,100).apply{
                duration=700
                addUpdateListener { valueAnimator ->
                    dwnldProg = animatedValue as Int
                    valueAnimator.repeatCount = ValueAnimator.INFINITE
                    valueAnimator.repeatMode = ValueAnimator.REVERSE
                    invalidate()
                }
                start()
            }
            }
            ButtonState.Completed -> {
                buttonText = "DOWNLOADED"
                valueAnimator.end()
                dwnldProg=100
                invalidate()
            }
            ButtonState.Clicked -> {
                buttonText = "DOWNLOAD"
                dwnldProg=0
                valueAnimator.start()
                invalidate()
            }
        }
    }
    var buttonEnabled = false


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            textEnabled = getColor(R.styleable.LoadingButton_textColorEnabled,0)
            textDisabled = getColor(R.styleable.LoadingButton_textColorDisabled, 0)
            backgroundEnabled = getColor(R.styleable.LoadingButton_backgroundColorEnabled, 0)
            backgroundDisabled = getColor(R.styleable.LoadingButton_backgroundColorDisabled, 0)
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(when(buttonEnabled){
         true -> backgroundEnabled
         false -> backgroundDisabled
        })

        painter.color = resources.getColor(R.color.colorPrimaryDark)
        widthSize = width
        heightSize = height
        canvas.drawRect(0f,0f,(widthSize.toFloat()*dwnldProg/100), heightSize.toFloat(), painter)

        when(buttonEnabled){
            true -> painter.color = textEnabled
            false -> painter.color = textDisabled
        }
//        painter.color = when(buttonEnabled){
//            true -> textEnabled
//            false -> textDisabled
//        }
        painter.textAlign = Paint.Align.CENTER
        val xPos = (width/2).toFloat()
        val yPos = ((height /2)-((painter.descent()+painter.ascent())/2)).toFloat()
        canvas.drawText(buttonText,xPos,yPos,painter)

        painter.color = resources.getColor(R.color.colorAccent)
        canvas.drawArc(width-((height*2)-(height*.3)).toFloat(),height*.2.toFloat(),width-height.toFloat(),height*.8.toFloat(),0f,dwnldProg*360/100.toFloat(),true,painter)

    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
//        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
//        val h: Int = resolveSizeAndState(
//            MeasureSpec.getSize(w),
//            heightMeasureSpec,
//            0
//        )
//        widthSize = w
//        heightSize = h
//        setMeasuredDimension(w, h)
//    }

}