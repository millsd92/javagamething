package gameengine.abstractions;

import gameengine.Game;

public abstract class MovableObject extends AnimatedObject
{
    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private boolean isMoving = false, hasCollided = false;
    private double currentX, currentY;
    private int width, height;
    //</editor-fold>

    protected void initializeMovable(String filename, int animationSpeed, double startingX, double startingY,
                                     AnimationState startingState)
    {
        initializeAnimations(filename, animationSpeed, startingState);
        setCurrentX(startingX);
        setCurrentY(startingY);
        setSize();
    }

    public void move(double interpolation)
    {
        if (isMoving)
        {
            calculateMovement(interpolation);
            setAnimationSpeed(1);
            setCurrentState(AnimationState.MOVING);
            isMoving = false;
        }
        else
        {
            setAnimationSpeed(10);
            setCurrentState(AnimationState.IDLE);
        }
        animate();
    }

    public abstract void calculateMovement(double interpolation);

    //---------- Getters ----------//
    //<editor-fold desc="Getters">
    public boolean isMoving()
    { return isMoving; }

    public boolean hasCollided()
    { return hasCollided; }

    public double getCurrentX()
    { return currentX; }

    public double getCurrentY()
    { return currentY; }

    public int getWidth()
    { return width; }

    public int getHeight()
    { return height; }
    //</editor-fold>

    //---------- Setters ----------//
    //<editor-fold desc="Setters">
    public void setMoving(boolean isMoving)
    { this.isMoving = isMoving; }

    public void setCurrentY(double currentY)
    {
        if (currentY + height <= Game.SCREEN_HEIGHT)
            this.currentY = currentY;
        else
        {
            hasCollided = true;
            this.currentY = Game.SCREEN_HEIGHT - height;
        }
    }

    public void setCurrentX(double currentX)
    {
        /*if (currentX - width >= 0.0)
            this.currentX = currentX;
        else
        {
            hasCollided = true;
            this.currentX = 0.0;
        }*/
        this.currentX = currentX;
    }

    public void setSize()
    {
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }
    //</editor-fold>
}