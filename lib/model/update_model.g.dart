// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'update_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UpdateModel _$UpdateModelFromJson(Map<String, dynamic> json) {
  return UpdateModel()
    ..type = json['type'] as String
    ..url = json['url'] as String
    ..title = json['title'] as String
    ..content = json['content'] as String
    ..checksum = json['checksum'] as String;
}

Map<String, dynamic> _$UpdateModelToJson(UpdateModel instance) =>
    <String, dynamic>{
      'type': instance.type,
      'url': instance.url,
      'title': instance.title,
      'content': instance.content,
      'checksum': instance.checksum
    };
