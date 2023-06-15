package presentationLayer.gui.transportModule.panels.trucks;

import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.ScrollablePanel;

import java.awt.*;
import javax.swing.*;


public class UpdateTruckPanel extends ScrollablePanel {
    public UpdateTruckPanel() {
        super("src/resources/truck_main_page.jpg");
        init();
    }
    private void init() {

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setSize(scrollPane.getSize());
        JLabel header = new JLabel("Select truck to update::\n");
        header.setVerticalAlignment(JLabel.TOP);
        panel.add(header);

        //TODO: add the list of trucks to the panel


    }

}
