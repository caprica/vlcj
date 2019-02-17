package uk.co.caprica.vlcj.player.component;

/**
 * Enumeration of flags for controller input (mouse and keyboard) event handling for the video surface.
 */
public enum InputEvents {

    /**
     * No input event handling, no mouse or keyboard listener events will fire.
     */
    NONE,

    /**
     * Default input event handling, mouse and keyboard listener events will fire.
     */
    DEFAULT,

    /**
     * Disable native input event handling, mouse and keyboard listener events will fire.
     * <p>
     * This is the mode that is usually required on Windows.
     */
    DISABLE_NATIVE

}
