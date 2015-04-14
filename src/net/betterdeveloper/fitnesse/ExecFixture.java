package net.betterdeveloper.fitnesse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Slim fixture to execute Runtime.exec commands, see README.md for more information.
 */
public class ExecFixture {

    private int _lastReturnCode;

    /**
     * The return code for the executed command
     */
    public int lastExecReturnCode() {
        return _lastReturnCode;
    }

    /**
     * Executes a command.
     *
     * @param cmd The command to be passed to Runtime.exec
     * @return The executed process' stdout output
     */
    public String exec (String cmd) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec (cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder result = new StringBuilder(1024);
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        _lastReturnCode = p.waitFor();
        return result.toString();
    }
}