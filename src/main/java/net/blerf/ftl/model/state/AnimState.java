package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnimState {
    private boolean playing = false;
    private boolean looping = false;
    /**
     * Sets the current frame of this anim (0-based).
     * <p>
     * Start/end frames during playback vary. Anims, and their important
     * frames, are defined in "animations.xml".
     * <p>
     * FTL seems to clobber this value upon loading, based on the
     * circumstances driving the anim, so editing it is probably useless.
     */
    private int currentFrame = 0;
    /**
     * Sets time elapsed while playing this anim.
     * <p>
     * Technically this doesn't count, so much as remember how far into the
     * anim playback was when the current frame appeared.
     * <p>
     * This value is 1000 / (animSheet's frame count) * (currentFrame).
     * Sometimes that's off by 1 due to rounding somewhere.
     * <p>
     * TODO: That formula matched WeaponModuleState's weaponAnim, at least.
     * <p>
     * FTL seems to clobber this value upon loading, based on the
     * circumstances driving the anim, so editing it is probably useless.
     */
    private int progressTicks = 0;
    /**
     * Scale factor - a pseudo-float (1000 is 1.0)
     * Projectiles with flightAnimId "debris_small" set their deathAnim scale to 250.
     */
    private int scale = 1000;
    /**
     * Unknown.
     * <p>
     * Observed values: 0 (when playing), -1000 (when not playing).
     * One time, a missile exploded whose deathAnim had -32000.
     */
    private int x = -1000;
    private int y = -1000;

    /**
     * Copy constructor.
     */
    public AnimState(AnimState srcAnim) {
        playing = srcAnim.isPlaying();
        looping = srcAnim.isLooping();
        currentFrame = srcAnim.getCurrentFrame();
        progressTicks = srcAnim.getProgressTicks();
        scale = srcAnim.getScale();
        x = srcAnim.getX();
        y = srcAnim.getY();
    }

    @Override
    public String toString() {
        return String.format("Playing:           %7b%n", playing) +
                String.format("Looping?:          %7b%n", looping) +
                String.format("Current Frame:     %7d%n", currentFrame) +
                String.format("Progress Ticks:    %7d%n", progressTicks) +
                String.format("Scale:             %7d (%5.03f)%n", scale, scale / 1000f) +
                String.format("X,Y?:                %5d,%5d%n", x, y);
    }
}
