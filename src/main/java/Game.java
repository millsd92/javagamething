import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Game extends JFrame
{
    //---------- Defining GUI Components ----------//
    private ImagePanel pnlBackground;
    private JLabel lblPrompt;
    private JButton btnPlay;
    private JButton btnOptions;
    private JButton btnClose;

    private Game()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(640, 360));
        setTitle("Welcome to the Game...");
        setLocationRelativeTo(null);

        System.out.println("src" + File.separator + "images" + File.separator + "menu_placeholder.png");
        try
        {
            pnlBackground = new ImagePanel(ImageIO.read(new File("src" + File.separator + "resources"
                    + File.separator + "images" + File.separator + "menu_placeholder.png")));
        }
        catch (IOException ex)
        {
            JOptionPane.showConfirmDialog(this,
                    "Missing image files! Reinstall and try again. Exiting.", "Error!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        add(pnlBackground);
    }

    private static final class ImagePanel extends JPanel
    {
        private BufferedImage image;

        private ImagePanel(BufferedImage image)
        { this.image = image; }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    public static void main(String[] args)
    { SwingUtilities.invokeLater(() -> new Game().setVisible(true)); }
}
