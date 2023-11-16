package com.tracks.performance.enums;

public enum XPathExpression {

    CSRF_TOKEN_XPATH("//*[@name='csrf-token']/@content"),
    VIEW_CONTEXT("//*[contains(@title, 'View context')]");


    private final String xPath;

    XPathExpression(String xPath) {
        this.xPath = xPath;
    }

    public String getExpression() {
        return xPath;
    }
}
