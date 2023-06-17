package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plAbstracts.interfaces.Panel;
import presentationLayer.gui.plAbstracts.interfaces.UIElement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class PanelManager implements UIElement {

    private final CardLayout cardLayout;
    private final JPanel container;
    private presentationLayer.gui.plAbstracts.interfaces.Panel currentPanel;
    private final LinkedList<presentationLayer.gui.plAbstracts.interfaces.Panel> panelsHistory = new LinkedList<>();

    public PanelManager(presentationLayer.gui.plAbstracts.interfaces.Panel startingPanel) {
        cardLayout = new CardLayout();
        container = new JPanel();
        container.setLayout(cardLayout);
        container.setLocation((int)(MainWindow.screenSize.width*0.2), 0);
        container.setBorder(new EmptyBorder(0,0,0,0));
        String name = startingPanel.getClass().getSimpleName();
        container.add(startingPanel.getComponent(),name);
        cardLayout.show(container,name);
        currentPanel = startingPanel;
    }

    public void setPanel(Panel panel) {

        if(currentPanel.getClass().getSimpleName().equals(panel.getClass().getSimpleName())) {
            return;
        }

        panelsHistory.push(currentPanel);
        currentPanel = panel;
        container.removeAll();
        String name = panel.getClass().getSimpleName();
        container.add(panel.getComponent(),name);
        cardLayout.show(container, name);
        container.validate();
        container.repaint();
    }

    public void previousPanel(){
        if(panelsHistory.size() > 0){
            setPanel(panelsHistory.pop());
        }
    }

    @Override
    public Component getComponent() {
        return container;
    }

    @Override
    public void componentResized(Dimension newSize) {
        container.setSize(new Dimension((int)(newSize.width*0.8)-10,newSize.height-20));
        currentPanel.componentResized(newSize);
        container.validate();
    }
}
