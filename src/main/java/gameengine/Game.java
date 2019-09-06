package gameengine;

// Necessary imports.
import gameengine.abstractions.AnimationState;
import gameengine.abstractions.Direction;
import gameengine.graphicobjects.Hero;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Game extends JComponent
{
    private static boolean isRunning = false;
    private static boolean isPaused = false;
    private int FRAMES_PER_SECOND = 0;
    private static Hero hero;
    public static int SCREEN_HEIGHT, SCREEN_WIDTH;

    Game(@NotNull JFrame frame, boolean isFullscreen)
    {
        //---------- Make it Large ----------//
        setSize(frame.getSize());

        if (!isFullscreen)
        {
            SCREEN_HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height
                    - Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration()).bottom;
            SCREEN_WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
        }
        else
        {
            SCREEN_WIDTH = frame.getWidth();
            SCREEN_HEIGHT = frame.getHeight();
        }

        //---------- Start the Loop ---------//
        startGame();
    }

    /*
     * Creates the game loop on a new thread.
     */
    private void startGame()
    {
        hero = new Hero(GameMain.IMAGES_FOLDER + File.separator + "sprites" + File.separator + "hero",
                10, 10, 200, AnimationState.IDLE, Direction.RIGHT,
                25.0, 25.0, 0.1, 7.5, .5, 5);
        isRunning = true;
        Thread gameLoop = new Thread(this::runGameLoop);
        gameLoop.start();
    }

    /*
     * A game loop based on this website: http://www.java-gaming.org/index.php?topic=24220.0
     */
    private void runGameLoop()
    {
        double lastUpdate = System.nanoTime();
        double lastRender;
        int frameCount = 0;

        long NANO_OFFSET = 1000000000;
        int lastSecond = (int) (lastUpdate / NANO_OFFSET);

        while (isRunning)
        {
            if (!isPaused)
            {
                double currentTime = System.nanoTime();
                int numberOfUpdates = 0;

                int MAX_UPDATES = 5;
                double TARGET_UPDATE = 30.0;
                double TARGET_UPDATE_TIME = NANO_OFFSET / TARGET_UPDATE;
                while (currentTime - lastUpdate > TARGET_UPDATE_TIME && numberOfUpdates < MAX_UPDATES)
                {
                    processInput();
                    updateGame();
                    lastUpdate += TARGET_UPDATE_TIME;
                    numberOfUpdates++;
                }

                double TARGET_FPS = 60.0;
                double TARGET_RENDER_TIME = NANO_OFFSET / TARGET_FPS;
                double interpolation = Math.min(1.0f, (currentTime - lastUpdate) / TARGET_RENDER_TIME);

                renderGame(interpolation);
                lastRender = currentTime;
                frameCount++;

                int currentSecond = (int) (lastUpdate / NANO_OFFSET);
                if (currentSecond > lastSecond)
                {
                    FRAMES_PER_SECOND = frameCount;
                    frameCount = 0;
                    lastSecond = currentSecond;
                }

                while (currentTime - lastRender < TARGET_RENDER_TIME && currentTime - lastUpdate
                        < TARGET_UPDATE_TIME)
                {
                    Thread.yield();
                    try
                    {
                        Thread.sleep(0, 1);
                    }
                    catch (InterruptedException ignored) {}
                    currentTime = System.nanoTime();
                }
            }
            else
            {
                Thread.yield();
                try
                {
                    Thread.sleep(0, 1);
                }
                catch (InterruptedException ignored) {}
            }
        }
    }

    private void processInput()
    { InputManager.processLatestInputs(); }

    private void updateGame()
    {
    }

    private void renderGame(double interpolation)
    {
        hero.move(interpolation);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(hero.getCurrentImage(), (int) Math.floor(hero.getCurrentX()),
                (int) Math.floor(hero.getCurrentY()), this);
        g.setFont(GameMain.PIXEL_FONT_LARGE);
        g.drawString(FRAMES_PER_SECOND + " FPS",
                getWidth() - g.getFontMetrics().stringWidth(FRAMES_PER_SECOND + " FPS "),
                g.getFontMetrics().getHeight());
        Toolkit.getDefaultToolkit().sync();
    }

    @Contract(pure = true)
    static Hero getHero() { return hero; }

    static void setIsPaused(boolean isPaused) { Game.isPaused = isPaused; }

    @Contract(pure = true)
    static boolean isPaused() { return isPaused; }
}