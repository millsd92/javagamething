package gameengine;

// Necessary imports.
import gameengine.abstractions.AnimationState;
import gameengine.abstractions.Direction;
import gameengine.graphicobjects.Hero;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Game extends JFrame
{
    public static int SCREEN_HEIGHT, SCREEN_WIDTH;
    private static boolean isRunning = false;
    private static boolean isPaused = false;
    private int FRAMES_PER_SECOND = 0;
    private static Hero hero;

    Game(DisplayMode displayMode, GraphicsDevice device, boolean isFullscreen)
    {
        //---------- Setting Up Screen ----------//
        setTitle("Here's the game!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        SCREEN_HEIGHT = displayMode.getHeight();
        SCREEN_WIDTH = displayMode.getWidth();

        if (isFullscreen)
        {
            device.setFullScreenWindow(this);
            device.setDisplayMode(displayMode);
        }
        else
            setSize(displayMode.getWidth(), displayMode.getHeight());

        //---------- Start the Loop ---------//
        startGame();

        //---------- Setting Up Controls ----------//
        addKeyListener(new MainControls());
    }

    /*
     * Creates the game loop on a new thread.
     */
    private void startGame()
    {
        hero = new Hero(GameMain.IMAGES_FOLDER + File.separator + "sprites" + File.separator + "hero",
                10, 10, 200, AnimationState.IDLE, Direction.RIGHT);
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

    private void updateGame()
    {
    }

    private void renderGame(double interpolation)
    {
        hero.move(interpolation);
        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(hero.getCurrentImage(), (int) Math.floor(hero.getCurrentX()),
                (int) Math.floor(hero.getCurrentY()), this);
        g.setFont(GameMain.PIXEL_FONT_LARGE);
        g.drawString(FRAMES_PER_SECOND + " FPS",
                SCREEN_WIDTH - g.getFontMetrics().stringWidth(FRAMES_PER_SECOND + " FPS "),
                g.getFontMetrics().getHeight() + g.getFontMetrics().getMaxAscent()
                        + g.getFontMetrics().getMaxDescent() + g.getFontMetrics().getMaxAdvance());
        Toolkit.getDefaultToolkit().sync();
    }

    public static Hero getHero() { return hero; }

    public static void setIsPaused(boolean isPaused) { Game.isPaused = isPaused; }

    public static boolean isPaused() { return isPaused; }
}
