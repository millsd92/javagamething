package gameengine.abstractions;

import gameengine.Game;

public abstract class MovableObject extends AnimatedObject
{
    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private boolean isMoving = false, hasCollided = false, startMoving = false, stopping = false,
            yMovement = false, changingDirection = false;
    private double currentX, currentY, deltaX, deltaY;
    private int width, height;

    private static final double TERMINAL_VELOCITY_X = 25;
    private static final double TERMINAL_VELOCITY_Y = 25;
    //</editor-fold>

    protected void initializeMovable(String filename, int animationSpeed, double startingX, double startingY,
                                     AnimationState startingState, Direction currentDirection)
    {
        initializeAnimations(filename, animationSpeed, startingState, currentDirection);
        setCurrentX(startingX);
        setCurrentY(startingY);
        setSize();
    }

    public void move(double interpolation)
    {
        if (stopping)
        {
            if (deltaX > 0.0)
                deltaX -= 2 * (deltaX / getCurrentAnimationSize());
            calculateMovement(interpolation);
            setAnimationSpeed(1);
            setCurrentState(AnimationState.STOPPING);
            if (transitionAnimate(AnimationState.IDLE))
            {
                stopping = false;
                isMoving = false;
                startMoving = false;
            }
        }
        else if (isMoving)
        {
            if (deltaX < 5.0)
                deltaX += 0.5;
            calculateMovement(interpolation);
            setAnimationSpeed(1);
            setCurrentState(AnimationState.MOVING);
            animate();
        }
        else if (startMoving)
        {
            deltaX = 0.0;
            calculateMovement(interpolation);
            setAnimationSpeed(1);
            setCurrentState(AnimationState.START_MOVING);
            if (transitionAnimate(AnimationState.MOVING))
            {
                isMoving = true;
                startMoving = false;
                stopping = false;
            }
        }
        else
        {
            setAnimationSpeed(10);
            setCurrentState(AnimationState.IDLE);
            animate();
        }
    }

    public abstract void calculateMovement(double interpolation);

    //---------- Getters ----------//
    //<editor-fold desc="Getters">
    public boolean isMoving()
    { return isMoving; }

    public boolean isStopping()
    { return stopping; }

    public boolean hasCollided()
    { return hasCollided; }

    public boolean hasYMovement()
    { return yMovement; }

    public double getCurrentX()
    { return currentX; }

    public double getCurrentY()
    { return currentY; }

    public double getDeltaX()
    { return deltaX; }

    public double getDeltaY()
    { return deltaY; }

    public int getWidth()
    { return width; }

    public int getHeight()
    { return height; }
    //</editor-fold>

    //---------- Setters ----------//
    //<editor-fold desc="Setters">
    public void setMoving(boolean isMoving)
    { this.isMoving = isMoving; }

    public void setStartMoving(boolean startMoving)
    { this.startMoving = startMoving; }

    public void setStopping(boolean stopping)
    { this.stopping = stopping; }

    public void setCurrentY(double currentY)
    {
        if (currentY + height <= Game.SCREEN_HEIGHT && currentY >= 0.0)
            this.currentY = currentY;
        else if (currentY + height > Game.SCREEN_HEIGHT)
        {
            hasCollided = true;
            this.currentY = Game.SCREEN_HEIGHT - height;
        }
        else if (currentY < 0.0)
        {
            hasCollided = true;
            this.currentY = 0.0;
        }
    }

    public void setCurrentX(double currentX)
    {
        if (currentX >= 0.0 && currentX + width < Game.SCREEN_WIDTH)
            this.currentX = currentX;
        else if (currentX < 0.0)
        {
            hasCollided = true;
            this.currentX = 0.0;
        }
        else if (currentX + width > Game.SCREEN_WIDTH)
        {
            hasCollided = true;
            this.currentX = Game.SCREEN_WIDTH - width;
        }
    }

    public void setDeltaX(double deltaX)
    { this.deltaX = Math.min(deltaX, TERMINAL_VELOCITY_X); }

    public void setDeltaY(double deltaY)
    { this.deltaY = Math.min(deltaY, TERMINAL_VELOCITY_Y); }

    public void setYMovement(boolean yMovement)
    { this.yMovement = yMovement; }

    private void setSize()
    {
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public void changeDirection()
    { this.changingDirection = true; }
    //</editor-fold>
}