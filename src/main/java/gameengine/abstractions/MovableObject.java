package gameengine.abstractions;

public abstract class MovableObject
{
    private boolean isMoving = false;

    void move(double interpolation)
    {
        if (isMoving)
        {
            calculateMovement(interpolation);
            isMoving = false;
        }
    }

    abstract void calculateMovement(double interpolation);
}