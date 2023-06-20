package presentationLayer.cli.employeeModule.View;

public class MenuManager {
    private Menu menu;
    private static boolean finished;

    public MenuManager() {
        finished = false;
        menu = new LoginMenu();
    }

    public void run() {
        while (!finished) {
            menu = menu.run();
        }
        System.exit(0);
    }

    public static void terminate() {
        finished = true;
    }

    public static void main(String[] args) {
        new MenuManager().run();
    }
}
