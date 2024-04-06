package net.blerf.ftl.model.state;

import net.blerf.ftl.model.projectileinfo.ExtendedProjectileInfo;
import net.blerf.ftl.model.type.ProjectileType;

public class ProjectileState {
    private ProjectileType projectileType = ProjectileType.INVALID;
    private int currentPosX = 0, currentPosY = 0;
    private int prevPosX = 0, prevPosY = 0;
    private int speed = 0;
    private int goalPosX = 0, goalPosY = 0;
    private int heading = 0;
    private int ownerId = 0;
    private int selfId = 0;

    private DamageState damage = null;

    private int lifespan = 0;
    private int destinationSpace = 0;
    private int currentSpace = 0;
    private int targetId = 0;
    private boolean dead = false;

    private String deathAnimId = "";
    private String flightAnimId = "";

    private AnimState deathAnim = new AnimState();
    private AnimState flightAnim = new AnimState();

    private int velocityX = 0, velocityY = 0;
    private boolean missed = false;
    private boolean hitTarget = false;

    private String hitSolidSound = "";
    private String hitShieldSound = "";
    private String missSound = "";

    private int entryAngle = -1;  // Guess: X degrees CCW, where 0 is due East.
    private boolean startedDying = false;
    private boolean passedTarget = false;

    private int type = 0;
    private boolean broadcastTarget = false;

    private ExtendedProjectileInfo extendedInfo = null;


    /**
     * Constructs an incomplete ProjectileState.
     * <p>
     * It will need Damage and type-specific extended info.
     */
    public ProjectileState() {
    }

    /**
     * Copy constructor.
     * <p>
     * Each anim, Damage, and ExtendedProjectileInfo will be
     * copy-constructed as well.
     */
    public ProjectileState(ProjectileState srcProjectile) {
        projectileType = srcProjectile.getProjectileType();
        currentPosX = srcProjectile.getCurrentPositionX();
        currentPosY = srcProjectile.getCurrentPositionY();
        prevPosX = srcProjectile.getPreviousPositionX();
        prevPosY = srcProjectile.getPreviousPositionY();
        speed = srcProjectile.getSpeed();
        goalPosX = srcProjectile.getGoalPositionX();
        goalPosY = srcProjectile.getGoalPositionY();
        heading = srcProjectile.getHeading();
        ownerId = srcProjectile.getOwnerId();
        selfId = srcProjectile.getSelfId();

        damage = new DamageState(srcProjectile.getDamage());

        lifespan = srcProjectile.getLifespan();
        destinationSpace = srcProjectile.getDestinationSpace();
        currentSpace = srcProjectile.getCurrentSpace();
        targetId = srcProjectile.getTargetId();
        dead = srcProjectile.isDead();

        deathAnimId = srcProjectile.getDeathAnimId();
        flightAnimId = srcProjectile.getFlightAnimId();

        deathAnim = new AnimState(srcProjectile.getDeathAnim());
        flightAnim = new AnimState(srcProjectile.getFlightAnim());

        velocityX = srcProjectile.getVelocityX();
        velocityY = srcProjectile.getVelocityY();
        missed = srcProjectile.hasMissed();
        hitTarget = srcProjectile.hasHitTarget();

        hitSolidSound = srcProjectile.getHitSolidSound();
        hitShieldSound = srcProjectile.getHitShieldSound();
        missSound = srcProjectile.getMissSound();

        entryAngle = srcProjectile.getEntryAngle();
        startedDying = srcProjectile.hasStartedDying();
        passedTarget = srcProjectile.hasPassedTarget();

        type = srcProjectile.getType();
        broadcastTarget = srcProjectile.getBroadcastTarget();

        if (srcProjectile.getExtendedInfo(ExtendedProjectileInfo.class) != null) {
            extendedInfo = srcProjectile.getExtendedInfo(ExtendedProjectileInfo.class).copy();
        }
    }

    public void setProjectileType(ProjectileType t) {
        projectileType = t;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setCurrentPositionX(int n) {
        currentPosX = n;
    }

    public void setCurrentPositionY(int n) {
        currentPosY = n;
    }

    public int getCurrentPositionX() {
        return currentPosX;
    }

    public int getCurrentPositionY() {
        return currentPosY;
    }

    public void setPreviousPositionX(int n) {
        prevPosX = n;
    }

    public void setPreviousPositionY(int n) {
        prevPosY = n;
    }

    public int getPreviousPositionX() {
        return prevPosX;
    }

    public int getPreviousPositionY() {
        return prevPosY;
    }

    /**
     * Sets the projectile's speed.
     * <p>
     * This is a pseudo-float based on the 'speed' tag of the
     * WeaponBlueprint's xml.
     */
    public void setSpeed(int n) {
        speed = n;
    }

    public int getSpeed() {
        return speed;
    }

    public void setGoalPositionX(int n) {
        goalPosX = n;
    }

    public void setGoalPositionY(int n) {
        goalPosY = n;
    }

    public int getGoalPositionX() {
        return goalPosX;
    }

    public int getGoalPositionY() {
        return goalPosY;
    }

    /**
     * Set's the projectile's orientation.
     * <p>
     * MISSILE_2's image file points north.
     * A heading of 0 renders it pointing east.
     * A heading of 45 points southeast, pivoting the body around the tip.
     * A heading of 90 points south, with the body above the pivot point.
     *
     * @param n degrees clockwise (can be negative)
     */
    public void setHeading(int n) {
        heading = n;
    }

    public int getHeading() {
        return heading;
    }

    /**
     * Unknown.
     *
     * @param n player ship (0) or nearby ship (1), even for drones' projectiles
     */
    public void setOwnerId(int n) {
        ownerId = n;
    }

    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Unknown.
     * <p>
     * A unique number for this projectile, presumably copied from some
     * global counter which increments with each new projectile.
     * <p>
     * The DamageState will usually have the same value set for its own
     * selfId. But not always!? Projectile type and pending/fired status are
     * not predictive.
     */
    public void setSelfId(int n) {
        selfId = n;
    }

    public int getSelfId() {
        return selfId;
    }

    public void setDamage(DamageState damage) {
        this.damage = damage;
    }

    public DamageState getDamage() {
        return damage;
    }

    /**
     * Unknown.
     * <p>
     * There doesn't appear to be a ticks field to track when to start
     * dying?
     */
    public void setLifespan(int n) {
        lifespan = n;
    }

    public int getLifespan() {
        return lifespan;
    }

    /**
     * Sets which ship to eventually use as the origin for position
     * coordinates.
     *
     * @param n player ship (0) or nearby ship (1)
     * @see #setCurrentSpace(int)
     */
    public void setDestinationSpace(int n) {
        destinationSpace = n;
    }

    public int getDestinationSpace() {
        return destinationSpace;
    }

    /**
     * Sets which ship to use as the origin for position coordinates.
     *
     * @param n player ship (0) or nearby ship (1)
     * @see #setDestinationSpace(int)
     */
    public void setCurrentSpace(int n) {
        currentSpace = n;
    }

    public int getCurrentSpace() {
        return currentSpace;
    }

    /**
     * Unknown.
     *
     * @param n player ship (0) or nearby ship (1)
     * @see #setDestinationSpace(int)
     * @see #setOwnerId(int)
     */
    public void setTargetId(int n) {
        targetId = n;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setDead(boolean b) {
        dead = b;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDeathAnimId(String s) {
        deathAnimId = s;
    }

    public String getDeathAnimId() {
        return deathAnimId;
    }

    /**
     * Sets an animSheet to play depicting the projectile in flight.
     * <p>
     * TODO: This has been observed as "" when it's an asteroid!?
     */
    public void setFlightAnimId(String s) {
        flightAnimId = s;
    }

    public String getFlightAnimId() {
        return flightAnimId;
    }

    /**
     * Sets the death anim state, played on impact.
     * <p>
     * TODO: Determine what happens when the projectile is shot.
     *
     * @see #setDeathAnimId(String)
     */
    public void setDeathAnim(AnimState anim) {
        deathAnim = anim;
    }

    public AnimState getDeathAnim() {
        return deathAnim;
    }

    /**
     * Sets the flight anim state, played while in transit.
     * <p>
     * Newly spawned projectiles, and pending ones that haven't been fired
     * yet, have their flightAnims playing set to true.
     *
     * @see #setFlightAnimId(String)
     */
    public void setFlightAnim(AnimState anim) {
        flightAnim = anim;
    }

    public AnimState getFlightAnim() {
        return flightAnim;
    }

    public void setVelocityX(int n) {
        velocityX = n;
    }

    public void setVelocityY(int n) {
        velocityY = n;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    /**
     * Sets whether this projectile will never hit its target.
     * <p>
     * FTL will mark it as missed before it passes its target. This is
     * probably set when it's created.
     *
     * @see #setPassedTarget(boolean)
     */
    public void setMissed(boolean b) {
        missed = b;
    }

    public boolean hasMissed() {
        return missed;
    }

    /**
     * Sets whether this projectile hit a target (even shields).
     */
    public void setHitTarget(boolean b) {
        hitTarget = b;
    }

    public boolean hasHitTarget() {
        return hitTarget;
    }

    /**
     * Sets the sound to play when this projectile hits something solid.
     * <p>
     * This will be a tag name from "sounds.xml", such as "hitHull2".
     */
    public void setHitSolidSound(String s) {
        hitSolidSound = s;
    }

    public String getHitSolidSound() {
        return hitSolidSound;
    }

    /**
     * Sets the sound to play when this projectile hits shields.
     * <p>
     * This will be a tag name from "sounds.xml", such as "hitShield3".
     */
    public void setHitShieldSound(String s) {
        hitShieldSound = s;
    }

    public String getHitShieldSound() {
        return hitShieldSound;
    }

    /**
     * Sets the sound to play when this projectile misses.
     * <p>
     * This will be a tag name from "sounds.xml", such as "miss".
     */
    public void setMissSound(String s) {
        missSound = s;
    }

    public String getMissSound() {
        return missSound;
    }

    /**
     * Unknown.
     * <p>
     * When not set, this is -1.
     */
    public void setEntryAngle(int n) {
        entryAngle = n;
    }

    public int getEntryAngle() {
        return entryAngle;
    }

    /**
     * Unknown.
     */
    public void setStartedDying(boolean b) {
        startedDying = b;
    }

    public boolean hasStartedDying() {
        return startedDying;
    }

    /**
     * Sets whether this projectile has passed its target.
     * <p>
     * FTL will have already marked it as having missed first.
     *
     * @see #setMissed(boolean)
     */
    public void setPassedTarget(boolean b) {
        passedTarget = b;
    }

    public boolean hasPassedTarget() {
        return passedTarget;
    }

    public void setType(int n) {
        type = n;
    }

    public int getType() {
        return type;
    }

    /**
     * Sets whether a red dot should be painted at the targeted location.
     * <p>
     * This is used by burst volleys (e.g., flak).
     */
    public void setBroadcastTarget(boolean b) {
        broadcastTarget = b;
    }

    public boolean getBroadcastTarget() {
        return broadcastTarget;
    }

    public void setExtendedInfo(ExtendedProjectileInfo info) {
        extendedInfo = info;
    }

    public <T extends ExtendedProjectileInfo> T getExtendedInfo(Class<T> infoClass) {
        if (extendedInfo == null) return null;
        return infoClass.cast(extendedInfo);
    }


    private String prettyInt(int n) {
        if (n == Integer.MIN_VALUE) return "MIN";
        if (n == Integer.MAX_VALUE) return "MAX";

        return String.format("%d", n);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Projectile Type:   %s%n", projectileType.toString()));

        if (ProjectileType.INVALID.equals(projectileType)) {
            result.append("\n");
            result.append("(When Projectile Type is INVALID, no other fields are set.)\n");
            return result.toString();
        }

        result.append(String.format("Current Position:  %8d,%8d (%9.03f,%9.03f)%n", currentPosX, currentPosY, currentPosX / 1000f, currentPosY / 1000f));
        result.append(String.format("Previous Position: %8d,%8d (%9.03f,%9.03f)%n", prevPosX, prevPosY, prevPosX / 1000f, prevPosY / 1000f));
        result.append(String.format("Speed:             %8d (%7.03f)%n", speed, speed / 1000f));
        result.append(String.format("Goal Position:     %8d,%8d (%9.03f,%9.03f)%n", goalPosX, goalPosY, goalPosX / 1000f, goalPosY / 1000f));
        result.append(String.format("Heading:           %8d%n", heading));
        result.append(String.format("Owner Id?:         %8d%n", ownerId));
        result.append(String.format("Self Id?:          %8d%n", selfId));

        result.append(String.format("%nDamage...%n"));
        if (damage != null) {
            result.append(damage.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\n");

        result.append(String.format("Lifespan:          %8d%n", lifespan));
        result.append(String.format("Destination Space: %8d%n", destinationSpace));
        result.append(String.format("Current Space:     %8d%n", currentSpace));
        result.append(String.format("Target Id?:        %8d%n", targetId));
        result.append(String.format("Dead:              %8b%n", dead));
        result.append(String.format("Death AnimId:      %s%n", deathAnimId));
        result.append(String.format("Flight AnimId:     %s%n", flightAnimId));

        result.append(String.format("%nDeath Anim?...%n"));
        if (deathAnim != null) {
            result.append(deathAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append(String.format("%nFlight Anim?...%n"));
        if (flightAnim != null) {
            result.append(flightAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\n");

        result.append(String.format("Velocity (x,y):    %8d,%6d (%7.03f,%7.03f)%n", velocityX, velocityY, velocityX / 1000f, velocityY / 1000f));
        result.append(String.format("Missed:            %8b%n", missed));
        result.append(String.format("Hit Target:        %8b%n", hitTarget));
        result.append(String.format("Hit Solid Sound:   %s%n", hitSolidSound));
        result.append(String.format("Hit Shield Sound:  %s%n", hitShieldSound));
        result.append(String.format("Miss Sound:        %s%n", missSound));
        result.append(String.format("Entry Angle?:      %8s%n", prettyInt(entryAngle)));
        result.append(String.format("Started Dying:     %8b%n", startedDying));
        result.append(String.format("Passed Target?:    %8b%n", passedTarget));

        result.append("\n");

        result.append(String.format("Type?:             %8d%n", type));
        result.append(String.format("Broadcast Target:  %8b (Red dot at targeted location)%n", broadcastTarget));

        result.append(String.format("%nExtended Projectile Info...%n"));
        if (extendedInfo != null) {
            result.append(extendedInfo.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
