package gameengine;

// Necessary imports.
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Game extends JFrame
{
    private final double TARGET_FPS = 60.0;
    private final long NANO_OFFSET = 1000000000;
    private final double TARGET_RENDER_TIME = NANO_OFFSET / TARGET_FPS;
    private static boolean isRunning = false;
    private static boolean isPaused = false;
    private int CURRENT_FPS;

    Game(DisplayMode displayMode, GraphicsDevice device, boolean isFullscreen)
    {
        //---------- Setting Up Screen ----------//
        setTitle("Here's the game!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (isFullscreen)
        {
            device.setFullScreenWindow(this);
            device.setDisplayMode(displayMode);
        }
        else
            setSize(displayMode.getWidth(), displayMode.getHeight());

        //---------- Attempt to test FPS ---------//
        startGame();

        //---------- Setting Up Controls ----------//
        addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            { }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    isRunning = false;
                    isPaused = true;
                    Game.this.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            { }
        });
    }

    /*
     * Creates the game loop on a new thread.
     */
    private void startGame()
    {
        Thread gameLoop = new Thread(this::runGameLoop);
        gameLoop.start();
    }

    private void runGameLoop()
    {
        double previousUpdateTime = System.nanoTime();
        double previousRenderTime = System.nanoTime();
        int lastSecond = (int) (previousUpdateTime / NANO_OFFSET), thisSecond = lastSecond;
        double currentTime;
        float interpolation;

        while (isRunning)
        {
            if (!isPaused)
            {
                currentTime = System.nanoTime();

                while (currentTime - previousUpdateTime > TARGET_RENDER_TIME)
                {
                    positionUpdate();
                    previousUpdateTime = System.nanoTime();
                }

                interpolation = Math.min(1.0f, (float) ((currentTime - previousRenderTime) / TARGET_RENDER_TIME));
                renderGame(interpolation);
                previousRenderTime = System.nanoTime();

                thisSecond = (int) (previousUpdateTime / NANO_OFFSET);
                if (thisSecond > lastSecond)
                {
                    lastSecond = thisSecond;
                }
            }
        }
    }

    private void positionUpdate()
    {

    }

    private void renderGame(float interpolation)
    {

    }
}
