//
//  LivePricesListModule.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import Foundation
import SwiftyJSON

protocol LivePricesListModuleInterface: class {
    func showNetworkActivityVisible(visible : Bool)
    
    func showAlert( title: String, message: String)
    
    func showItineraries( itineraries: [Itinerary])
    
    func setTripDates( outboundDate: Date, inboundDate: Date)
    
    func setTripPlaces( origin: String, destination: String )
    
    func setResultCount( count: Int, total: Int)
}

class LivePricesListModule : Module {
    
    private let service: LivePricingService
    
    weak var interface: LivePricesListModuleInterface?
    
    init() {
        service = LivePricingService.init()
    }
    
    //MARK: module lifecycle
    
    func onResume() {
        interface?.showNetworkActivityVisible(visible: true)
        getResults()
    }
    
    func onPause() {
    }
    
    //MARK: exposed methods

    func onRefreshData() {
        getResults()
    }
    
    //MARK: response handling
    
    private func getResults() {
        
        interface?.showNetworkActivityVisible(visible: true)
        
        service.getResults { (result) in
            switch result {
            case .success(let value) :
                self.interface?.showNetworkActivityVisible(visible: false)
                self.interface?.showItineraries(itineraries: value.itineraries)
                if value.outboundDate != nil && value.inboundDate != nil {
                    self.interface?.setTripDates(outboundDate: value.outboundDate!, inboundDate: value.inboundDate!)
                }
                self.interface?.setTripPlaces(origin: value.originName, destination: value.destinationName)
                self.interface?.setResultCount(count: value.itineraries.count, total: value.itineraries.count)
            case .failure(let error) :
                self.interface?.showNetworkActivityVisible(visible: false)
                self.interface?.showAlert(title: "error", message: error.localizedDescription)
            }
        };
    }
}

