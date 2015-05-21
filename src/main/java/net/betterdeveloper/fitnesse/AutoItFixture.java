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

    public int getErrorCode() {
        return autoIt.getError();
    }

    public String getVersion() {
        return autoIt.getVersion();
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

    public void closeWindow (String windowTitle) {
        lastWindowTitleManipulated = windowTitle;
        autoIt.winClose(windowTitle);
    }

    public void closeWindow () {
        autoIt.winClose(lastWindowTitleManipulated);
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
        //autoIt.clipPut(lastWindowTitleManipulated); //DEBUG
        return autoIt.winWait(lastWindowTitleManipulated, "", timeout);
    }

    public boolean waitForWindowCreated() {
        return waitForWindowCreated(lastWindowTitleManipulated);
    }

    public boolean waitForWindowActive(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        //autoIt.clipPut(lastWindowTitleManipulated); //DEBUG
        return autoIt.winWaitActive(lastWindowTitleManipulated, "", timeout);
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

    public String getTextInWindow(){
        return autoIt.winGetText(lastWindowTitleManipulated).replaceAll("(\\r|\\n)", ""); // see https://www.autoitscript.com/forum/topic/55945-getting-value-from-visible-text/
    }

    public boolean clickControlOfWindowWithButtonTimesXY(String controlId, String windowTitle, String button, int count, int x, int y) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button, count, x, y);
    }

    public boolean clickControlOfWindowWithButtonTimes(String controlId, String windowTitle, String button, int count) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button, count);
    }

    public boolean clickControlOfWindowWithButton(String controlId, String windowTitle, String button) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button);
    }

    public boolean clickControlOfWindow(String controlId, String windowTitle) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim());
    }

    public boolean clickControl(String controlId) {
        return clickControlOfWindow(controlId, lastWindowTitleManipulated);
    }

    public boolean clickControlWithButton(String controlId, String button) {
        return clickControlOfWindowWithButton(controlId, lastWindowTitleManipulated, button);
    }

    public void sendKeys (String keys){
        autoIt.send(keys, false);
    }

    public void sendRawKeys (String keys){
        autoIt.send(keys, true);
    }

    public boolean sendKeysToControl(String text, String controlId){
        return autoIt.controlSend(lastWindowTitleManipulated, "", controlId, text, false);
    }

    public boolean sendRawKeysToControl(String text, String controlId){
        return autoIt.controlSend(lastWindowTitleManipulated, "", controlId, text, true);
    }

    public void appendTextInControl(String text, String controlId){
        autoIt.controlCommandAddString(lastWindowTitleManipulated, "", controlId, text);
    }

    public boolean setTextInControl(String text, String controlId){
        return autoIt.controlSetText(lastWindowTitleManipulated, "", controlId, text);
    }

    public String getTextInControl(String controlId){
        return autoIt.controlGetText(lastWindowTitleManipulated, "", controlId);
    }

    public String getTextInStatusBar(String windowTitle){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.statusbarGetText(windowTitle);
    }

    public String getTextInStatusBar(){
        return autoIt.statusbarGetText(lastWindowTitleManipulated);
    }

    public String getTextInClipboard(){
        return autoIt.clipGet();
    }

    public void setTextInClipboard(String text){
        autoIt.clipPut(text);
    }

    public String getTabOfControlOfWindow (String controlId, String windowTitle) {
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlCommandCurrentTab(windowTitle,"", controlId);
    }

    public String getTabOfControl (String controlId) {
        return getTabOfControlOfWindow(controlId,lastWindowTitleManipulated);
    }

    public void goToTabRightOfControl (String controlId) {
        autoIt.controlCommandTabRight(lastWindowTitleManipulated, "", controlId);
    }

    public void goToTabLeftOfControl (String controlId) {
        autoIt.controlCommandTabLeft(lastWindowTitleManipulated, "", controlId);
    }

    public String executeCommandInControlOfWindowWithOption(String command, String controlId, String windowTitle, String option){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlCommandString(windowTitle, "", controlId, command, option);
    }

    public String executeCommandInControlWithOption(String command, String controlId, String option){
        return executeCommandInControlOfWindowWithOption(command, controlId, lastWindowTitleManipulated, option);
    }

    public String executeCommandInControl(String command, String controlId){
        return executeCommandInControlWithOption(command, controlId, "");
    }

    public boolean controlIsChecked (String controlId) {
        return autoIt.controlCommandIsChecked(lastWindowTitleManipulated, "", controlId);
    }

    public void checkControl (String controlId) {
        autoIt.controlCommandCheck(lastWindowTitleManipulated, "", controlId);
    }

    public boolean controlIsEnabled (String controlId) {
        return autoIt.controlCommandIsEnabled(lastWindowTitleManipulated, "", controlId);
    }

    public boolean controlIsVisible (String controlId) {
        return autoIt.controlCommandIsVisible(lastWindowTitleManipulated, "", controlId);
    }

    public void selectItemOfControl (String item, String controlId) {
        autoIt.controlCommandSelectString(lastWindowTitleManipulated, "", controlId, item);
    }

    public boolean focusOnControl (String controlId) {
        return autoIt.controlFocus(lastWindowTitleManipulated, "", controlId);
    }

    public String controlWithFocus() {
        return autoIt.controlGetFocus(lastWindowTitleManipulated);
    }

    public String executeCommandInListViewOfWindowWithOptionAndOption(String command, String controlId, String windowTitle, String option, String option2){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlListViewString(windowTitle, "", controlId, command, option, option2);
    }

    public String executeCommandInListViewWithOptionAndOption(String command, String controlId, String option, String option2){
        return executeCommandInListViewOfWindowWithOptionAndOption(command, controlId, lastWindowTitleManipulated, option, option2);
    }

    public int getItemCountOfListView (String controlId) {
        return autoIt.controlListViewGetItemCount(lastWindowTitleManipulated, "", controlId);
    }

    public int findItemAndSubitemOfListView (String item, String subItem, String controlId) {
        return autoIt.controlListViewFindItem(lastWindowTitleManipulated,"", controlId, item, subItem);
    }

    public int getSelectionCountOfListView (String controlId) {
        return autoIt.controlListViewGetSelectedCount(lastWindowTitleManipulated, "", controlId);
    }

    public String getSelectedItemOfListView (String controlId) {
        return autoIt.controlListViewGetSelected(lastWindowTitleManipulated, "", controlId);
    }

    public boolean isSelectionOfListView (String item, String controlId) {
        return autoIt.controlListViewIsSelected(lastWindowTitleManipulated,"", controlId, item);
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
        System.out.println(f.getHandleOfWindow("Calculator"));
        f.sendRawKeys("*7*3=");
        f.sendKeys("^c"); //copy
        System.out.println ("[ClassnameNN:#327701]=" + f.getTextInControl("[ClassnameNN:#327701]"));
        System.out.println(f.getErrorCode());
        System.out.println(f.getVersion());
        System.out.println("Clipboard=" + f.getTextInClipboard());
        String wText = f.getTextInWindow();
        System.out.println("Window text=" + wText);
        f.closeWindow();
    }

}
