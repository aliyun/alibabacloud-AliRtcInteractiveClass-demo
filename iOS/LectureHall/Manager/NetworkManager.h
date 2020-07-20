//
//  NetworkManager.h
//  LectureHall
//
//  Created by alibaba on 2020/5/22.
//  Copyright © 2020 alibaba. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkManager : NSObject

+ (void)GET:(NSString *)route
       parameters:(id _Nullable)parameters
completionHandler:(void (^)(NSString *__nullable errString,NSDictionary *_Nullable resultDic))handle;

@end

NS_ASSUME_NONNULL_END
