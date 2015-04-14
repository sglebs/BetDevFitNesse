package net.betterdeveloper.fitnesse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecFixture {

    private int _lastReturnCode;

    public int lastExecReturnCode() {
        return _lastReturnCode;
    }

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