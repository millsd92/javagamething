package gameengine.abstractions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract class AnimatedObject
{
    private static final String IDLE_STRING = "-idle";
    private static final String MOVING_STRING = "-moving";
    private static final String TAKING_DAMAGE_STRING = "-damaged";
    private static final String HEALING_STRING = "-healing";
    private static final String ATTACKING_STRING = "-attacking";
    private static final String DEFENDING_STRING = "-defending";
    private static final String COLLIDED_STRING = "-collided";
    private static final String IMAGE_EXTENSION = ".png";

    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private AnimationState currentState, previousState;
    private Direction currentDirection;
    private int animationSpeed, currentFrame = 0, currentImageIndex = 0;
    BufferedImage currentImage;
    private ArrayList<BufferedImage> idleImages = new ArrayList<>(), movingImages = new ArrayList<>(),
            takingDamageImages = new ArrayList<>(), healingImages = new ArrayList<>(),
            attackingImages = new ArrayList<>(), defendingImages = new ArrayList<>(),
            collidedImages = new ArrayList<>(), currentAnimation;
    //</editor-fold>

    void initializeAnimations(String filename, int animationSpeed, AnimationState startingState)
    {
        currentState = previousState = startingState;
        this.animationSpeed = animationSpeed;
        setAllImages(filename);
        switch (currentState)
        {
            case IDLE:
                currentAnimation = idleImages;
                currentImage = currentAnimation.get(0);
                break;
            case MOVING:
                currentAnimation = movingImages;
                currentImage = currentAnimation.get(0);
                break;
            case TAKING_DAMAGE:
                currentAnimation = takingDamageImages;
                currentImage = currentAnimation.get(0);
                break;
            case HEALING:
                currentAnimation = healingImages;
                currentImage = currentAnimation.get(0);
                break;
            case ATTACKING:
                currentAnimation = attackingImages;
                currentImage = currentAnimation.get(0);
                break;
            case DEFENDING:
                currentAnimation = defendingImages;
                currentImage = currentAnimation.get(0);
                break;
            case COLLIDED:
                currentAnimation = collidedImages;
                currentImage = currentAnimation.get(0);
                break;
        }
    }

    void animate()
    {
        currentFrame++;
        if (currentFrame >= animationSpeed)
        {
            currentFrame = 0;
            if (currentState != previousState)
            {
                previousState = currentState;
                switch (currentState)
                {
                    case IDLE:
                        currentAnimation = idleImages;
                        break;
                    case MOVING:
                        currentAnimation = movingImages;
                        break;
                    case TAKING_DAMAGE:
                        currentAnimation = takingDamageImages;
                        break;
                    case HEALING:
                        currentAnimation = healingImages;
                        break;
                    case ATTACKING:
                        currentAnimation = attackingImages;
                        break;
                    case DEFENDING:
                        currentAnimation = defendingImages;
                        break;
                    case COLLIDED:
                        currentAnimation = collidedImages;
                        break;
                }
                currentImageIndex = 0;
            }
            if (currentImageIndex > currentAnimation.size() - 1)
                currentImageIndex = 0;
            currentImage = currentAnimation.get(currentImageIndex);
            currentImageIndex++;
        }
    }

    //---------- Getters ----------//
    //<editor-fold desc="Getters">
    public AnimationState getCurrentState()
    { return currentState; }

    public AnimationState getPreviousState()
    { return previousState; }

    public Direction getCurrentDirection()
    { return currentDirection; }

    int getAnimationSpeed()
    { return animationSpeed; }

    BufferedImage getCurrentImage()
    { return currentImage; }
    //</editor-fold>

    //---------- Setters -----------//
    //<editor-fold desc="Setters">
    public void setCurrentState(AnimationState currentState)
    { this.currentState = currentState; }

    public void setPreviousState(AnimationState previousState)
    { this.previousState = previousState; }

    public void setCurrentDirection(Direction currentDirection)
    { this.currentDirection = currentDirection; }

    void setAnimationSpeed(int animationSpeed)
    {
        if (animationSpeed > 0)
            this.animationSpeed = animationSpeed;
        else
            throw new IllegalArgumentException("Animation Speed must be greater than 0!");
    }

    private void setAllImages(String filename)
    {
        setImages(idleImages, filename, IDLE_STRING);
        setImages(movingImages, filename, MOVING_STRING);
        setImages(takingDamageImages, filename, TAKING_DAMAGE_STRING);
        setImages(healingImages, filename, HEALING_STRING);
        setImages(attackingImages, filename, ATTACKING_STRING);
        setImages(defendingImages, filename, DEFENDING_STRING);
        setImages(collidedImages, filename, COLLIDED_STRING);
    }

    private void setImages(ArrayList<BufferedImage> images, String filename, String animation)
    {
        int imageNumber = 1;

        StringBuilder pathToImage;
        File fileToTest;

        do
        {
            pathToImage = new StringBuilder();
            pathToImage.append(filename);
            pathToImage.append(animation);
            pathToImage.append(imageNumber);
            pathToImage.append(IMAGE_EXTENSION);

            imageNumber++;

            fileToTest = new File(pathToImage.toString());
            if (fileToTest.exists())
            {
                try
                {
                    images.add(ImageIO.read(fileToTest));
                }
                catch (IOException ignore)
                {
                    System.out.println("Image fail!");
                }
            }
        }
        while (fileToTest.exists());
    }
    //</editor-fold>
}
