import 'package:json_annotation/json_annotation.dart';

part 'update_model.g.dart';

@JsonSerializable(nullable: false)
class UpdateModel {
  String type;
  String url;
  String title;
  String content;
  String checksum;

  UpdateModel();

  factory UpdateModel.fromJson(Map<dynamic, dynamic> json) =>
      _$UpdateModelFromJson(json);
}
