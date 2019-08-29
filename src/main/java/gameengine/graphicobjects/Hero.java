package gameengine.graphicobjects;

import gameengine.abstractions.AnimationState;
import gameengine.abstractions.Direction;

public class Hero extends gameengine.abstractions.MovableObject
{
    public Hero(String filename, int animationSpeed, double startingX, double startingY,
                AnimationState startingState, Direction currentDirection)
    {
        initializeMovable(filename, animationSpeed, startingX, startingY, startingState, currentDirection);
    }

    @Override
    public void calculateMovement(double interpolation)
    {
        if (getCurrentDirection() == Direction.RIGHT)
        {
            setCurrentX(getCurrentX() + (getDeltaX() + interpolation));
            if (hasYMovement())
                setCurrentY(getCurrentY() + (getDeltaY() + interpolation));
        }
        else
        {
            setCurrentX(getCurrentX() - (getDeltaX() + interpolation));
            if (hasYMovement())
                setCurrentY(getCurrentY() - (getDeltaY() + interpolation));
        }
    }
}