package com.rage.ecommerce.drools.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Fact {
    private boolean someProperty;
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public boolean isSomeProperty() {
        return someProperty;
    }

    public void setSomeProperty(boolean someProperty) {
        this.someProperty = someProperty;
    }

    @Override
    public String toString() {
        return "YourFact{someProperty=" + someProperty + ", result='" + result + "'}";
    }
}
