package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import com.ybx.flutter.plugin.apiplugin.ApiPlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    ApiPlugin.registerWith(registry.registrarFor("com.ybx.flutter.plugin.apiplugin.ApiPlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
