package gameengine.abstractions;

import gameengine.Game;

public abstract class MovableObject extends AnimatedObject
{
    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private boolean isGrounded = false;
    private double currentX, currentY, deltaX, deltaY, elasticity, jumpHeight, speed, maxSpeed;
    private int width, height;

    private double terminalVelocityX, terminalVelocityY;
    //</editor-fold>

    protected void initializeMovable(String filename, int animationSpeed, double startingX, double startingY,
                                     AnimationState startingState, Direction currentDirection,
                                     double terminalVelocityX, double terminalVelocityY, double elasticity,
                                     double jumpHeight, double speed, double maxSpeed)
    {
        initializeAnimations(filename, animationSpeed, startingState, currentDirection);
        setCurrentX(startingX);
        setCurrentY(startingY);
        setTerminalVelocities(terminalVelocityX, terminalVelocityY);
        setElasticity(elasticity);
        setJumpHeight(jumpHeight);
        setSpeed(speed);
        setMaxSpeed(maxSpeed);
        setSize();
    }

    public void move(double interpolation)
    {
        animationStep(true);
        calculateMovement(interpolation);
    }

    public void jump(double jumpHeight)
    {
        setAnimation(AnimationState.JUMPING, AnimationState.FALLING);
        isGrounded = false;
        setDeltaY(-jumpHeight);
    }

    public void changeDirection()
    {
        if (getCurrentState() != AnimationState.CHANGING_DIRECTION && isGrounded && deltaX == 0.0)
            setAnimation(AnimationState.CHANGING_DIRECTION, AnimationState.IDLE);
        else if (getCurrentState() != AnimationState.CHANGING_DIRECTION && isGrounded && deltaX != 0.0)
            idle();
    }

    public void run()
    {
        if (isGrounded)
        {
            if (getCurrentState() != AnimationState.START_MOVING && getCurrentState() != AnimationState.MOVING)
                setAnimation(AnimationState.START_MOVING, AnimationState.MOVING);
            if (deltaX < maxSpeed)
                setDeltaX(deltaX + speed);
        }
        else if (deltaX < maxSpeed)
                setDeltaX(deltaX + (speed / 5.0));
    }

    public void idle()
    {
        if (deltaX > 0.0 && isGrounded && getCurrentState() != AnimationState.STOPPING
                && getCurrentState() != AnimationState.IDLE)
            setAnimation(AnimationState.STOPPING, AnimationState.IDLE);
        else if (!isGrounded && getCurrentState() != AnimationState.FALLING)
            setAnimation(AnimationState.FALLING);
        if (getCurrentState() == AnimationState.IDLE)
            setDeltaX(0.0);
        else if (getCurrentState() == AnimationState.STOPPING)
            setDeltaX(deltaX - 2 * (deltaX / getCurrentAnimationSize()));
        else if (getCurrentState() == AnimationState.FALLING)
            setDeltaX(deltaX - 0.05);
    }

    public abstract void calculateMovement(double interpolation);

    //---------- Getters ----------//
    //<editor-fold desc="Getters">
    public boolean isGrounded()
    { return isGrounded; }

    public double getCurrentX()
    { return currentX; }

    public double getCurrentY()
    { return currentY; }

    protected double getDeltaX()
    { return deltaX; }

    protected double getDeltaY()
    { return deltaY; }

    public double getJumpHeight()
    { return jumpHeight; }

    public double getSpeed()
    { return speed; }
    //</editor-fold>

    //---------- Setters ----------//
    //<editor-fold desc="Setters">
    protected void setCurrentY(double currentY)
    {
        if (currentY + height <= Game.SCREEN_HEIGHT && currentY >= 0.0)
        {
            isGrounded = false;
            this.currentY = currentY;
        }
        else if (currentY + height > Game.SCREEN_HEIGHT)
        {
            if (Math.abs(deltaY) < 0.5)
            {
                setAnimation(AnimationState.LANDING, AnimationState.IDLE);
                isGrounded = true;
            }
            else
                deltaY = (deltaY * elasticity * -1);
            this.currentY = Game.SCREEN_HEIGHT - height;
        }
        else if (currentY < 0.0)
        {
            isGrounded = false;
            this.currentY = 0.0;
        }
    }

    protected void setCurrentX(double currentX)
    {
        if (currentX >= 0.0 && currentX + width < Game.SCREEN_WIDTH)
            this.currentX = currentX;
        else if (currentX < 0.0)
            this.currentX = 0.0;
        else if (currentX + width > Game.SCREEN_WIDTH)
            this.currentX = Game.SCREEN_WIDTH - width;
    }

    private void setDeltaX(double deltaX)
    { this.deltaX = Math.max(Math.min(deltaX, terminalVelocityX), 0.0); }

    protected void setDeltaY(double deltaY)
    { this.deltaY = Math.min(deltaY, terminalVelocityY); }

    private void setTerminalVelocities(double terminalVelocityX, double terminalVelocityY)
    {
        this.terminalVelocityX = terminalVelocityX;
        this.terminalVelocityY = terminalVelocityY;
    }

    private void setSize()
    {
        height = currentImage.getHeight();
        width = currentImage.getWidth();
    }

    private void setElasticity(double elasticity)
    {
        if (elasticity > 0.95)
            this.elasticity = 0.95;
        else
            this.elasticity = Math.max(elasticity, 0.0);
    }

    private void setJumpHeight(double jumpHeight)
    { this.jumpHeight = Math.max(jumpHeight, 0.0); }

    private void setSpeed(double speed)
    {
        if (speed >= 0.0 && speed <= terminalVelocityX)
            this.speed = speed;
        else if (speed < 0.0)
            this.speed = 0.0;
        else
            this.speed = terminalVelocityX;
    }

    private void setMaxSpeed(double maxSpeed)
    {
        if (maxSpeed >= speed && maxSpeed <= terminalVelocityX)
            this.maxSpeed = maxSpeed;
        else if (maxSpeed < speed)
            this.maxSpeed = speed;
        else
            this.maxSpeed = terminalVelocityX;
    }
    //</editor-fold>
}