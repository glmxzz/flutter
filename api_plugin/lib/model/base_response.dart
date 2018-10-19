import 'package:json_annotation/json_annotation.dart';

part 'package:api_plugin/model/base_response.g.dart';

@JsonSerializable(nullable: false)
class BaseResponse {
  String msg;
  int code;
  Map data;

  BaseResponse();

  factory BaseResponse.fromJson(Map<String, dynamic> json) =>
      _$BaseResponseFromJson(json);
}