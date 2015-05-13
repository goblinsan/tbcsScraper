package mobileTbcs.util;

import java.io.File;

public class ResourceFinder {
    public ResourceFinder(){
        ClassLoader loader = this.getClass().getClassLoader();
        String webDir = loader.getResource("mobileTbcs/webUi/HtmlPage.class").toExternalForm();
        File indexLoc = new File(loader.getResource("mobileTbcs/webUi/HtmlPage.class").getFile());
        System.out.println(webDir);
    }
}
