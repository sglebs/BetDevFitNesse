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

    public static void main(String[] args) throws InterruptedException {
        AutoItFixture f = new AutoItFixture();
        AutoItX x = f.autoIt;
        x.run("calc.exe");
        x.winActivate("Calculator");
        x.winWaitActive("Calculator");
//Enter 3
        x.controlClick("Calculator", "", "133") ;
        Thread.sleep(1000);
//Enter +
        x.controlClick("Calculator", "", "93") ;
        Thread.sleep(1000);
//Enter 3
        x.controlClick("Calculator", "", "133") ;
        Thread.sleep(1000);
//Enter =
        x.controlClick("Calculator", "", "121") ;
    }

}
