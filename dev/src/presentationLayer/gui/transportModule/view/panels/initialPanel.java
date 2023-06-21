package presentationLayer.gui.transportModule.view.panels;

import presentationLayer.gui.plAbstracts.AbstractTransportModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.ObservableModel;

import javax.swing.*;
import java.awt.*;

public class initialPanel extends AbstractTransportModulePanel {

    public initialPanel(){
        super(null);
        init();
    }

    private void init(){

        GridBagConstraints constraints = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome To The Transport Module!");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;
        constraints.insets = new Insets(50, 5, 5, 5);
        panel.add(welcomeLabel,constraints);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        welcomeLabel.setForeground(new Color(255, 255, 255));
    }

    @Override
    protected void clearFields() {
        
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        panel.revalidate();
    }

    
}
