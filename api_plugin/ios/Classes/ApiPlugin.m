#import "ApiPlugin.h"
#import <api_plugin/api_plugin-Swift.h>

@implementation ApiPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftApiPlugin registerWithRegistrar:registrar];
}
@end
