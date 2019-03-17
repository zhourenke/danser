package me.wieku.framework.graphics.textures

class TextureRegion(
    private var baseTexture: Texture,
    private var U1: Float,
    private var U2: Float,
    private var V1: Float,
    private var V2: Float,
    private var layer: Int
) {


    fun getWidth(): Float = (U2 - U1) * baseTexture.width

    fun getHeight(): Float = (V2 - V1) * baseTexture.height

    fun getArea(): Float = getWidth() * getHeight()

    fun getTexture() = baseTexture
    fun getU1() = U1
    fun getU2() = U2
    fun getV1() = V1
    fun getV2() = U2
    fun getLayer() = layer
}