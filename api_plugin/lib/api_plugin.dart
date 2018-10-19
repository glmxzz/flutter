import 'dart:async';

import 'package:flutter/services.dart';

class ApiPlugin {
  static const MethodChannel _channel =
      const MethodChannel('api_plugin');

  static Future<Map> get platformVersion async {
    var info = await _channel.invokeMethod('getPlatformVersion');
    return info;
  }


  static Future<String> getSignHash(Map data) async {
    var info = await _channel.invokeMethod('getSignHash', data);
    return info;
  }

  static Future<dynamic> handler(MethodCall call) {
    String method = call.method;

    switch (method) {
      case "updateLocation":
        {
          Map args = call.arguments;
          print(args);
        }
        break;
    }
    return new Future.value("");
  }
}
