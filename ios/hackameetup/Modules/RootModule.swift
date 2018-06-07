//
//  RootModule.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import Foundation

protocol RootModuleInterface : class {
    func goToPriceList()
}

class RootModule: Module {
    
    weak var interface: RootModuleInterface?

    //MARK: exposed methods
    
    func startButtonPressed() {
        interface?.goToPriceList()
    }
    
    //MARK: module lifecycle

    
    func onResume() {
        
    }
    
    func onPause() {
        
    }
}
