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
        setCurrentX(getCurrentX() + (getDeltaX() + interpolation));
        if (hasYMovement())
            setCurrentY(getCurrentY() + (getDeltaY() + interpolation));
    }
}