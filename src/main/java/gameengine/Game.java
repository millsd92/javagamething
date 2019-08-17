package gameengine;

// Necessary imports.
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Game extends JFrame
{
    private JLabel lblFPSCounter;

    private static boolean isRunning = false;
    private static boolean isPaused = false;
    private int FRAMES_PER_SECOND = 0;

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

        //---------- Setting Up HUD ----------//
        lblFPSCounter = new JLabel();
        lblFPSCounter.setOpaque(true);
        lblFPSCounter.setFont(GameMain.PIXEL_FONT_LARGE);
        lblFPSCounter.setText(FRAMES_PER_SECOND + " FPS ");
        lblFPSCounter.setHorizontalAlignment(JLabel.RIGHT);

        JPanel HUD = new JPanel();
        HUD.setOpaque(true);
        HUD.setLayout(new BorderLayout());
        HUD.add(lblFPSCounter, BorderLayout.NORTH);

        add(HUD);

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
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    isPaused = false;
                    isRunning = true;
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
            double currentTime = System.nanoTime();
            int numberOfUpdates = 0;

            int MAX_UPDATES = 5;
            double TARGET_UPDATE = 30.0;
            double TARGET_UPDATE_TIME = NANO_OFFSET / TARGET_UPDATE;
            while (currentTime - lastUpdate > TARGET_UPDATE_TIME && numberOfUpdates < MAX_UPDATES)
            {
                //updateGame();
                lastUpdate += TARGET_UPDATE_TIME;
                numberOfUpdates++;
            }

            double TARGET_FPS = 60.0;
            double TARGET_RENDER_TIME = NANO_OFFSET / TARGET_FPS;
            double interpolation = Math.min(1.0f, (currentTime - lastUpdate) / TARGET_RENDER_TIME);

            renderGame(interpolation);
            //FRAMES_PER_SECOND = (int) (NANO_OFFSET / (System.nanoTime() - lastRender));
            lastRender = currentTime;
            frameCount++;

            int currentSecond = (int) (lastUpdate / NANO_OFFSET);
            if (currentSecond > lastSecond)
            {
                FRAMES_PER_SECOND = frameCount;
                frameCount = 0;
                lastSecond = currentSecond;
            }

            while (currentTime - lastRender < TARGET_RENDER_TIME && currentTime - lastUpdate < TARGET_UPDATE_TIME)
            {
                Thread.yield();
                try { Thread.sleep(0, 1); } catch (InterruptedException ignored) {}
                currentTime = System.nanoTime();
            }
        }
    }

    private void renderGame(double interpolation)
    {
        lblFPSCounter.setText(FRAMES_PER_SECOND + " FPS ");
    }
}
