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

    public AutoItFixture() {
    }

    public void startApp(String appPath) {
        autoIt.run(appPath);
    }

    public void activateWindow(String windowTitle) {
        autoIt.winActivate(windowTitle);
    }

    public void waitForWindowActive(String windowTitle) {
        autoIt.winWaitActive(windowTitle);
    }

    public void clickControlOfWindow(String controlId, String windowTitle) {
        autoIt.controlClick(windowTitle, "", controlId);
    }

    public static void main(String[] args) throws InterruptedException {
        AutoItFixture f = new AutoItFixture();
        f.activateWindow("Calculator");
        f.waitForWindowActive("Calculator");
//Enter 3 - by ID
        f.clickControlOfWindow("[ID:133]", "Calculator") ;
        Thread.sleep(1000);
//Enter +
        f.clickControlOfWindow("[ID:93]", "Calculator") ;
        Thread.sleep(1000);
//Enter 3 - by ClassnameNN
        f.clickControlOfWindow("[ClassnameNN:Button16]", "Calculator") ;
        Thread.sleep(1000);
//Enter =
        f.clickControlOfWindow("[ID:121]", "Calculator") ;
    }

}
