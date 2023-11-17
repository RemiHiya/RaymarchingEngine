package ui.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.DragListener

open class Container : Table() {
    private var resizeLeft = false
    private var resizeRight = false
    private var resizeTop = false
    private var resizeBottom = false

    init {
        setResizeListeners()
    }

    private fun setResizeListeners() {
        addListener(object : DragListener() {
            override fun dragStart(event: InputEvent, x: Float, y: Float, pointer: Int) {
                determineResizeDirection(x, y)
                updateCursor()
            }

            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                resizeContainer(x, y)
            }

            override fun dragStop(event: InputEvent, x: Float, y: Float, pointer: Int) {
                clearResizeDirection()
                restoreDefaultCursor()
            }
        })
    }

    private fun determineResizeDirection(x: Float, y: Float) {
        val left = x < width * 0.25
        val right = x > width * 0.75
        val top = y > height * 0.75
        val bottom = y < height * 0.25

        resizeLeft = left
        resizeRight = right
        resizeTop = top
        resizeBottom = bottom
    }

    private fun resizeContainer(x: Float, y: Float) {
        val deltaX = if (resizeLeft) -x else if (resizeRight) x else 0f
        val deltaY = if (resizeTop) y else if (resizeBottom) -y else 0f

        if (resizeLeft || resizeRight) {
            width += deltaX
        }

        if (resizeTop || resizeBottom) {
            height += deltaY
        }
    }

    private fun updateCursor() {
        /*if (resizeLeft && resizeTop) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.NW_RESIZE)
        } else if (resizeLeft && resizeBottom) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.SW_RESIZE)
        } else if (resizeRight && resizeTop) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.NE_RESIZE)
        } else if (resizeRight && resizeBottom) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.SE_RESIZE)
        } else if (resizeLeft) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.W_RESIZE)
        } else if (resizeRight) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.E_RESIZE)
        } else if (resizeTop) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.N_RESIZE)
        } else if (resizeBottom) {
            Gdx.graphics.setCursor(Cursor.SystemCursor.S_RESIZE)
        }*/
    }

    private fun clearResizeDirection() {
        resizeLeft = false
        resizeRight = false
        resizeTop = false
        resizeBottom = false
    }

    private fun restoreDefaultCursor() {
        //Gdx.graphics.setCursor(Cursor.SystemCursor.Arrow)
    }
}
