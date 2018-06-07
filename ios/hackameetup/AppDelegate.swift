//
//  AppDelegate.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        window = UIWindow(frame: UIScreen.main.bounds)
        
        let rootViewController = RootViewController(nibName: "RootViewController", bundle: nil)
        
        let navigationController = UINavigationController(rootViewController: rootViewController)
        
        window?.rootViewController = navigationController
        
        window?.makeKeyAndVisible()
    
        return true
    }
}
