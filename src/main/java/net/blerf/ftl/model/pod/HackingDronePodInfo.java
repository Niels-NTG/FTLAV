package net.blerf.ftl.model.pod;

import net.blerf.ftl.model.state.AnimState;

public class HackingDronePodInfo extends ExtendedDronePodInfo {
    private int attachPositionX = 0;
    private int attachPositionY = 0;
    private int unknownGamma = 0;
    private int unknownDelta = 0;
    private AnimState landingAnim = new AnimState();
    private AnimState extensionAnim = new AnimState();

    /**
     * Constructor.
     */
    public HackingDronePodInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected HackingDronePodInfo(HackingDronePodInfo srcInfo) {
        super(srcInfo);
        attachPositionX = srcInfo.getAttachPositionX();
        attachPositionY = srcInfo.getAttachPositionY();
        unknownGamma = srcInfo.getUnknownGamma();
        unknownDelta = srcInfo.getUnknownDelta();
        landingAnim = new AnimState(srcInfo.getLandingAnim());
        extensionAnim = new AnimState(srcInfo.getExtensionAnim());
    }

    @Override
    public HackingDronePodInfo copy() {
        return new HackingDronePodInfo(this);
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    @Override
    public void commandeer() {
        setAttachPositionX(0);
        setAttachPositionY(0);
        setUnknownGamma(0);
        setUnknownDelta(0);

        getLandingAnim().setPlaying(false);
        getLandingAnim().setCurrentFrame(0);
        getLandingAnim().setProgressTicks(0);

        getExtensionAnim().setPlaying(false);
        getExtensionAnim().setCurrentFrame(0);
        getExtensionAnim().setProgressTicks(0);
    }

    //Alpha and beta might be xy of the center of the claw on the wall.

    /**
     * Sets the position of this drone pod's attachment to a ship wall.
     * <p>
     * This is the center of the claw-side edge of the drone sprite, at the
     * senter of the wall of the room it attached to. This point might not
     * be ON the wall, possibly be a few pixels outside or inside.
     * <p>
     * This is set when the done pod makes contact with the wall.
     * <p>
     * When not set, this is (0, 0).
     */
    public void setAttachPositionX(int n) {
        attachPositionX = n;
    }

    public void setAttachPositionY(int n) {
        attachPositionY = n;
    }

    public int getAttachPositionX() {
        return attachPositionX;
    }

    public int getAttachPositionY() {
        return attachPositionY;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 0 (in flight), 1 (on contact).
     */
    public void setUnknownGamma(int n) {
        unknownGamma = n;
    }

    public int getUnknownGamma() {
        return unknownGamma;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 0 (in flight), 1 (after extension anim completes and
     * purple lights turn on). Stayed at 1 after the hacking system was
     * damaged and inoperative while the pod was still attached (purple
     * lights turned off).
     */
    public void setUnknownDelta(int n) {
        unknownDelta = n;
    }

    public int getUnknownDelta() {
        return unknownDelta;
    }

    /**
     * Sets the attachment/grappling anim.
     * <p>
     * This begins playing on contact.
     */
    public void setLandingAnim(AnimState anim) {
        landingAnim = anim;
    }

    public AnimState getLandingAnim() {
        return landingAnim;
    }

    /**
     * Sets the antenna extension anim.
     * <p>
     * This begins playing after the landing anim completes.
     */
    public void setExtensionAnim(AnimState anim) {
        extensionAnim = anim;
    }

    public AnimState getExtensionAnim() {
        return extensionAnim;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Attach Position: %7s,%7s%n", attachPositionX, attachPositionY));
        result.append(String.format("Gamma?:              %7d%n", unknownGamma));
        result.append(String.format("Delta?:              %7d%n", unknownDelta));

        result.append(String.format("\nLanding Anim?...%n"));
        if (landingAnim != null) {
            result.append(landingAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append(String.format("\nExtension Anim?...%n"));
        if (extensionAnim != null) {
            result.append(extensionAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
