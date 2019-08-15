package gameengine;

// Necessary imports.
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class Game extends JFrame
{
    public Game(DisplayMode displayMode, GraphicsDevice device, boolean isFullscreen)
    {
        setTitle("Here's the game!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        device.setFullScreenWindow(this);
        device.setDisplayMode(displayMode);
        System.out.println(Arrays.asList(device.getDisplayModes()).contains(displayMode));
        System.out.println(displayMode.getRefreshRate());
        System.out.println(device.isDisplayChangeSupported());
        System.out.println(device.isFullScreenSupported());
    }
}
