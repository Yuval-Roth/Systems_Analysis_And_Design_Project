package presentationLayer.gui.plUtils;

import javafx.util.Pair;
import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuickAccess implements UIElement {
    private final List<Pair<String, List<Link>>> links;
    private final JPanel panel;
    private final JScrollPane scrollPane;

    public QuickAccess(){
        links = new LinkedList<>();
        panel = new JPanel();
        scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Colors.getTrasparentForegroundColor());
                g.fillRect(x,y,width,height);
            }
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0,0,0,10);
            }
            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
        scrollPane.setVerticalScrollBar(new PrettyScrollBar(360));
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);

        panel.setBorder(new EmptyBorder(0,7,30,0));
        panel.setBackground(Colors.getBackgroundColor());

    }

    public QuickAccess addCategory(String name, Link ... links){
        this.links.add(new Pair<>(name, Arrays.asList(links)));
        return this;
    }

    public List<Pair<String, List<Link>>> getLinks(){
        return links;
    }

    @Override
    public Component getComponent() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int i = 0;
        gbc.gridy = 0;
        for (Pair<String, List<Link>> link : links) {
            JLabel header = new JLabel(link.getKey());
            header.setBorder(new EmptyBorder(0,20,5,0));
            header.setUI(new BasicLabelUI(){
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setPaint(Colors.getForegroundColor());
                    g2.setStroke(new BasicStroke(5));
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                    g2.fillOval(0,c.getHeight()/4, c.getHeight()/2-5, c.getHeight()/2-5);
                    g2.drawLine(0,c.getHeight(),panel.getWidth(),c.getHeight());
                    super.paint(g2, c);
                }
            });

            header.setFont(header.getFont().deriveFont(Font.BOLD).deriveFont(20f));
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.ipadx = 25;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(header, gbc);
            gbc.gridy = ++i;


            gbc.insets = new Insets(0, 0, 15, 0);
            gbc.ipadx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            for (Link l : link.getValue()) {
                panel.add(l.getComponent(), gbc);
                gbc.gridy = ++i;
            }
        }
        return scrollPane;
    }

    @Override
    public void componentResized(Dimension newSize) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setSize((int)(d.width*0.25),(int)(newSize.getHeight()));
        scrollPane.revalidate();
    }
}
