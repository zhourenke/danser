package me.wieku.framework.audio

import jouvieje.bass.Bass.*
import jouvieje.bass.defines.BASS_ATTRIB
import jouvieje.bass.defines.BASS_ATTRIB.BASS_ATTRIB_VOL
import jouvieje.bass.defines.BASS_DATA.BASS_DATA_FFT1024
import jouvieje.bass.defines.BASS_FX
import jouvieje.bass.defines.BASS_POS.BASS_POS_BYTE
import jouvieje.bass.defines.BASS_STREAM
import me.wieku.framework.resource.FileHandle
import me.wieku.framework.resource.FileType
import org.lwjgl.BufferUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Track(file: FileHandle) {
    private var channelStream = when (file.fileType) {
        FileType.Classpath -> {
            val buffer = file.toBuffer()
            BASS_StreamCreateFile(
                true,
                buffer,
                0,
                buffer.limit().toLong(),
                BASS_STREAM.BASS_STREAM_DECODE or BASS_STREAM.BASS_STREAM_PRESCAN
            )
        }
        FileType.Local, FileType.Absolute -> {
            BASS_StreamCreateFile(
                false,
                file.absolutePath(),
                0,
                0,
                BASS_STREAM.BASS_STREAM_DECODE or BASS_STREAM.BASS_STREAM_PRESCAN
            )
        }
    }

    private var fxChannel = BASS_FX_TempoCreate(channelStream.asInt(), BASS_FX.BASS_FX_FREESOURCE)

    private var dataBuffer = BufferUtils.createByteBuffer(512*4)

    var fftData = FloatArray(512)
        private set

    var peak: Float = 0.0f
        private set

    var beat: Float = 0.0f
        private set

    var leftChannelLevel: Float = 0.0f
        private set

    var rightChannelLevel: Float = 0.0f
        private set

    fun play() {
        setVolume(/*settings.Audio.GeneralVolume * settings.Audio.MusicVolume*/0.5f)
        BASS_ChannelPlay(fxChannel.asInt(), true)
    }

    fun playV(volume: Float) {
        setVolume(volume)
        BASS_ChannelPlay(fxChannel.asInt(), false)
    }

    fun pause() {
        BASS_ChannelPause(fxChannel.asInt())
    }

    fun resume() {
        BASS_ChannelPlay(fxChannel.asInt(), false)
    }

    fun stop() {
        BASS_ChannelStop(fxChannel.asInt())
    }

    fun setVolume(vol: Float) {
        BASS_ChannelSetAttribute(fxChannel.asInt(), BASS_ATTRIB.BASS_ATTRIB_VOL, vol)
    }

    fun setVolumeRelative(vol: Float) {
        BASS_ChannelSetAttribute(
            fxChannel.asInt(),
            BASS_ATTRIB_VOL, /*settings.Audio.GeneralVolume*settings.Audio.MusicVolume**/
            vol
        )
    }

    fun getLength(): Float {
        return BASS_ChannelBytes2Seconds(
            fxChannel.asInt(),
            BASS_ChannelGetLength(fxChannel.asInt(), BASS_POS_BYTE)
        ).toFloat()
    }

    fun setPosition(pos: Float) {
        BASS_ChannelSetPosition(
            fxChannel.asInt(),
            BASS_ChannelSeconds2Bytes(fxChannel.asInt(), pos.toDouble()),
            BASS_POS_BYTE
        )
    }

    fun getPosition(): Float {
        return BASS_ChannelBytes2Seconds(
            fxChannel.asInt(),
            BASS_ChannelGetPosition(fxChannel.asInt(), BASS_POS_BYTE)
        ).toFloat()
    }

    fun setTempo(tempo: Float) {
        BASS_ChannelSetAttribute(
            fxChannel.asInt(),
            jouvieje.bass.enumerations.BASS_ATTRIB.BASS_ATTRIB_TEMPO.asInt(),
            (tempo - 1.0f) * 100
        )
    }

    fun setPitch(tempo: Float) {
        BASS_ChannelSetAttribute(
            fxChannel.asInt(),
            jouvieje.bass.enumerations.BASS_ATTRIB.BASS_ATTRIB_TEMPO_PITCH.asInt(),
            (tempo - 1.0f) * 12
        )
    }

    /*fun getState(): Int {
        return BASS_ChannelIsActive(fxChannel.asInt())
    }*/

    fun update() {
        BASS_ChannelGetData(fxChannel.asInt(), dataBuffer, BASS_DATA_FFT1024)

        dataBuffer.asFloatBuffer().get(fftData)

        var toPeak = -1.0f
        var beatAv = 0.0f

        for ((i, v) in fftData.withIndex()) {
            if (toPeak < v) {
                toPeak = v
            }
            if (i in 1..4) {
                beatAv = Math.max(beatAv, v)
            }
        }

        beat = beatAv
        peak = toPeak

        val level = BASS_ChannelGetLevel(fxChannel.asInt())

        leftChannelLevel = (level and 65535).toFloat() / 32768
        rightChannelLevel = (level shr 16).toFloat() / 32768
    }

    fun getLevelCombined(): Float {
        return (leftChannelLevel + rightChannelLevel) / 2
    }

}