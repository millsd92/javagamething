// Necessary imports.
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public final class GameMain extends JFrame
{
    //---------- Defining Class Constants -----------//
    private static final int BUTTON_BORDER_THICKNESS = 5;

    //---------- Defining Necessary GUI Components ----------//
    private ImagePanel pnlBackground;

    //---------- Defining Colors ----------//
    public static final Color BACKGROUND_COLOR = new Color(88, 88, 100);
    public static final Color TEXT_COLOR = new Color(235, 235, 215);
    public static final Color TEXT_HIGHLIGHT_COLOR = Color.WHITE;
    public static final Color BUTTON_COLOR = new Color(64, 64, 88);
    public static final Color BUTTON_BORDER_COLOR = new Color(55, 55, 64);
    public static final Color BUTTON_HIGHLIGHT_COLOR = new Color(50, 50, 62);
    public static final Color BUTTON_BORDER_HIGHLIGHT_COLOR = new Color(34, 34, 52);

    //---------- Defining Fonts ----------//
    public static Font PIXEL_FONT_XLARGE;
    public static Font PIXEL_FONT_LARGE;

    //---------- Defining Borders ----------//
    public static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, BUTTON_BORDER_THICKNESS),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
    );
    public static final Border BUTTON_BORDER_HIGHLIGHT = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, BUTTON_BORDER_THICKNESS),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
    );

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

        //---------- Setting Up Containers ----------//
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel pnlInput = new JPanel();
        pnlInput.setBackground(BACKGROUND_COLOR);
        pnlInput.setLayout(new GridBagLayout());

        pnlBackground.setBackground(BACKGROUND_COLOR);
        pnlBackground.setPreferredSize(new Dimension(670, 400));

        //---------- Setting Up Components ----------//
        JLabel lblPrompt = new JLabel("Please select an option below:");
        lblPrompt.setForeground(TEXT_COLOR);
        lblPrompt.setFont(PIXEL_FONT_XLARGE);
        lblPrompt.setHorizontalAlignment(JLabel.CENTER);

        JButton btnPlay = new JButton("Play");

        JButton btnOptions = new JButton("Options");

        JButton btnClose = new JButton("Exit");
        btnClose.addActionListener((ActionEvent) ->
        { dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); });

        //---------- Adding Components to Containers -----------//
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

        add(pnlBackground, BorderLayout.NORTH);
        add(pnlInput);
        setButtonEffects(pnlInput);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * Sets effects on all buttons.
     */
    private void setButtonEffects(Container container)
    {
        for (Component component : container.getComponents())
        {
            if (component instanceof JButton)
            {
                component.setFont(PIXEL_FONT_LARGE);
                ((JButton) component).setBorder(BUTTON_BORDER);
                ((JButton) component).setFocusPainted(false);
                ((JButton) component).setContentAreaFilled(false);
                ((JButton) component).setOpaque(true);
                component.setForeground(TEXT_COLOR);
                component.setBackground(BUTTON_COLOR);
                component.addMouseListener(new ButtonMouseListener());
            }
        }
    }

    /*
     * A MouseListener that highlights the foreground and background of the button when the mouse enters.
     */
    private static final class ButtonMouseListener implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                JButton source = (JButton)e.getSource();
                source.setBorder(BUTTON_BORDER_HIGHLIGHT);
                source.setBackground(GameMain.BUTTON_HIGHLIGHT_COLOR);
                source.setForeground(GameMain.TEXT_HIGHLIGHT_COLOR);
            }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                JButton source = (JButton)e.getSource();
                source.setBorder(BUTTON_BORDER);
                source.setBackground(GameMain.BUTTON_COLOR);
                source.setForeground(GameMain.TEXT_COLOR);
            }
        }
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
