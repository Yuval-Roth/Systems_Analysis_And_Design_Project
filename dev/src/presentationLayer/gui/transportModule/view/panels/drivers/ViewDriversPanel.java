package presentationLayer.gui.transportModule.view.panels.drivers;

import presentationLayer.gui.plAbstracts.AbstractModulePanel;
import presentationLayer.gui.plAbstracts.interfaces.Searchable;
import presentationLayer.gui.plUtils.ObservableList;
import presentationLayer.gui.plUtils.PrettyList;
import presentationLayer.gui.transportModule.control.DriversControl;

import java.awt.*;
import java.util.Comparator;

public class ViewDriversPanel extends AbstractModulePanel {

    private PrettyList driverList;
    private ObservableList emptyDriverList;
    private ObservableList<Searchable> drivers;

    public ViewDriversPanel(DriversControl control) {
        super(control);
        init();
    }

    private void init() {


        emptyDriverList = new ObservableList<>();
        control.getAll(this,emptyDriverList);
        drivers = emptyDriverList;
        drivers.sort(new Comparator<Searchable>() {
            @Override
            public int compare(Searchable o1, Searchable o2) {
                return o1.getShortDescription().compareTo(o2.getShortDescription());
            }
        });


        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;

        driverList = new PrettyList(drivers, panel);
        contentPanel.add(driverList.getComponent(), constraints);

    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);

        Dimension panelSize = panel.getPreferredSize();
        Dimension contentPanelSize = new Dimension((int) (panelSize.width * 0.8), (int) (panelSize.height * 0.9));
        contentPanel.setPreferredSize(contentPanelSize);

        driverList.componentResized(new Dimension((int) (contentPanelSize.width*0.8), (int) (contentPanelSize.height*0.9)));

        panel.revalidate();
    }

    @Override
    protected void clearFields() {
        // do nothing
    }
}
