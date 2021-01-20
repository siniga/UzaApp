package com.agnet.uza.models;

import java.util.List;

public class Discount {

  private String name;
  private int percentage;


    public Discount(String name, int percentage) {
      this.name = name;
      this.percentage = percentage;

    }

    public String getName() {
        return name;
    }

    public int getPercentage() {
        return percentage;
    }

  @Override
  public String toString() {
    return name +" "+percentage+"%";
  }
}
