// Necessary imports.
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public final class GameMain extends JFrame
{
    //---------- Defining Necessary GUI Components ----------//
    private ImagePanel pnlBackground;

    //---------- Defining Colors ----------//
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(88, 88, 100);
    public static final Color DEFAULT_TEXT_COLOR = new Color(235, 235, 215);

    //---------- Defining Fonts ----------//
    public static Font PIXEL_FONT_XLARGE;
    public static Font PIXEL_FONT_LARGE;

    private GameMain()
    {
        //---------- Setting Up Default Frame Attributes ----------//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Welcome to the Game...");

        //---------- Retrieving Background Image ----------//
        try
        {
            pnlBackground = new ImagePanel(ImageIO.read(new File("javagamething" + File.separator + "src"
                    + File.separator + "resources" + File.separator + "images" + File.separator
                    + "mainmenubackground.png")));
        }
        catch (IOException ex)
        {
            JOptionPane.showConfirmDialog(this,
                    "Missing image files! Reinstall and try again. Exiting.", "Error!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        //---------- Setting Up Font ----------//
        try
        {
            PIXEL_FONT_XLARGE = Font.createFont(Font.TRUETYPE_FONT, new File("javagamething" + File.separator
                    + "src" + File.separator + "resources" + File.separator + "fonts" + File.separator
                    + "VT323-Regular.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(PIXEL_FONT_XLARGE);
            PIXEL_FONT_LARGE = PIXEL_FONT_XLARGE.deriveFont(24f);
        }
        catch (FontFormatException | IOException e)
        {
            JOptionPane.showConfirmDialog(this,
                    "Missing font files! Reinstall and try again. Exiting.", "Error!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        //---------- Setting Up Components ----------//
        JLabel lblPrompt = new JLabel("Please select an option below:");
        lblPrompt.setForeground(DEFAULT_TEXT_COLOR);
        lblPrompt.setFont(PIXEL_FONT_XLARGE);
        lblPrompt.setHorizontalAlignment(JLabel.CENTER);

        JButton btnPlay = new JButton("Play");
        btnPlay.setFont(PIXEL_FONT_LARGE);

        JButton btnOptions = new JButton("Options");
        btnOptions.setFont(PIXEL_FONT_LARGE);

        JButton btnClose = new JButton("Exit");
        btnClose.setFont(PIXEL_FONT_LARGE);
        btnClose.addActionListener((ActionEvent) ->
        { dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); });

        //---------- Setting Up Containers ----------//
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel pnlInput = new JPanel();
        pnlInput.setBackground(DEFAULT_BACKGROUND_COLOR);
        pnlInput.setLayout(new GridBagLayout());
        constraints.gridwidth = 3;
        pnlInput.add(lblPrompt, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0.33f;
        constraints.gridy = 1;
        pnlInput.add(btnPlay, constraints);
        constraints.gridx = 1;
        pnlInput.add(btnOptions, constraints);
        constraints.gridx = 2;
        pnlInput.add(btnClose, constraints);

        pnlBackground.setBackground(DEFAULT_BACKGROUND_COLOR);
        pnlBackground.setPreferredSize(new Dimension(670, 400));

        add(pnlBackground, BorderLayout.NORTH);
        add(pnlInput);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * A private, inner class that allows for a panel's background to be an image.
     */
    private static final class ImagePanel extends JPanel
    {
        private Image image;

        private ImagePanel(Image image)
        { this.image = image; }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }

    // The entry-point of the application.
    public static void main(String[] args)
    { SwingUtilities.invokeLater(() -> new GameMain().setVisible(true)); }
}
