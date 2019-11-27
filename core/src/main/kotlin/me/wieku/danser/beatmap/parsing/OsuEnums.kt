package me.wieku.danser.beatmap.parsing

import me.wieku.framework.utils.EnumWithId
import org.joml.Vector2f

enum class Section(override val enumId: Int, val separator: String): EnumWithId {
    Unknown(0, ":"),
    General(1 shl 0, ":"),
    Colours(1 shl 1, ","),
    Editor(1 shl 2, ":"),
    Metadata(1 shl 3, ":"),
    TimingPoints(1 shl 4, ","),
    Events(1 shl 5, ","),
    HitObjects(1 shl 6, ","),
    Difficulty(1 shl 7, ":"),
    Variables(1 shl 8, ":");

    companion object: EnumWithId.Companion<Section>(Unknown)
}

enum class Events(override val enumId: Int): EnumWithId {
    Background(0),
    Video(1),
    Break(2),
    Colour(3),
    Sprite(4),
    Sample(5),
    Animation(6);

    companion object: EnumWithId.Companion<Events>(Background)
}

enum class Origins(override val enumId: Int, val offset: Vector2f): EnumWithId {
    TopLeft(0, Vector2f(-1f, -1f)),
    Centre(1, Vector2f(0f, 0f)),
    CentreLeft(2, Vector2f(-1f, 0f)),
    TopRight(3, Vector2f(1f, -1f)),
    BottomCentre(4, Vector2f(0f, 1f)),
    TopCentre(5, Vector2f(0f, -1f)),
    Custom(6, Vector2f(0f, 0f)),
    CentreRight(7, Vector2f(1f, 0f)),
    BottomLeft(8, Vector2f(-1f, 1f)),
    BottomRight(9, Vector2f(1f, 1f));

    companion object: EnumWithId.Companion<Origins>(Centre)
}

enum class StoryLayer(override val enumId: Int): EnumWithId {
    Background(0),
    Fail(1),
    Pass(2),
    Foreground(3);

    companion object: EnumWithId.Companion<StoryLayer>(Background)
}