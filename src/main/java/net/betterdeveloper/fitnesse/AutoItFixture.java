package net.betterdeveloper.fitnesse;

import java.io.File;
import autoitx4java.AutoItX;
import com.jacob.com.LibraryLoader;
/**
 * Don't forget to run AS ADMINISTRATOR: regsvr32 lib/AutoItX3_x64.dll
 */
public class AutoItFixture {

    private static String jvmBitVersion(){
        return System.getProperty("sun.arch.data.model");
    }
    static {
        String jacobDllVersionToUse;
        if (jvmBitVersion().contains("32")) jacobDllVersionToUse = "jacob-1.18-M2-x86.dll";
        else jacobDllVersionToUse = "jacob-1.18-M2-x64.dll";
        File file = new File("lib", jacobDllVersionToUse);
        if (file.exists()) System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
        else System.setProperty(LibraryLoader.JACOB_DLL_PATH, jacobDllVersionToUse);
        LibraryLoader.loadJacobLibrary();
    }

    private AutoItX autoIt = new AutoItX();
    private String lastWindowActivated = "";

    public AutoItFixture() {
    }

    public void startApp(String appPath) {
        autoIt.run(appPath);
    }

    public void activateWindow(String windowTitle) {
        autoIt.winActivate(windowTitle);
        this.lastWindowActivated = windowTitle;
    }

    public void waitForWindowActive(String windowTitle) {
        autoIt.winWaitActive(windowTitle);
    }
    public void waitForWindowActive() {
        waitForWindowActive(lastWindowActivated);
    }

    public void clickControlOfWindow(String controlId, String windowTitle) {
        autoIt.controlClick(windowTitle, "", controlId);
    }

    public void clickControlOfWindow(String controlId) {
        clickControlOfWindow(controlId, lastWindowActivated);
    }

    public static void main(String[] args) throws InterruptedException {
        AutoItFixture f = new AutoItFixture();
        f.startApp("calc.exe");
        f.activateWindow("Calculator");
        f.waitForWindowActive();
        //Enter 3 - by ID
        f.clickControlOfWindow("[ID:133]");
        //Enter +
        f.clickControlOfWindow("[ID:93]");
        //Enter 3 - by ClassnameNN
        f.clickControlOfWindow("[ClassnameNN:Button16]");
        //Enter =
        f.clickControlOfWindow("[ID:121]");
    }

}
