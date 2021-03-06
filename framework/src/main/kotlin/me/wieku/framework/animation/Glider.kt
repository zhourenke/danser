package me.wieku.framework.animation

import me.wieku.framework.math.Easing
import java.util.*

class Glider(var value: Float) {
    private val eventQueue = ArrayDeque<GliderEvent>()

    private var lastTime = 0f

    fun update(time: Float) {
        lastTime = time

        while (eventQueue.isNotEmpty()) {
            val event = eventQueue.peekFirst()
            if (time < event.startTime) break

            value = event.startValue + event.easing.func((time-event.startTime)/(event.endTime-event.startTime)) * (event.endValue - event.startValue)

            if (time >= event.endTime) {
                eventQueue.pop()
            } else break
        }

    }

    fun addEvent(startTime: Float, endTime: Float, startValue: Float, endValue: Float, easing: Easing = Easing.Linear) {
        eventQueue.add(GliderEvent(startTime, endTime, startValue, endValue, easing))
    }

    fun addEvent(endTime: Float, endValue: Float, easing: Easing = Easing.Linear) {
        eventQueue.clear()
        eventQueue.add(GliderEvent(lastTime, endTime, value, endValue, easing))
    }

    fun reset() {
        eventQueue.clear()
    }

}