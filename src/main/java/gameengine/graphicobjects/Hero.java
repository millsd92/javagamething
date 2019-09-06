package gameengine.graphicobjects;

import gameengine.abstractions.AnimatedObject;
import gameengine.abstractions.AnimationState;
import gameengine.abstractions.Direction;

public class Hero extends gameengine.abstractions.MovableObject
{
    public Hero(String filename, int animationSpeed, double startingX, double startingY,
                AnimationState startingState, Direction currentDirection, double terminalVelocityX,
                double terminalVelocityY, double elasticity, double jumpHeight, double speed, double maxSpeed)
    {
        initializeMovable(filename, animationSpeed, startingX, startingY, startingState, currentDirection,
                terminalVelocityX, terminalVelocityY, elasticity, jumpHeight, speed, maxSpeed);
    }

    @Override
    public void calculateMovement(double interpolation)
    {
        if (getCurrentDirection() == Direction.RIGHT && getDeltaX() != 0.0)
            setCurrentX(getCurrentX() + (getDeltaX() + interpolation));
        else if (getDeltaX() != 0.0)
            setCurrentX(getCurrentX() - (getDeltaX() + interpolation));
        if (!isGrounded())
        {
            setDeltaY(getDeltaY() + AnimatedObject.GRAVITY_OVER_TIME);
            setCurrentY(getCurrentY() + (getDeltaY() + interpolation));
        }
    }
}