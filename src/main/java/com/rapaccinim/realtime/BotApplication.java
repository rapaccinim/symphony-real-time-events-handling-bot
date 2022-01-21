package com.rapaccinim.realtime;

import com.symphony.bdk.core.SymphonyBdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.symphony.bdk.core.config.BdkConfigLoader.loadFromFile;

/**
 * Simple Bot Application.
 */
public class BotApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(BotApplication.class);

  public static void main(String[] args) throws Exception {

    // Initialize BDK entry point
    // final SymphonyBdk bdk = new SymphonyBdk(loadFromClasspath("/config.yaml"));
    final SymphonyBdk bdk = new SymphonyBdk(loadFromFile("config.yaml"));

    // here you subscribe the event listener to the datafeed
    bdk.datafeed().subscribe(new MyOrderListener(bdk));

    // always remember to start the datafeed service at the end
    bdk.datafeed().start();
  }

}
