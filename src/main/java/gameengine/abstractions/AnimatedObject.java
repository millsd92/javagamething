package gameengine.abstractions;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * I will reiterate some of what I said in the README file here. Animations are specific due to their
 * programmatic way of being loaded in. The images need to be .png files. They also need to be named
 * based on the strings here. For instance: if the sprite's name is evil-eye-monster-thing then all
 * idle animations need to be called evil-eye-monster-thing-idle1.png, evil-eye-monster-thing-idle2.png,
 * etc. The other animations need to be in line with the string constants defined at the top of this class
 * for consistency's sake.
 */
public abstract class AnimatedObject
{
    //---------- Filename Strings ----------//
    //<editor-fold desc="Filename Strings">
    private static final String IDLE_STRING = "-idle";
    private static final String MOVING_STRING = "-moving";
    private static final String TAKING_DAMAGE_STRING = "-damaged";
    private static final String HEALING_STRING = "-healing";
    private static final String ATTACKING_STRING = "-attacking";
    private static final String DEFENDING_STRING = "-defending";
    private static final String COLLIDED_STRING = "-collided";
    private static final String START_MOVING_STRING = "-start-moving";
    private static final String STOPPING_STRING = "-stopping";
    private static final String CHANGE_DIRECTION_STRING = "-changing-direction";
    private static final String IMAGE_EXTENSION = ".png";

    public static final double GRAVITY_OVER_TIME = 0.25;
    //</editor-fold>

    //---------- Class Variables ----------//
    //<editor-fold desc="Class Variables">
    private AnimationState currentState, previousState;
    private Direction currentDirection;
    private int animationSpeed, currentFrame = 0, currentImageIndex = 0;
    BufferedImage currentImage;
    private ArrayList<BufferedImage> idleImages = new ArrayList<>(), movingImages = new ArrayList<>(),
            takingDamageImages = new ArrayList<>(), healingImages = new ArrayList<>(),
            attackingImages = new ArrayList<>(), defendingImages = new ArrayList<>(),
            collidedImages = new ArrayList<>(), startMovingImages = new ArrayList<>(),
            stoppingImages = new ArrayList<>(), changingDirectionImages = new ArrayList<>(), currentAnimation;
    //</editor-fold>

    void initializeAnimations(String filename, int animationSpeed, AnimationState startingState,
                              Direction currentDirection)
    {
        currentState = previousState = startingState;
        this.currentDirection = currentDirection;
        this.animationSpeed = animationSpeed;
        setAllImages(filename);
        setCurrentAnimation();
        currentImage = currentAnimation.get(0);
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
                setCurrentAnimation();
                currentImageIndex = 0;
            }
            if (currentImageIndex > currentAnimation.size() - 1)
                currentImageIndex = 0;
            if (currentDirection == Direction.RIGHT)
                currentImage = currentAnimation.get(currentImageIndex);
            else
                currentImage = flipImage(currentAnimation.get(currentImageIndex));
            currentImageIndex++;
        }
    }

    boolean transitionAnimate(AnimationState nextState)
    {
        animate();
        if (currentImageIndex == currentAnimation.size() - 1)
        {
            previousState = currentState;
            currentState = nextState;
            setCurrentAnimation();
            currentFrame = 0;
            return true;
        }
        else
            return false;
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

    public BufferedImage getCurrentImage()
    { return currentImage; }

    int getCurrentAnimationSize()
    { return currentAnimation.size(); }
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

    private void setCurrentAnimation()
    {
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
            case START_MOVING:
                currentAnimation = startMovingImages;
                break;
            case STOPPING:
                currentAnimation = stoppingImages;
                break;
            case CHANGING_DIRECTION:
                currentAnimation = changingDirectionImages;
                break;
        }
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
        setImages(startMovingImages, filename, START_MOVING_STRING);
        setImages(stoppingImages, filename, STOPPING_STRING);
        setImages(changingDirectionImages, filename, CHANGE_DIRECTION_STRING);
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

    @NotNull
    private BufferedImage flipImage(@NotNull BufferedImage image)
    {
        AffineTransform transform = new AffineTransform();
        transform.concatenate(AffineTransform.getScaleInstance(-1, 1));
        transform.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage returnImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = returnImage.createGraphics();
        graphics.transform(transform);
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return returnImage;
    }
    //</editor-fold>
}
