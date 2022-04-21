package advisor;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.addOption("featured", TaskExecutor::featured);
        menu.addOption("new", TaskExecutor::newTracks);
        menu.addOption("categories", TaskExecutor::categories);
        menu.addOption("playlists", TaskExecutor::playlists);
        menu.addOption("exit", s->menu.exit());
        menu.run();
    }
}
