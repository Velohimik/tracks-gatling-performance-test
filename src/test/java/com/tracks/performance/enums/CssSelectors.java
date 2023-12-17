package com.tracks.performance.enums;

public enum CssSelectors {
  CSRF_TOKEN_XPATH("meta[name=\"csrf-token\"]"),
  AUTHENTICITY_TOKEN_SELECTOR("input[name=\"authenticity_token\"]"),
  VIEW_CONTEXT("a[title^=\"View context\"]");

  private final String selector;

  CssSelectors(String selector) {
    this.selector = selector;
  }

  public String getExpression() {
    return selector;
  }
}
