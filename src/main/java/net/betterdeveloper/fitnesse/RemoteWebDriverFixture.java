package net.betterdeveloper.fitnesse;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.Augmenter;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM;

public class RemoteWebDriverFixture {
    WebDriver webDriver;
    WebElement lastElement;

    private Map<String, String> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
        // Assume you have a Map<String, String> in JSONObject
        @SuppressWarnings("unchecked")
        Iterator<String> nameItr = jsonObject.keys();
        Map<String, String> outMap = new HashMap<String, String>();
        while(nameItr.hasNext()) {
            String name = nameItr.next();
            outMap.put(name, jsonObject.getString(name));
        }

        String platform = outMap.get(PLATFORM);
        if (platform != null) {
            outMap.put(PLATFORM, platform.toUpperCase());
        }

        return  outMap;
    }

    public void startOnWithCapabilities (String url, String capabilitiesInJson) throws java.net.MalformedURLException {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(capabilitiesInJson);
        } catch (JSONException e) {
            throw new RuntimeException("Unable to interpret capabilities", e);
        }

        Map<String, String> desiredCapabilities;
        try {
            desiredCapabilities = jsonObjectToMap(jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException("Unable to fetch required fields from json string", e);
        }

        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(url), new DesiredCapabilities(desiredCapabilities));
        remoteWebDriver.setFileDetector(new org.openqa.selenium.remote.LocalFileDetector()); // https://saucelabs.com/resources/selenium-file-upload
        webDriver =  new Augmenter().augment(remoteWebDriver);
    }

    public void stop () {
        webDriver.close();
    }

    public boolean findElementByName (String name) {
        lastElement = webDriver.findElement(By.name(name));
        return lastElement!=null;
    }

    public boolean findElementByXpath (String xpath) {
        lastElement = webDriver.findElement(By.xpath(xpath));
        return lastElement!=null;
    }

    public boolean clickOnElementWithName (String name) {
        boolean found = findElementByName(name);
        if (found)
            lastElement.click();
        return found;
    }

    public boolean clickOnElementWithXpath (String xpath) {
        boolean found = findElementByXpath(xpath);
        if (found)
            lastElement.click();
        return found;
    }

    public boolean clickOnElement() {
        if (lastElement!=null)
            lastElement.click();
        return lastElement!=null;
    }

}
