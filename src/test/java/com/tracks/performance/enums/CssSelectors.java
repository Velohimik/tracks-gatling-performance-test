package com.tracks.performance.enums;

import lombok.Getter;

@Getter
public enum CssSelectors {

  CSRF_TOKEN_SELECTOR("meta[name=\"csrf-token\"]"),
  AUTHENTICITY_TOKEN_SELECTOR("input[name=\"authenticity_token\"]"),
  VIEW_CONTEXT_SELECTOR("a[title^=\"View context\"]");


  private final String selector;

  CssSelectors(String selector) {
    this.selector = selector;
  }

}
