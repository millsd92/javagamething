package gameengine.graphicobjects;

import gameengine.abstractions.AnimationState;

public class Hero extends gameengine.abstractions.MovableObject
{
    public Hero(String filename, int animationSpeed, double startingX, double startingY,
                AnimationState startingState)
    {
        initializeMovable(filename, animationSpeed, startingX, startingY, startingState);
    }

    @Override
    public void calculateMovement(double interpolation)
    {
        setCurrentX(getCurrentX() + (5 + interpolation));
    }
}