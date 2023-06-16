package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.examples.JPanelWithBackground;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public abstract class Panel implements UIElement {

    protected final JPanel panel;
    protected final PopupMenuListener repaintListener;

    protected Panel(){
        panel = new JPanel();
        repaintListener = getRepaintListener();
    }
    protected Panel(String fileName){
        panel = new JPanelWithBackground(fileName);
        panel.paintComponents(panel.getGraphics());
        repaintListener = getRepaintListener();
    }

    public void add(Component component){
        panel.add(component);
    }

    public abstract Component getComponent();
    public void componentResized(Dimension newSize) {
        panel.setPreferredSize(new Dimension((int)(newSize.width*0.8),newSize.height));
        panel.revalidate();
    }

    private PopupMenuListener getRepaintListener(){
        return new PopupMenuListener() {
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {panel.repaint();}
            @Override public void popupMenuCanceled(PopupMenuEvent e) {}
        };
    }
}
