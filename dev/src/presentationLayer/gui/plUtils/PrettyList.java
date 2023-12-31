package presentationLayer.gui.plUtils;

import javafx.util.Pair;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plAbstracts.interfaces.UIElement;
import presentationLayer.gui.transportModule.model.ObservableTruck;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class PrettyList implements UIElement {

    private final ObservableList<Searchable> models;
    private final Container repaintListener;
    private JScrollPane listPanel;
    private JList<Searchable> list;


    private DefaultListModel<Searchable> listModel;

    public PrettyList(ObservableList<Searchable> models , Container repaintListener) {
        this.models = models;
        this.repaintListener = repaintListener;
        init();
    }

    private void init(){

        listModel = new DefaultListModel<>();
        listModel.addAll(models);
        list = new JList<>(listModel);
        listPanel = new JScrollPane(list);

        list.setBackground(new Color(0,0,0,0));
        listPanel.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colors.getForegroundColor());
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(0,0,0,c.getHeight()-26);
                g2.drawArc(0,c.getHeight()-50,50,50,180,90);
                g2.drawLine(26,c.getHeight(),c.getWidth()-25,c.getHeight());
                g2.drawArc(c.getWidth()-50-1,c.getHeight()-50,50,50,270,90);
                g2.drawLine(c.getWidth()-1,c.getHeight()-26,c.getWidth()-1,0);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(0,20,20,20);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
        list.setBorder(new EmptyBorder(0,20,20,20));
        listPanel.setBackground(new Color(0,0,0,0));
        listPanel.setVerticalScrollBar(new PrettyScrollBar(160));
        listPanel.getVerticalScrollBar().setUnitIncrement(30);
        listPanel.getHorizontalScrollBar().setUnitIncrement(30);
        listPanel.getVerticalScrollBar().setBackground(new Color(0,0,0,0));

        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component r = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setFont(Fonts.textBoxFont);
//                int w = SwingUtilities.computeStringWidth(getFontMetrics(getFont()), value.toString());
                r.setPreferredSize(new Dimension(value.toString().length()*8,50));

                if(isSelected) {
                    setBackground(new Color(200,200,200,128));
                }

                setBorder(new Border() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(1));
                        g2.setColor(new Color(200, 200, 200, 100));
                        g2.drawLine(1, height - 1, width-1, height - 1);
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(0, 0, 0, 0);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return false;
                    }
                });
                repaintListener.repaint();
                return r;
            }
        });
    }

    public void addListSelectionListener(ListSelectionListener listener){
        list.addListSelectionListener(listener);
    }

    public JList<Searchable> getList(){
        return list;
    }
    public DefaultListModel<Searchable> getListModel() {
        return listModel;
    }


    @Override
    public Component getComponent() {
        return listPanel;
    }

    @Override
    public void componentResized(Dimension newSize) {
        listPanel.setPreferredSize(newSize);
    }
}
