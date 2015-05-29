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

    /**
     * <p>Get the last error code occurred.</p>
     * <p><code>
     * | $errorCode= | get error code |
     * </code></p>
     *
     * @return  int The last error code occurred.
     */
    public int getErrorCode() {
        return autoIt.getError();
    }

    /**
     * <p>Get the AutoIt version.</p>
     * <p><code>
     * | $version= | get version |
     * </code></p>
     *
     * @return  String  The AutoIt version being used.
     */
    public String getVersion() {
        return autoIt.getVersion();
    }

    /**
     * <p>Changes the delay in milliseconds when waiting for a window</p>
     * <p><code>
     * | $previousDelay=| set win delay to | 500 |
     * </code></p>
     *
     * @param   newDelay  The new delay to use, in milliseconds
     * @return  int  The previous delay used, in milliseconds
     */
    public int  setWinWaitDelayTo(int newDelay) {
        return Integer.valueOf (autoIt.autoItSetOption("WinWaitDelay", String.valueOf(newDelay)));
    }

    /**
     * <p>Changes one of the various AutoIt options</p>
     * <p><code>
     * | $previousValue=| set option | ExpandEnvStrings | to | 1 |
     * </code></p>
     *
     * @param   optionName  The name of the option to change
     * @param   newValue    The new value for the given option
     * @return  String      The previous value used
     */
    public String setOptionTo (String optionName, String newValue) {
        return autoIt.autoItSetOption(optionName.trim(), newValue);
    }

    /**
     * <p>Starts a given application (EXE) controlling its show flag.
     * 4:  SW_SHOWNOACTIVATE, 3: SW_MAXIMIZE, 2: SW_SHOWMINIMIZED, 1: SW_SHOWNORMAL, 0: SW_HIDE,
     * 10: SW_SHOWDEFAULT, 7: SW_SHOWMINNOACTIVE </p>
     * <p><code>
     * | $pid= | start app | notepad.exe | with show flag | 10 |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @param   showFlag    The mode in which to show the app.
     * @return  int      The process ID
     */
    public int startAppWithShowFlag(String appPath, int showFlag) {
        return autoIt.run(appPath, "", showFlag);
    }

    /**
     * <p>Starts a given application (EXE) using as show flag SW_SHOW</p>
     * <p><code>
     * | $pid= | start app | notepad.exe |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @return  int      The process ID
     */
    public int startApp(String appPath) {
        return startAppWithShowFlag(appPath, AutoItX.SW_SHOW); // current default dir, show window
    }

    /**
     * <p>Starts a given application (EXE) using a specific working dir as as show flag SW_SHOW</p>
     * <p><code>
     * | $pid= | start app | notepad.exe | in working dir | c:\temp |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @param   workingDir  The path of the working directory
     * @return  int      The process ID
     */
    public int startAppInWorkingDir(String appPath, String workingDir) {
        return autoIt.run(appPath, workingDir);
    }

    /**
     * <p>Starts a given application (EXE) using a specific working dir and the given show flag</p>
     * <p><code>
     * | $pid= | start app | notepad.exe | in working dir | c:\temp | and show flag | 10 |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @param   workingDir  The path of the working directory
     * @param   showFlag    The mode in which to show the app.
     * @return  int      The process ID
     */
    public int startAppInWorkingDirAndShowFlag(String appPath, String workingDir, int showFlag) {
        return autoIt.run(appPath, workingDir, showFlag);
    }

    /**
     * <p>Sets the timeout to be used in APIs that may take a timeout value (in seconds)</p>
     * <p><code>
     * | set timeout | 500 |
     * </code></p>
     *
     * @param   timeoutInSeconds    The timeout, in seconds
     */
    public void setTimeout (int timeoutInSeconds)  {
        this.timeout = timeoutInSeconds;
    }

    /**
     * <p>Closes the given window</p>
     * <p><code>
     * | close window | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to close
     */
    public void closeWindow (String windowTitle) {
        lastWindowTitleManipulated = windowTitle;
        autoIt.winClose(windowTitle);
    }

    /**
     * <p>Closes the last window that was manipulated with any API that takes a window title</p>
     * <p><code>
     * | close window |
     * </code></p>
     *
     */
    public void closeWindow () {
        autoIt.winClose(lastWindowTitleManipulated);
    }

    /**
     * <p>Activates the given window</p>
     * <p><code>
     * | activate window | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to activate
     */
    public void activateWindow(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        autoIt.winActivate(windowTitle.trim()); // TODO: this always returns false. Why??
    }

    /**
     * <p>Activates the last window that was manipulated with any API that takes a window title</p>
     * <p><code>
     * | activate window |
     * </code></p>
     *
     */
    public void activateWindow() {
        activateWindow(lastWindowTitleManipulated);
    }

    /**
     * <p>Waits for the given window to be created</p>
     * <p><code>
     * | wait for window created | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to wait for
     */
    public boolean waitForWindowCreated(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        //autoIt.clipPut(lastWindowTitleManipulated); //DEBUG
        return autoIt.winWait(lastWindowTitleManipulated, "", timeout);
    }

    /**
     * <p>Waits for the creation of the last window that was manipulated with any API that takes a window title</p>
     * <p><code>
     * | wait for window created |
     * </code></p>
     *
     */
    public boolean waitForWindowCreated() {
        return waitForWindowCreated(lastWindowTitleManipulated);
    }

    /**
     * <p>Waits for the given window to be active</p>
     * <p><code>
     * | wait for window active | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to wait for
     */
    public boolean waitForWindowActive(String windowTitle) {
        this.lastWindowTitleManipulated = windowTitle.trim();
        //autoIt.clipPut(lastWindowTitleManipulated); //DEBUG
        return autoIt.winWaitActive(lastWindowTitleManipulated, "", timeout);
    }

    /**
     * <p>Waits for the activation of the last window that was manipulated with any API that takes a window title</p>
     * <p><code>
     * | wait for window active |
     * </code></p>
     *
     */
    public boolean waitForWindowActive() {
        return waitForWindowActive(lastWindowTitleManipulated);
    }

    /**
     * <p>Waits for the given window to be ready (created and active)</p>
     * <p><code>
     * | wait for window ready | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to wait for
     * @return  boolean        true if the window was created and is active
     */
    public boolean waitForWindowReady(String windowTitle){
        boolean created = waitForWindowCreated(windowTitle);
        activateWindow(windowTitle);
        boolean isActive = waitForWindowActive(windowTitle);
        return created && isActive;
    }

    /**
     * <p>Returns the low-level handle for the given window</p>
     * <p><code>
     * | $handle= | get handle of window | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to wait for
     * @return  String         The handle for the given window. An opaque value.
     */
    public String getHandleOfWindow(String windowTitle) {
        return autoIt.winGetHandle(windowTitle.trim());
    }

    /**
     * <p>Returns the text in the last window that was manipulated with any API that
     * takes a window title. All cr lf are removed</p>
     * <p><code>
     * | $text= | get text in window |
     * </code></p>
     *
     * @return  String         The handle for the given window. An opaque value.
     */
    public String getTextInWindow(){
        return autoIt.winGetText(lastWindowTitleManipulated).replaceAll("(\\r|\\n)", ""); // see https://www.autoitscript.com/forum/topic/55945-getting-value-from-visible-text/
    }

    /**
     * <p>Clicks the given widget (control) of the given window with the given mouse button,
     * a certain number of times and at relative coordinates X,Y</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | of window | MyApp | with button | left | times | 2 | x | 10 | y | 15 |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The ID of the window being clicked
     * @param button The name of the mouse button to click. AutoIt supports various values here, see the AutoIt docs.
     * @param count The number of times to click the mouse button (2 means double click, etc)
     * @param x     The relative horizontal offset inside the widget/control where to click
     * @param y     The relative vertical offset inside the widget/control where to click
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlOfWindowWithButtonTimesXY(String controlId, String windowTitle, String button, int count, int x, int y) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button, count, x, y);
    }

    /**
     * <p>Clicks the given widget (control) of the given window with the given mouse button,
     * a certain number of times</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | of window | MyApp | with button | left | times | 2 |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The ID of the window being clicked
     * @param button The name of the mouse button to click. AutoIt supports various values here, see the AutoIt docs.
     * @param count The number of times to click the mouse button (2 means double click, etc)
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlOfWindowWithButtonTimes(String controlId, String windowTitle, String button, int count) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button, count);
    }

    /**
     * <p>Clicks the given widget (control) of the given window with the given mouse button</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | of window | MyApp | with button | left |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The ID of the window being clicked
     * @param button The name of the mouse button to click. AutoIt supports various values here, see the AutoIt docs.
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlOfWindowWithButton(String controlId, String windowTitle, String button) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim(), button);
    }

    /**
     * <p>Clicks the given widget (control) of the given window with the left mouse button</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | of window | MyApp |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The ID of the window being clicked
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlOfWindow(String controlId, String windowTitle) {
        return autoIt.controlClick(windowTitle.trim(), "", controlId.trim());
    }

    /**
     * <p>Clicks the given widget (control) of the last window (that was manipulated with any API that
     * takes a window title) with the left mouse button</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControl(String controlId) {
        return clickControlOfWindow(controlId, lastWindowTitleManipulated);
    }

    /**
     * <p>Clicks the given widget (control) of the last window (that was manipulated with any API that
     * takes a window title) with the given mouse button</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | with button | left |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param button The name of the mouse button to click. AutoIt supports various values here, see the AutoIt docs.
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlWithButton(String controlId, String button) {
        return clickControlOfWindowWithButton(controlId, lastWindowTitleManipulated, button);
    }

    /**
     * <p>Clicks the given widget (control) of the last window (that was manipulated with any API that
     * takes a window title) with the left mouse button at the given offset</p>
     * <p><code>
     * | click control | [CLASS:TspDBGrid; INSTANCE:1] | at x | 12 | and y | 23 |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @param x     The relative horizontal offset inside the widget/control where to click
     * @param y     The relative vertical offset inside the widget/control where to click
     * @return  boolean    true if the click(s) requested was(were) successful
     */
    public boolean clickControlAtXAndY(String controlId, int x, int y) {
        return clickControlOfWindowWithButtonTimesXY(controlId, lastWindowTitleManipulated, "left", 1, x, y);
    }

    /**
     * <p>Sends the keystrokes to the current window. Note that some chars have special meaning: ^ for CONTROL etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     */
    public void sendKeys (String keys){
        autoIt.send(keys, false);
    }

    /**
     * <p>Sends the keystrokes to the current window. Note that all chars will be sent literally - no special interpretation of ^ etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send raw keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     */
    public void sendRawKeys (String keys){
        autoIt.send(keys, true);
    }

    /**
     * <p>Sends the keystrokes to the given control in the the last window (that was manipulated with any API that
     * takes a window title). Note that some chars have special meaning: ^ for CONTROL etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send keys | ^c | to control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param keys The keys to send to the control, simulating a user typing.
     * @return  boolean    true if the keys(s) sent was(were) successful
     */
    public boolean sendKeysToControl(String keys, String controlId){
        return autoIt.controlSend(lastWindowTitleManipulated, "", controlId, keys, false);
    }

    /**
     * <p>Sends the keystrokes to the given control in the the last window (that was manipulated with any API that
     * takes a window title). Note that all chars will be sent literally - no special interpretation of ^ etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send raw keys | ^c | to control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param keys The keys to send to the control, simulating a user typing.
     * @return  boolean    true if the keys(s) sent was(were) successful
     */
    public boolean sendRawKeysToControl(String keys, String controlId){
        return autoIt.controlSend(lastWindowTitleManipulated, "", controlId, keys, true);
    }

    /**
     * <p>Appends the text to the given control in the the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | append text | hello world | to control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param text The text to append to the control
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void appendTextInControl(String text, String controlId){
        autoIt.controlCommandAddString(lastWindowTitleManipulated, "", controlId, text);
    }

    /**
     * <p>Sets the text to the given control in the the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | set text | hello world | in control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param text The text to append to the control
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean    true if the text was appended successfully
     */
    public boolean setTextInControl(String text, String controlId){
        boolean ok =  autoIt.controlSetText(lastWindowTitleManipulated, "", controlId, text);
        if (!ok) { //https://www.autoitscript.com/forum/topic/124240-controlsettext-not-working-sometimes/
            ok = focusOnControl(controlId);
            if (ok) {
                ok = autoIt.controlSetText(lastWindowTitleManipulated, "", controlId, text);
            }
        }
        return ok;
    }

    /**
     * <p>Returns the text in the given control in the the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $text= | get text in control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being clicked. AutoIt supports various forms here, see the AutoIt docs.
     * @return  String  The text in the given control
     */
    public String getTextInControl(String controlId){
        return autoIt.controlGetText(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns the text in the status bar in the the given window</p>
     * <p><code>
     * | $text= | get text in status bar of window | Calculator |
     * </code></p>
     *
     * @param windowTitle The name of the window being manipulated
     * @return  String  The text in the status bar
     */
    public String getTextInStatusBarOfWindow(String windowTitle){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.statusbarGetText(windowTitle);
    }

    /**
     * <p>Returns the text in the status bar in the the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $text= | get text in status bar |
     * </code></p>
     *
     * @return  String  The text in the status bar
     */
    public String getTextInStatusBar(){
        return autoIt.statusbarGetText(lastWindowTitleManipulated);
    }

    /**
     * <p>Returns the text in the clipboard</p>
     * <p><code>
     * | $text= | get text in clipboard |
     * </code></p>
     *
     * @return  String  The text in the clipboard
     */
    public String getTextInClipboard(){
        return autoIt.clipGet();
    }

    /**
     * <p>Sets the text in the clipboard</p>
     * <p><code>
     * | set text in clipboard to | My text |
     * </code></p>
     *
     * @param  text  The new text in the clipboard
     */
    public void setTextInClipboardTo(String text){
        autoIt.clipPut(text);
    }

    /**
     * <p>Returns the Tab of the given control in the given window</p>
     * <p><code></p>
     * | $tab= | get tab of control | [CLASS:TspDBGrid; INSTANCE:1] | of window | Calculator |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The name of the window being manipulated
     * @return  String  The tab of the control in the window. An opaque value.
     */
    public String getTabOfControlOfWindow (String controlId, String windowTitle) {
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlCommandCurrentTab(windowTitle,"", controlId);
    }

    /**
     * <p>Returns the Tab of the given control in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $tab= | get tab of control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  String  The tab of the control in the window. An opaque value.
     */
    public String getTabOfControl (String controlId) {
        return getTabOfControlOfWindow(controlId,lastWindowTitleManipulated);
    }

    /**
     * <p>Go to the next tab (to the right) of the Tab of the given control in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | go to tab right of control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void goToTabRightOfControl (String controlId) {
        autoIt.controlCommandTabRight(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Go to the previous tab (to the left) of the Tab of the given control in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | go to tab left of control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void goToTabLeftOfControl (String controlId) {
        autoIt.controlCommandTabLeft(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and given window, with
     * the option as parameter.</p>
     * <p><code>
     * | execute command | EditPaste | in control | [CLASS:TspDBGrid; INSTANCE:1] | of window | Calculator | with option | text to paste |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The name of the window being manipulated
     * @param option The "extra stuff" to pass to the given @command.
     * @return String The result of the command executed - it depends on what command you requested.
     */
    public String executeCommandInControlOfWindowWithOption(String command, String controlId, String windowTitle, String option){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlCommandString(windowTitle, "", controlId, command, option);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and in the last window (that was manipulated with
     * any API that takes a window title), with the option as parameter.</p>
     * <p><code>
     * | execute command | EditPaste | in control | [CLASS:TspDBGrid; INSTANCE:1] | with option | text to paste |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param option The "extra stuff" to pass to the given @command.
     * @return String The result of the command executed - it depends on what command you requested.
     */
    public String executeCommandInControlWithOption(String command, String controlId, String option){
        return executeCommandInControlOfWindowWithOption(command, controlId, lastWindowTitleManipulated, option);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and in the last window (that was manipulated with
     * any API that takes a window title)</p>
     * <p><code>
     * | execute command | GetSelected | in control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return String The result of the command executed - it depends on what command you requested.
     */
    public String executeCommandInControl(String command, String controlId){
        return executeCommandInControlWithOption(command, controlId, "");
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and given window, with
     * the option as parameter.</p>
     * <p><code>
     * | execute boolean command | IsVisible | in control | [CLASS:TspDBGrid; INSTANCE:1] | of window | Calculator | with option | "" |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The name of the window being manipulated
     * @param option The "extra stuff" to pass to the given @command.
     * @return boolean The result of the command executed - it depends on what command you requested.
     */
    public boolean executeBooleanCommandInControlOfWindowWithOption(String command, String controlId, String windowTitle, String option){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlCommandBoolean(windowTitle, "", controlId, command, option);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and given window, with
     * the option as parameter.</p>
     * <p><code>
     * | execute boolean command | IsVisible | in control | [CLASS:TspDBGrid; INSTANCE:1] | of window | Calculator | with option | "" |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param option The "extra stuff" to pass to the given @command.
     * @return boolean The result of the command executed - it depends on what command you requested.
     */
    public boolean executeBooleanCommandInControlWithOption(String command, String controlId, String option){
        return executeBooleanCommandInControlOfWindowWithOption(command, controlId, lastWindowTitleManipulated, option);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given control and given window, with
     * the option as parameter.</p>
     * <p><code>
     * | execute boolean command | IsVisible | in control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return boolean The result of the command executed - it depends on what command you requested.
     */
    public boolean executeBooleanCommandInControl(String command, String controlId){
        return executeBooleanCommandInControlWithOption(command, controlId, "");
    }

    /**
     * <p>Returns true if the given control is checked - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | control is checked | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean  true if the control is checked
     */
    public boolean controlIsChecked (String controlId) {
        return autoIt.controlCommandIsChecked(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Check the given control - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | check control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void checkControl (String controlId) {
        autoIt.controlCommandCheck(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns true if the given control is enabled - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | control is enabled | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean  true if the control is enabled
     */
    public boolean controlIsEnabled (String controlId) {
        return autoIt.controlCommandIsEnabled(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns true if the given control is visible - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | control is visible | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean  true if the control is visible
     */
    public boolean controlIsVisible (String controlId) {
        return autoIt.controlCommandIsVisible(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Select a given item in the control - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | select item | London | of control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param item  The element to be selected
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void selectItemOfControl (String item, String controlId) {
        autoIt.controlCommandSelectString(lastWindowTitleManipulated, "", controlId, item);
    }

    /**
     * <p>Focus the given control - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | focus on control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean  true if the focus worked
     */
    public boolean focusOnControl (String controlId) {
        return autoIt.controlFocus(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Paste the given text in the given control - in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | paste | my text | in control | [CLASS:TspDBGrid; INSTANCE:1] |
     * </code></p>
     *
     * @param text  The text to paste
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     */
    public void pasteInControl (String text, String controlId) {
        autoIt.controlCommandEditPaste(lastWindowTitleManipulated,"",controlId, text);
    }

    /**
     * <p>Returns the control with focus in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $control= | get control with focus |
     * </code></p>
     *
     * @return  String  The control with focus. An opaque value.
     */
    public String getControlWithFocus() {
        return autoIt.controlGetFocus(lastWindowTitleManipulated);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given ListView control and given window, with
     * the options as parameters.</p>
     * <p><code>
     * | execute command | EditPaste | in list view | [CLASS:TspDBListView; INSTANCE:1] | of window | Calculator | with option | text to paste | and option | foobar |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param windowTitle The name of the window being manipulated
     * @param option The "extra stuff" to pass to the given @command.
     * @param option2 The "extra stuff" to pass to the given @command.
     * @return String The result of the command executed - it depends on what command you requested.
     */
    public String executeCommandInListViewOfWindowWithOptionAndOption(String command, String controlId, String windowTitle, String option, String option2){
        lastWindowTitleManipulated=windowTitle;
        return autoIt.controlListViewString(windowTitle, "", controlId, command, option, option2);
    }

    /**
     * <p>Low level API - check the AutoIt documentation under ControlCommand.
     * Executes the given command in the given ListView control in the last window
     * (that was manipulated with any API tha takes a window title), with
     * the options as parameters.</p>
     * <p><code>
     * | execute command | EditPaste | in list view | [CLASS:TspDBListView; INSTANCE:1] | with option | text to paste |
     * </code></p>
     *
     * @param command The command to execute (check the AUtoIt documentation).
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @param option The "extra stuff" to pass to the given @command.
     * @param option2 The "extra stuff" to pass to the given @command.
     * @return String The result of the command executed - it depends on what command you requested.
     */
    public String executeCommandInListViewWithOptionAndOption(String command, String controlId, String option, String option2){
        return executeCommandInListViewOfWindowWithOptionAndOption(command, controlId, lastWindowTitleManipulated, option, option2);
    }

    /**
     * <p>Returns the number of items in the given ListView in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $count= | get item count of list view | [CLASS:TspDBListView; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  int  The item count
     */
    public int getItemCountOfListView (String controlId) {
        return autoIt.controlListViewGetItemCount(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns the index of the given item/subitem of a ListView in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $index= | find item | England | and sub item | London | of list view | [CLASS:TspDBListView; INSTANCE:1] |
     * </code></p>
     *
     * @param item The item to find
     * @param subItem The subitem to find
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  int  The item index
     */
    public int findItemAndSubItemOfListView (String item, String subItem, String controlId) {
        return autoIt.controlListViewFindItem(lastWindowTitleManipulated,"", controlId, item, subItem);
    }

    /**
     * <p>Returns the number of items selected in the given ListView in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $count= | get item count of list view | [CLASS:TspDBListView; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  int  The item count
     */
    public int getSelectionCountOfListView (String controlId) {
        return autoIt.controlListViewGetSelectedCount(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns the selected item in the given ListView in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | $item= | get selected item of list view | [CLASS:TspDBListView; INSTANCE:1] |
     * </code></p>
     *
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  String  The item selected
     */
    public String getSelectedItemOfListView (String controlId) {
        return autoIt.controlListViewGetSelected(lastWindowTitleManipulated, "", controlId);
    }

    /**
     * <p>Returns true if the given item is selected in the given ListView in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | is selection | London | of list view | [CLASS:TspDBListView; INSTANCE:1] |
     * </code></p>
     *
     * @param item The item to check
     * @param controlId The ID of the control being passed. AutoIt supports various forms here, see the AutoIt docs.
     * @return  boolean  true if the given value passed is the selected item
     */
    public boolean isSelectionOfListView (String item, String controlId) {
        return autoIt.controlListViewIsSelected(lastWindowTitleManipulated,"", controlId, item);
    }

    /**
     * <p>Selects the given menu item in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | select menu item | Edit |
     * </code></p>
     *
     * @param menuItem The item to select
     * @return  boolean  true if the menu item was selected
     */
    public boolean selectMenuItem (String menuItem){
        return autoIt.winMenuSelectItem(lastWindowTitleManipulated, "", menuItem);
    }

    /**
     * <p>Selects the given menu item and subitem in the last window (that was manipulated with any API that
     * takes a window title).</p>
     * <p><code>
     * | select menu item | Edit | sub item | Paste |
     * </code></p>
     *
     * @param menuItem The item to select
     * @param subItem The subitem to select
     * @return  boolean  true if the menu item / subitem was selected
     */
    public boolean selectMenuItemSubitem (String menuItem, String subItem){
        return autoIt.winMenuSelectItem(lastWindowTitleManipulated, "", menuItem, subItem);
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
