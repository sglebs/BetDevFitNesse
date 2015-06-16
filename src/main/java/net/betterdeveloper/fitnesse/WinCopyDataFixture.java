package net.betterdeveloper.fitnesse;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class implements a fixture that uses WM_COPYDATA to talk to an application
 * and get results back. The idea is to use AutoIt-style manipulation but via
 * bi-directional WM_COPYDATA messages sent back and forth.
 */
public class WinCopyDataFixture {

    private static class WinCopyDataBridge { // this is what the DLL needs to implement. We have a dummy impl here.

        protected JSONObject executeCommand (String jsonRequest) {
            System.out.println (jsonRequest);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonRequest);
            } catch (JSONException e) {
                throw new RuntimeException("Unable to interpret request", e);
            }
            return new JSONObject("{\"result\":true}");
        }
    }
    WinCopyDataBridge bridge = new WinCopyDataBridge();

    private String lastWindowTitleManipulated = "";


    /**
     * <p>Starts a given application (EXE) using as show flag SW_SHOW</p>
     * <p><code>
     * | $p= | start app | notepad.exe |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @return  String      The process description
     */
    public String startApp(String appPath) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec (appPath);
        return p.toString();
    }

    /**
     * <p>Starts a given application (EXE) with the given parameters</p>
     * <p><code>
     * | $p= | start app | notepad.exe | with parameters | c:\temp\myfile.txt |
     * </code></p>
     *
     * @param   appPath     The path to the executable to launch.
     * @param   parameters  The extra parameters to pass
     * @return  String      The process description
     */
    public String startAppWithParameters(String appPath, String parameters) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(appPath, parameters);
        Process p = pb.start();
        return p.toString();
    }


    /**
     * <p>Sets the timeout to be used in APIs that may take a timeout value (in seconds)</p>
     * <p><code>
     * | set timeout | 5 |
     * </code></p>
     *
     * @param   timeoutInSeconds    The timeout, in seconds
     */
    public void setTimeout (int timeoutInSeconds)  {
        bridge.executeCommand (String.format("{\"op\":\"setTimeout\",\"params\":[%d]}", timeoutInSeconds));
    }


    /**
     * <p>Waits for the given window to be created and opened</p>
     * <p><code>
     * | wait for window created | Calculator |
     * </code></p>
     *
     * @param   windowTitle    The title of the window to wait for
     */
    public boolean waitForWindowOpened(String windowTitle) {
        JSONObject result = bridge.executeCommand (String.format("{\"op\":\"waitForWindowOpened\",\"params\":[\"%s\"]}", windowTitle));
        return result.getBoolean("result");
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
        JSONObject result = bridge.executeCommand (String.format("{\"op\":\"clickControlOfWindow\",\"params\":[\"%s\",\"s\"]}", controlId, windowTitle));
        return result.getBoolean("result");
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
     * <p>Sends the keystrokes to the current window. Note that some chars have special meaning: ^ for CONTROL etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     * @param windowTitle The ID of the window being sent keys
     * @return  boolean    true if the sendKey requested was successful
     */
    public boolean sendKeysToWindow (String keys, String windowTitle){
        JSONObject result = bridge.executeCommand (String.format("{\"op\":\"sendKeysToWindow\",\"params\":[\"%s\",\"s\"]}", keys, windowTitle));
        return result.getBoolean("result");

    }

    /**
     * <p>Sends the keystrokes to the current window. Note that some chars have special meaning: ^ for CONTROL etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     * @return  boolean    true if the sendKey requested was successful
     */
    public boolean sendKeys (String keys){
        return sendKeysToWindow(keys, lastWindowTitleManipulated);
    }

    /**
     * <p>Sends the keystrokes to the current window. Note that all chars will be sent literally - no special interpretation of ^ etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send raw keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     * @param windowTitle The ID of the window being sent keys
     * @return  boolean    true if the sendKey requested was successful
     */
    public boolean sendRawKeysToWindow (String keys, String windowTitle){
        JSONObject result = bridge.executeCommand (String.format("{\"op\":\"sendRawKeysToWindow\",\"params\":[\"%s\",\"s\"]}", keys, windowTitle));
        return result.getBoolean("result");
    }

    /**
     * <p>Sends the keystrokes to the current window. Note that all chars will be sent literally - no special interpretation of ^ etc.
     * See the AutoIt docs for details.</p>
     * <p><code>
     * | send raw keys | ^c |
     * </code></p>
     *
     * @param keys The keys to send to the window, simulating a user typing.
     * @return  boolean    true if the sendKey requested was successful
     */
    public boolean sendRawKeys (String keys){
        return sendRawKeysToWindow(keys, lastWindowTitleManipulated);
    }

}
