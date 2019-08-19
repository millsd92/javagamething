package gameengine;

// Necessary imports.
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public final class GameMain extends JFrame
{
    //---------- Defining All Class Variables ----------//
    //<editor-fold desc="Defining All Class Variables">
    //---------- Defining Class Constants -----------//
    private static final int BUTTON_BORDER_THICKNESS = 5;
    private static final int BUTTON_SHADOW_THICKNESS = 3;
    private static final int TINY_BUTTON_BORDER_THICKNESS = 3;
    private static final int TINY_BUTTON_SHADOW_THICKNESS = 1;
    private static final int BUTTON_INSIDE_BORDER_THICKNESS = 10;

    //---------- Defining Necessary GUI Components ----------//
    private final JButton btnApply;
    private final JCheckBox chkFullScreen;
    private final JComboBox<String> cmbResolutions;

    //---------- Defining Colors ----------//
    private static final Color BACKGROUND_COLOR = new Color(88, 88, 100);
    private static final Color TEXT_COLOR = new Color(235, 235, 215);
    private static final Color TEXT_HIGHLIGHT_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(64, 64, 88);
    private static final Color SCROLLBAR_COLOR = new Color(60, 60, 76);
    private static final Color BUTTON_BORDER_COLOR = new Color(55, 55, 64);
    private static final Color BUTTON_HIGHLIGHT_COLOR = new Color(50, 50, 62);
    private static final Color BUTTON_BORDER_HIGHLIGHT_COLOR = new Color(34, 34, 52);
    private static final Color BUTTON_SHADOW_COLOR = new Color(30, 30, 50);
    private static final Color BUTTON_SHADOW_HIGHLIGHT_COLOR = new Color(22, 22, 43);

    //---------- Defining Variables ---------//
    private int screenResolutionIndex = 0;
    private boolean fullScreen = false;
    private GraphicsDevice currentDevice = getGraphicsConfiguration().getDevice();

    //---------- Defining Fonts ----------//
    private static Font PIXEL_FONT_XLARGE;
    static Font PIXEL_FONT_LARGE;

    //---------- Defining Borders ----------//
    private static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS,
                            BUTTON_SHADOW_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, BUTTON_BORDER_THICKNESS)
            ),
            BorderFactory.createEmptyBorder(BUTTON_INSIDE_BORDER_THICKNESS, 0, BUTTON_INSIDE_BORDER_THICKNESS,
                    0)
    );
    private static final Border BUTTON_BORDER_HIGHLIGHT = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS,
                            BUTTON_SHADOW_HIGHLIGHT_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, BUTTON_BORDER_THICKNESS)
            ),
            BorderFactory.createEmptyBorder(BUTTON_INSIDE_BORDER_THICKNESS, 0, BUTTON_INSIDE_BORDER_THICKNESS,
                    0)
    );
    private static final Border BUTTON_BORDER_CLICKED = BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_THICKNESS,
                            0, 0, BACKGROUND_COLOR),
                    BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, BUTTON_BORDER_THICKNESS)
            ),
            BorderFactory.createEmptyBorder(BUTTON_INSIDE_BORDER_THICKNESS, 0, BUTTON_INSIDE_BORDER_THICKNESS,
                    0)
    );
    private static final Border TINY_BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, TINY_BUTTON_SHADOW_THICKNESS,
                    TINY_BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_COLOR),
            BorderFactory.createLineBorder(BUTTON_BORDER_COLOR, TINY_BUTTON_BORDER_THICKNESS)
    );
    private static final Border TINY_BUTTON_BORDER_HIGHLIGHT = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, TINY_BUTTON_SHADOW_THICKNESS,
                    TINY_BUTTON_SHADOW_THICKNESS, BUTTON_SHADOW_HIGHLIGHT_COLOR),
            BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, TINY_BUTTON_BORDER_THICKNESS)
    );
    private static final Border TINY_BUTTON_BORDER_CLICKED = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(TINY_BUTTON_SHADOW_THICKNESS,
                    TINY_BUTTON_SHADOW_THICKNESS, 0, 0, BACKGROUND_COLOR),
            BorderFactory.createLineBorder(BUTTON_BORDER_HIGHLIGHT_COLOR, TINY_BUTTON_BORDER_THICKNESS)
    );
    //</editor-fold>

    private GameMain()
    {
        //---------- Setting Up Default Frame Attributes ----------//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Welcome to the Game...");
        if (Toolkit.getDefaultToolkit().getScreenSize().height >= 1080)
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit()
                    .getImage("javagamething" + File.separator + "src" + File.separator + "resources"
                            + File.separator + "images" + File.separator + "large-cursor.png"),
                    new Point(getX(), getY()), "CustomCursor"));
        else
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit()
                            .getImage("javagamething" + File.separator + "src" + File.separator + "resources"
                                    + File.separator + "images" + File.separator + "regular-cursor.png"),
                    new Point(getX(), getY()), "CustomCursor"));

        //---------- Retrieving Background Image ----------//
        //<editor-fold desc="Retrieving Background Image">
        JLabel lblBackground = new JLabel(new ImageIcon("javagamething" + File.separator
                + "src" + File.separator + "resources" + File.separator + "images" + File.separator
                + "main-menu-logo.gif"));
        lblBackground.setOpaque(true);
        lblBackground.setBackground(BACKGROUND_COLOR);
        //</editor-fold>

        //---------- Setting Up Font ----------//
        //<editor-fold desc="Setting Up Font">
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
        //</editor-fold>

        //---------- Setting Up Containers ----------//
        //<editor-fold desc="Setting Up Containers">
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel pnlInput = new JPanel();
        pnlInput.setBackground(BACKGROUND_COLOR);
        pnlInput.setLayout(new GridBagLayout());

        JPanel pnlOptions = new JPanel();
        pnlOptions.setBackground(BACKGROUND_COLOR);
        pnlOptions.setLayout(new GridBagLayout());

        lblBackground.setBackground(BACKGROUND_COLOR);
        lblBackground.setPreferredSize(new Dimension(670, 400));

        //</editor-fold>

        //---------- Setting Up Components ----------//
        //<editor-fold desc="Setting Up Components">
        JLabel lblPrompt = new JLabel("Please select an option below:");
        lblPrompt.setForeground(TEXT_COLOR);
        lblPrompt.setFont(PIXEL_FONT_XLARGE);
        lblPrompt.setHorizontalAlignment(JLabel.CENTER);

        btnApply = new JButton("Apply");
        btnApply.setEnabled(false);

        cmbResolutions = new JComboBox<>(getScreenResolutions(currentDevice));

        CheckBoxIcon icon = new CheckBoxIcon();

        chkFullScreen = new JCheckBox("Full Screen?", icon);
        int checkboxGap = 10;
        chkFullScreen.setIconTextGap(checkboxGap);
        chkFullScreen.setForeground(TEXT_COLOR);
        chkFullScreen.setBackground(BACKGROUND_COLOR);
        chkFullScreen.setFont(PIXEL_FONT_LARGE);
        chkFullScreen.setHorizontalAlignment(JCheckBox.CENTER);
        chkFullScreen.setFocusPainted(false);
        if (!currentDevice.isFullScreenSupported())
            chkFullScreen.setEnabled(false);
        chkFullScreen.addChangeListener((ChangeEvent) ->
        {
            if (chkFullScreen.isSelected() != fullScreen)
                btnApply.setEnabled(true);
            else if (btnApply.isEnabled() && cmbResolutions.getSelectedIndex() == screenResolutionIndex)
                btnApply.setEnabled(false);
        });

        CustomMouseListener mouseListener = new CustomMouseListener();
        mouseListener.isTiny = true;
        cmbResolutions.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus)
            {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if (isSelected)
                {
                    setBackground(BUTTON_HIGHLIGHT_COLOR);
                    setForeground(TEXT_HIGHLIGHT_COLOR);
                }
                else
                {
                    setBackground(BUTTON_COLOR);
                    setForeground(TEXT_COLOR);
                }
                return component;
            }
        });
        cmbResolutions.setUI(new CustomComboBoxUI());
        cmbResolutions.addMouseListener(mouseListener);
        cmbResolutions.addItemListener((ItemEvent) ->
        {
            if (cmbResolutions.getSelectedIndex() != screenResolutionIndex)
                btnApply.setEnabled(true);
            else if (btnApply.isEnabled() && chkFullScreen.isSelected() == fullScreen)
                btnApply.setEnabled(false);
        });

        JButton btnPlay = new JButton("Play");
        btnPlay.addActionListener((ActionEvent) ->
        {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            DisplayMode displayMode = getSelectedDisplayMode();
            Game game = new Game(displayMode, currentDevice, fullScreen);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            game.setVisible(true);
        });

        JButton btnOptions = new JButton("Options");
        btnOptions.addActionListener((ActionEvent) ->
        {
            remove(pnlInput);
            add(pnlOptions);
            ((CustomMouseListener) btnOptions.getMouseListeners()[1]).isMouseInsideButton = false;
            pack();
        });

        btnApply.addActionListener((ActionEvent) ->
        {
            fullScreen = chkFullScreen.isSelected();
            screenResolutionIndex = cmbResolutions.getSelectedIndex();
            btnApply.setEnabled(false);
            remove(pnlOptions);
            add(pnlInput);
            ((CustomMouseListener) btnApply.getMouseListeners()[1]).isMouseInsideButton = false;
            pack();
        });

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener((ActionEvent) ->
        {
            chkFullScreen.setSelected(fullScreen);
            cmbResolutions.setSelectedIndex(screenResolutionIndex);
            remove(pnlOptions);
            add(pnlInput);
            ((CustomMouseListener) btnBack.getMouseListeners()[1]).isMouseInsideButton = false;
            pack();
        });

        JButton btnClose = new JButton("Exit");
        btnClose.addActionListener((ActionEvent) ->
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        //</editor-fold>

        //---------- Adding Components to Containers -----------//
        //<editor-fold desc="Adding Components to Containers">
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

        add(lblBackground, BorderLayout.NORTH);
        add(pnlInput);
        setCustomEffects(pnlInput);
        setCustomEffects(pnlOptions);
        pack();
        setLocationRelativeTo(null);
        //</editor-fold>
    }

    /*
     * Sets effects on all buttons and combo boxes.
     */
    private void setCustomEffects(@NotNull Container container)
    {
        for (Component component : container.getComponents())
        {
            if (component instanceof JButton || component instanceof JComboBox)
            {
                component.setFont(PIXEL_FONT_LARGE);
                if (component instanceof JButton)
                    ((JComponent) component).setBorder(BUTTON_BORDER);
                else
                    ((JComboBox) component).setBorder(TINY_BUTTON_BORDER);
                if (component instanceof JButton)
                {
                    ((JButton) component).setFocusPainted(false);
                    ((JButton) component).setContentAreaFilled(false);
                    component.addMouseListener(new CustomMouseListener());
                }
                ((JComponent) component).setOpaque(true);
                component.setForeground(TEXT_COLOR);
                component.setBackground(BUTTON_COLOR);
            }
        }
    }

    /*
     * A specified overloaded version of the setCustomEffects method.
     */
    private static void setCustomEffects(@NotNull JButton button)
    {
        CustomMouseListener tinyCustomMouseListener = new CustomMouseListener();
        tinyCustomMouseListener.isTiny = true;
        button.setBorder(TINY_BUTTON_BORDER);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(BUTTON_COLOR);
        button.addMouseListener(tinyCustomMouseListener);
    }

    /*
     * Using the graphics environment, determines what display is the main one and what resolutions are available
     * for that display.
     */
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
            if (integerTwo - integerOne != 0)
                return integerTwo - integerOne;
            else
            {
                integerOne = Integer.parseInt(stringOne.substring(stringOne.indexOf("x") + 1));
                integerTwo = Integer.parseInt(stringTwo.substring(stringTwo.indexOf("x") + 1));
                return integerTwo - integerOne;
            }
        });
        return screenResolutions;
    }

    /*
     * Gets the display mode selected in the combo box.
     */
    private DisplayMode getSelectedDisplayMode()
    {
        String resolutionString = cmbResolutions.getItemAt(screenResolutionIndex);
        int screenWidth = Integer.parseInt(resolutionString.substring(0, resolutionString.indexOf("x")));
        int screenHeight = Integer.parseInt(resolutionString.substring(resolutionString.indexOf("x") + 1));

        ArrayList<DisplayMode> modes = new ArrayList<>();
        for (DisplayMode mode : currentDevice.getDisplayModes())
            if (mode.getWidth() == screenWidth && mode.getHeight() == screenHeight)
                modes.add(mode);

        DisplayMode bestMode = modes.get(0);
        for (DisplayMode mode : modes)
            if (mode.getBitDepth() > bestMode.getBitDepth())
                bestMode = mode;

        return bestMode;
    }

    /*
     * A MouseListener that highlights the foreground and background of the button when the mouse enters and clicks.
     */
    private static final class CustomMouseListener implements MouseListener
    {
        private boolean isMouseInsideButton = false;
        private boolean isTiny = false;

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton || e.getSource() instanceof JComboBox)
            {
                JComponent source = (JComponent) e.getSource();
                if (!isTiny)
                {
                    source.setBorder(BUTTON_BORDER_CLICKED);
                    source.setLocation(source.getLocation().x + BUTTON_SHADOW_THICKNESS, source.getLocation().y
                            + BUTTON_SHADOW_THICKNESS);
                }
                else
                {
                    source.setBorder(TINY_BUTTON_BORDER_CLICKED);
                    source.setLocation(source.getLocation().x + TINY_BUTTON_SHADOW_THICKNESS,
                            source.getLocation().y + TINY_BUTTON_SHADOW_THICKNESS);
                }
            }
        }

        @Override
        public void mouseReleased(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton || e.getSource() instanceof JComboBox)
            {
                JComponent source = (JComponent) e.getSource();
                if (!isTiny)
                    source.setLocation(source.getLocation().x - BUTTON_SHADOW_THICKNESS, source.getLocation().y
                            - BUTTON_SHADOW_THICKNESS);
                else
                    source.setLocation(source.getLocation().x - TINY_BUTTON_SHADOW_THICKNESS,
                            source.getLocation().y - TINY_BUTTON_SHADOW_THICKNESS);
                if (isMouseInsideButton)
                    mouseEntered(e);
                else
                    mouseExited(e);
            }
        }

        @Override
        public void mouseEntered(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton || e.getSource() instanceof JComboBox)
            {
                isMouseInsideButton = true;
                JComponent source = (JComponent) e.getSource();
                if (!isTiny)
                    source.setBorder(BUTTON_BORDER_HIGHLIGHT);
                else
                    source.setBorder(TINY_BUTTON_BORDER_HIGHLIGHT);
                source.setBackground(GameMain.BUTTON_HIGHLIGHT_COLOR);
                source.setForeground(GameMain.TEXT_HIGHLIGHT_COLOR);
            }
        }

        @Override
        public void mouseExited(@NotNull MouseEvent e)
        {
            if (e.getSource() instanceof JButton || e.getSource() instanceof JComboBox)
            {
                isMouseInsideButton = false;
                JComponent source = (JComponent) e.getSource();
                if (!isTiny)
                    source.setBorder(BUTTON_BORDER);
                else
                    source.setBorder(TINY_BUTTON_BORDER);
                source.setBackground(GameMain.BUTTON_COLOR);
                source.setForeground(GameMain.TEXT_COLOR);
            }
        }
    }

    /*
     * A private, inner class that allows for a custom checkbox image.
     */
    private static final class CheckBoxIcon implements Icon
    {
        private ImageIcon unchecked;
        private ImageIcon checked;
        private static final int IMAGE_SIZE = 21;

        private CheckBoxIcon()
        {
            unchecked = new ImageIcon("javagamething" + File.separator + "src"
                    + File.separator + "resources" + File.separator + "images" + File.separator
                    + "unchecked.png");
            checked = new ImageIcon("javagamething" + File.separator + "src"
                    + File.separator + "resources" + File.separator + "images" + File.separator
                    + "checked.png");
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y)
        {
            ButtonModel model = ((AbstractButton) component).getModel();
            ImageIcon currentIcon = (model.isSelected() ? checked : unchecked);
            graphics.drawImage(currentIcon.getImage(), x, y, null);
        }

        @Override
        public int getIconWidth()
        {
            return IMAGE_SIZE;
        }

        @Override
        public int getIconHeight()
        {
            return IMAGE_SIZE;
        }
    }

    /*
     * Custom UI for the JComboBox with a custom arrow button.
     */
    private static final class CustomComboBoxUI extends BasicComboBoxUI
    {
        @NotNull
        @Override
        public JButton createArrowButton()
        {
            JButton arrowButton = new JButton(new ImageIcon("javagamething" + File.separator + "src"
                    + File.separator + "resources" + File.separator + "images" + File.separator
                    + "comboboxarrow.png"));
            GameMain.setCustomEffects(arrowButton);

            return arrowButton;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus)
        { } // Don't do a thing...

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus)
        { super.paintCurrentValue(g, bounds, false); } // I don't care if it has focus...

        @Override
        protected ComboPopup createPopup()
        {
            return new BasicComboPopup(comboBox)
            {
                @Override
                protected JScrollPane createScroller()
                {
                    JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setBackground(SCROLLBAR_COLOR);
                    scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI()
                    {
                        private ImageIcon getArrow()
                        {
                            return new ImageIcon("javagamething" + File.separator + "src"
                                    + File.separator + "resources" + File.separator + "images" + File.separator
                                    + "listarrow.png");
                        }

                        private void setButtonEffects(JButton button)
                        {
                            button.setBackground(BUTTON_HIGHLIGHT_COLOR);
                            button.setFocusPainted(false);
                            button.setContentAreaFilled(false);
                            button.setOpaque(true);
                            button.setBorder(BorderFactory.createEmptyBorder());
                        }

                        @Override
                        protected JButton createDecreaseButton(int orientation)
                        {
                            JButton decreaseButton = new JButton(getArrow());
                            setButtonEffects(decreaseButton);
                            return decreaseButton;
                        }

                        @Override
                        protected JButton createIncreaseButton(int orientation)
                        {
                            ImageIcon arrow = new ImageIcon("javagamething" + File.separator + "src"
                                    + File.separator + "resources" + File.separator + "images" + File.separator
                                    + "listarrow.png") {
                                @Override
                                public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                                    Graphics2D graphics2D = (Graphics2D) g.create();
                                    graphics2D.translate(0, getIconHeight());
                                    graphics2D.scale(1, -1);
                                    super.paintIcon(c, graphics2D, x, y);
                                }
                            };
                            JButton increaseButton = new JButton(arrow);
                            setButtonEffects(increaseButton);
                            return increaseButton;
                        }

                        @Override
                        public Dimension getPreferredSize(JComponent c)
                        { return new Dimension(createArrowButton().getPreferredSize()); }

                        @Override
                        protected void configureScrollBarColors()
                        { this.thumbColor = BUTTON_HIGHLIGHT_COLOR; }
                    });

                    return scrollPane;
                }
            };
        }
    }

    // The entry-point of the application.
    public static void main(String[] args)
    { SwingUtilities.invokeLater(() -> new GameMain().setVisible(true)); }
}
