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
    private String lastWindowTitleManipulated = "";
    int timeout = 10;

    public AutoItFixture() {
    }

    public String setWinWaitDelayTo(int new_delay) {
        return autoIt.autoItSetOption("WinWaitDelay", String.valueOf(new_delay));
    }

    public String setOptionTo (String option, String value) {
        return autoIt.autoItSetOption(option.trim(),value);
    }
    public int startApp(String appPath) {
        return autoIt.run(appPath);
    }

    public int startAppInWOrkingDir(String appPath, String workingDir) {
        return autoIt.run(appPath, workingDir);
    }

    public int startAppInWOrkingDirAndShowFlags(String appPath, String workingDir, int showFlag) {
        return autoIt.run(appPath, workingDir, showFlag);
    }

    public void setTimeout (int timeoutInSeconds)  {
        this.timeout = timeoutInSeconds;
    }

    public void activateWindow(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        autoIt.winActivate(windowTitle.trim()); // TODO: this always returns false. Why??
    }
    public void activateWindow() {
        activateWindow(lastWindowTitleManipulated);
    }

    public boolean waitForWindowCreated(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        return autoIt.winWait(windowTitle.trim(), "", timeout);
    }

    public boolean waitForWindowCreated() {
        return waitForWindowCreated(lastWindowTitleManipulated);
    }

    public boolean waitForWindowActive(String windowTitle) {
        return autoIt.winWaitActive(windowTitle.trim(), "", timeout);
    }

    public boolean waitForWindowActive() {
        return waitForWindowActive(lastWindowTitleManipulated);
    }

    public boolean waitForWindowReady(String windowTitle){
        boolean created = waitForWindowCreated(windowTitle);
        activateWindow(windowTitle);
        boolean isActive = waitForWindowActive(windowTitle);
        return created & isActive;
    }

    public String getHandleOfWindow(String windowTitle) {
        return autoIt.winGetHandle(windowTitle.trim());
    }

    public boolean clickControlOfWindow(String controlId, String windowTitle) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim());
    }

    public boolean clickControl(String controlId) {
        return clickControlOfWindow(controlId, lastWindowTitleManipulated);
    }

    public static void main(String[] args) throws InterruptedException {
        AutoItFixture f = new AutoItFixture();
        int pid = f.startApp("calc.exe");
        System.out.println(pid);
        String prevValue = f.setOptionTo("WinWaitDelay", "500");
        System.out.println(prevValue);
        boolean created = f.waitForWindowCreated("Calculator");
        System.out.println (created);
        f.activateWindow("Calculator");
        boolean isActive = f.waitForWindowActive();
        System.out.println (isActive);
        //Enter 3 - by ID
        f.clickControl("[ID:133]");
        //Enter +
        f.clickControl("[ID:93]");
        //Enter 3 - by ClassnameNN
        f.clickControl("[ClassnameNN:Button16]");
        //Enter =
        f.clickControl("[ID:121]");
        System.out.print(f.getHandleOfWindow("Calculator"));
    }

}
