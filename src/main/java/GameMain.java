// Necessary imports.
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public final class GameMain extends JFrame
{
    //---------- Defining Class Constants -----------//
    private static final int BUTTON_BORDER_THICKNESS = 5;
    private static final int BUTTON_SHADOW_THICKNESS = 3;

    //---------- Defining Necessary GUI Components ----------//
    private ImagePanel pnlBackground;
    private final JButton btnApply;
    private final JCheckBox chkFullScreen;
    private final JComboBox<String> cmbResolutions;

    //---------- Defining Colors ----------//
    public static final Color BACKGROUND_COLOR = new Color(88, 88, 100);
    public static final Color TEXT_COLOR = new Color(235, 235, 215);
    public static final Color TEXT_HIGHLIGHT_COLOR = Color.WHITE;
    public static final Color BUTTON_COLOR = new Color(64, 64, 88);
    public static final Color BUTTON_BORDER_COLOR = new Color(55, 55, 64);
    public static final Color BUTTON_HIGHLIGHT_COLOR = new Color(50, 50, 62);
    public static final Color BUTTON_BORDER_HIGHLIGHT_COLOR = new Color(34, 34, 52);
    public static final Color BUTTON_SHADOW_COLOR = new Color(30, 30, 50);
    public static final Color BUTTON_SHADOW_HIGHLIGHT_COLOR = new Color(22, 22, 43);

    //---------- Defining Class Variables ---------//
    private int screenResolutionIndex = 0;
    private boolean fullScreen = false;

    //---------- Defining Fonts ----------//
    public static Font PIXEL_FONT_XLARGE;
    public static Font PIXEL_FONT_LARGE;

    //---------- Defining Borders ----------//
    public static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS,
                            BUTTON_SHADOW_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, BUTTON_BORDER_THICKNESS)
            ),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
    );
    public static final Border BUTTON_BORDER_HIGHLIGHT = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS,
                            BUTTON_SHADOW_HIGHLIGHT_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, BUTTON_BORDER_THICKNESS)
            ),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
    );
    public static final Border BUTTON_BORDER_CLICKED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS, 0, 0,
                            BACKGROUND_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, BUTTON_BORDER_THICKNESS)
            ),
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

        JPanel pnlOptions = new JPanel();
        pnlOptions.setBackground(BACKGROUND_COLOR);
        pnlOptions.setLayout(new GridBagLayout());

        pnlBackground.setBackground(BACKGROUND_COLOR);
        pnlBackground.setPreferredSize(new Dimension(670, 400));

        //---------- Setting Up Components ----------//
        JLabel lblPrompt = new JLabel("Please select an option below:");
        lblPrompt.setForeground(TEXT_COLOR);
        lblPrompt.setFont(PIXEL_FONT_XLARGE);
        lblPrompt.setHorizontalAlignment(JLabel.CENTER);

        btnApply = new JButton("Apply");
        btnApply.setEnabled(false);

        cmbResolutions = new JComboBox<>(getScreenResolutions(getGraphicsConfiguration().getDevice()));

        chkFullScreen = new JCheckBox("Full Screen?");
        chkFullScreen.setForeground(TEXT_COLOR);
        chkFullScreen.setBackground(BACKGROUND_COLOR);
        chkFullScreen.setFont(PIXEL_FONT_LARGE);
        chkFullScreen.setHorizontalAlignment(JLabel.CENTER);
        chkFullScreen.addChangeListener((ChangeEvent) ->
        {
            if (chkFullScreen.isSelected() != fullScreen)
                btnApply.setEnabled(true);
            else if (btnApply.isEnabled() && cmbResolutions.getSelectedIndex() == screenResolutionIndex)
                btnApply.setEnabled(false);
        });

        cmbResolutions.setForeground(TEXT_COLOR);
        cmbResolutions.setBackground(BACKGROUND_COLOR);
        cmbResolutions.setFont(PIXEL_FONT_LARGE);
        cmbResolutions.addItemListener((ItemEvent) ->
        {
            if (cmbResolutions.getSelectedIndex() != screenResolutionIndex)
                btnApply.setEnabled(true);
            else if (btnApply.isEnabled() && chkFullScreen.isSelected() == fullScreen)
                btnApply.setEnabled(false);
        });

        JButton btnPlay = new JButton("Play");

        JButton btnOptions = new JButton("Options");
        btnOptions.addActionListener((ActionEvent) ->
        {
            remove(pnlInput);
            add(pnlOptions);
            pack();
        });

        btnApply.addActionListener((ActionEvent) ->
        {
            fullScreen = chkFullScreen.isSelected();
            screenResolutionIndex = cmbResolutions.getSelectedIndex();
            btnApply.setEnabled(false);
            remove(pnlOptions);
            add(pnlInput);
            pack();
        });

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener((ActionEvent) ->
        {
            chkFullScreen.setSelected(fullScreen);
            cmbResolutions.setSelectedIndex(screenResolutionIndex);
            remove(pnlOptions);
            add(pnlInput);
            pack();
        });

        JButton btnClose = new JButton("Exit");
        btnClose.addActionListener((ActionEvent) ->
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

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

        constraints.weightx = 1f;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        pnlOptions.add(chkFullScreen, constraints);
        constraints.gridy = 1;
        pnlOptions.add(cmbResolutions, constraints);
        constraints.weightx = 0.5f;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        pnlOptions.add(btnBack, constraints);
        constraints.gridx = 1;
        pnlOptions.add(btnApply, constraints);

        add(pnlBackground, BorderLayout.NORTH);
        add(pnlInput);
        setButtonEffects(pnlInput);
        setButtonEffects(pnlOptions);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * Sets effects on all buttons.
     */
    private void setButtonEffects(@NotNull Container container)
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

    @NotNull
    private String[] getScreenResolutions(GraphicsDevice currentDevice)
    {
        HashSet<String> screenResolutionSet = new HashSet<>();
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int i;
        for (i = 0; i < devices.length; i++)
            if (devices[i].equals(currentDevice))
                break;

        DisplayMode[] modes = devices[i].getDisplayModes();
        for (DisplayMode mode : modes)
            screenResolutionSet.add(mode.getWidth() + "x" + mode.getHeight());
        String[] screenResolutions = screenResolutionSet.toArray(new String[0]);
        Arrays.sort(screenResolutions, (stringOne, stringTwo) ->
        {
            int integerOne = Integer.parseInt(stringOne.substring(0, stringOne.indexOf("x")));
            int integerTwo = Integer.parseInt(stringTwo.substring(0, stringTwo.indexOf("x")));
            return integerTwo - integerOne;
        });
        return screenResolutions;
    }

    /*
     * A MouseListener that highlights the foreground and background of the button when the mouse enters and clicks.
     */
    private static final class ButtonMouseListener implements MouseListener
    {
        private boolean isMouseInsideButton = false;

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                JButton source = (JButton) e.getSource();
                source.setBorder(BUTTON_BORDER_CLICKED);
                source.setLocation(source.getLocation().x + BUTTON_SHADOW_THICKNESS, source.getLocation().y
                        + BUTTON_SHADOW_THICKNESS);
            }
        }

        @Override
        public void mouseReleased(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                JButton source = (JButton) e.getSource();
                source.setLocation(source.getLocation().x - BUTTON_SHADOW_THICKNESS, source.getLocation().y
                        - BUTTON_SHADOW_THICKNESS);
                if (isMouseInsideButton)
                    mouseEntered(e);
                else
                    mouseExited(e);
            }
        }

        @Override
        public void mouseEntered(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                isMouseInsideButton = true;
                JButton source = (JButton) e.getSource();
                source.setBorder(BUTTON_BORDER_HIGHLIGHT);
                source.setBackground(GameMain.BUTTON_HIGHLIGHT_COLOR);
                source.setForeground(GameMain.TEXT_HIGHLIGHT_COLOR);
            }
        }

        @Override
        public void mouseExited(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton)
            {
                isMouseInsideButton = false;
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
