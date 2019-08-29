package gameengine.abstractions;

import gameengine.Game;

public abstract class MovableObject extends AnimatedObject
{
    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private boolean isMoving = false, isGrounded = false, startMoving = false, stopping = false,
            changingDirection = false;
    private double currentX, currentY, deltaX, deltaY;
    private int width, height;

    private double terminalVelocityX, terminalVelocityY;
    //</editor-fold>

    protected void initializeMovable(String filename, int animationSpeed, double startingX, double startingY,
                                     AnimationState startingState, Direction currentDirection,
                                     double terminalVelocityX, double terminalVelocityY)
    {
        initializeAnimations(filename, animationSpeed, startingState, currentDirection);
        setCurrentX(startingX);
        setCurrentY(startingY);
        setTerminalVelocityX(terminalVelocityX);
        setTerminalVelocityY(terminalVelocityY);
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
        else if (changingDirection)
        {
            if (deltaX > 0.0)
                deltaX -= 2 * (deltaX / getCurrentAnimationSize());
            calculateMovement(interpolation);
            setAnimationSpeed(1);
            setCurrentState(AnimationState.CHANGING_DIRECTION);
            if (transitionAnimate(AnimationState.IDLE))
            {
                stopping = false;
                startMoving = false;
                isMoving = false;
                changingDirection = false;
                if (getCurrentDirection() == Direction.LEFT)
                    setCurrentDirection(Direction.RIGHT);
                else
                    setCurrentDirection(Direction.LEFT);
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
            if (!isGrounded)
                calculateMovement(interpolation);
            setDeltaX(0.0);
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

    public boolean changingDirection()
    { return changingDirection; }

    public boolean isGrounded()
    { return isGrounded; }

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

    public void setGrounded(boolean isGrounded)
    { this.isGrounded = isGrounded; }

    public void setCurrentY(double currentY)
    {
        if (currentY + height <= Game.SCREEN_HEIGHT && currentY >= 0.0)
        {
            isGrounded = false;
            this.currentY = currentY;
        }
        else if (currentY - height > Game.SCREEN_HEIGHT)
        {
            isGrounded = true;
            this.currentY = Game.SCREEN_HEIGHT - height;
        }
        else if (currentY < 0.0)
        {
            isGrounded = false;
            this.currentY = 0.0;
        }
    }

    public void setCurrentX(double currentX)
    {
        if (currentX >= 0.0 && currentX + width < Game.SCREEN_WIDTH)
            this.currentX = currentX;
        else if (currentX < 0.0)
            this.currentX = 0.0;
        else if (currentX + width > Game.SCREEN_WIDTH)
            this.currentX = Game.SCREEN_WIDTH - width;
    }

    public void setDeltaX(double deltaX)
    { this.deltaX = Math.min(deltaX, terminalVelocityX); }

    public void setDeltaY(double deltaY)
    { this.deltaY = Math.min(deltaY, terminalVelocityY); }

    public void setTerminalVelocityX(double terminalVelocityX)
    { this.terminalVelocityX = terminalVelocityX; }

    public void setTerminalVelocityY(double terminalVelocityY)
    { this.terminalVelocityY = terminalVelocityY; }

    private void setSize()
    {
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    public void changeDirection()
    { this.changingDirection = true; }
    //</editor-fold>
}