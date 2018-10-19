import 'dart:async';
import 'dart:collection';
import 'dart:io';

import 'package:dio/dio.dart';

import 'api_plugin.dart';
import 'model/base_response.dart';

class ApiManager {
  final TIME_OUT = 5000;
  static final ApiManager _singleton = new ApiManager._internal();
  bool isProxy = true;

  factory ApiManager() {
    return _singleton;
  }

  ApiManager._internal();

  void post(String path, Map reqParams) async {}

  Future<BaseResponse> get<T>(String path, Map reqParams) async {
    Dio _dio = getDio();

    var commomParams = await ApiPlugin.platformVersion;

    if (reqParams != null) {
      reqParams.addAll(commomParams);
    } else {
      reqParams = commomParams;
    }

    //排序
    final sorted = new SplayTreeMap<String, dynamic>.from(
        reqParams, (a, b) => a.compareTo(b));

    _dio.options.data = sorted;
    var signHash = await ApiPlugin.getSignHash(sorted);
    _dio.options.headers["x-sign"] = signHash;
    var response = await _dio.get(path);

    return BaseResponse.fromJson(response.data);
  }

  Dio getDio() {
    Dio _dio = new Dio();
    if (isProxy) {
      _dio.onHttpClientCreate = (HttpClient client) {
        client.findProxy = (uri) {
          //proxy all request to localhost:8888
          return "PROXY 10.1.9.40:8899";
        };
        // 你也可以自己创建一个新的HttpClient实例返回。
        // return new HttpClient(SecurityContext);
      };
    }

    _dio.options.baseUrl = "http://lt1-capi.haoyile.com/miloan/rest";
    _dio.options.connectTimeout = TIME_OUT;
    _dio.options.receiveTimeout = TIME_OUT;
//    BeforeRequest.process(_dio.interceptor.request);

    return _dio;
  }
}
