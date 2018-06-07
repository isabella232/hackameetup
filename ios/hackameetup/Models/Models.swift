//
//  Models.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import Foundation
import SwiftyJSON

class Leg {
    var Id = ""
    var duration: Int = 0
    var carrierIconUrl: String = ""
    var carrierName: String = ""
    var departureDatetime: Date?
    var arrivalDatetime: Date?
    var originStationCode: String = ""
    var destinationStationCode: String = ""
    
    static func fromJSON( legJSON: JSON, rootJSON: JSON) -> Leg {
        
        let leg = Leg()
        
        leg.Id = legJSON["Id"].stringValue
        leg.duration = legJSON["Duration"].intValue
        leg.departureDatetime = DateUtils.dateFromString(dateString: legJSON["Departure"].stringValue)
        leg.arrivalDatetime = DateUtils.dateFromString(dateString: legJSON["Arrival"].stringValue)
        
        if let carrierJSON = legJSON["Carriers"].array {
            if carrierJSON.count > 0 {
                let carrierId = carrierJSON[0].intValue
                if let carrierJSON = rootJSON["Carriers"].array?.first(where: {$0["Id"].intValue == carrierId}) {
                    leg.carrierName = carrierJSON["Name"].stringValue
                    
                    if let favIconfileName = NSURL(string: carrierJSON["ImageUrl"].stringValue)?.lastPathComponent {
                        leg.carrierIconUrl = "https://logos.skyscnr.com/images/airlines/favicon/\(favIconfileName)"
                    }
                }
            }
        }
        
        if let placeJSON = rootJSON["Places"].array?.first(where: {$0["Id"].intValue == legJSON["OriginStation"].intValue}) {
            leg.originStationCode = placeJSON["Code"].stringValue
        }
        
        if let placeJSON = rootJSON["Places"].array?.first(where: {$0["Id"].intValue == legJSON["DestinationStation"].intValue}) {
            leg.destinationStationCode = placeJSON["Code"].stringValue
        }
        
        return leg
    }
}

class Itinerary {
    var price: Decimal = 0.0
    var outboundLeg: Leg = Leg()
    var inboundLeg: Leg = Leg()
    
    static func ItinerariesfromJSON( json: JSON) -> [Itinerary] {
        
        var intineraries: [Itinerary] = []
        
        let legs = json["Legs"].arrayValue.map({Leg.fromJSON(legJSON: $0, rootJSON: json)})
        
        for itineraryJSON in json["Itineraries"].arrayValue {
            
            let intinerary = Itinerary()
            
            
            let outboundLegId = itineraryJSON["OutboundLegId"].stringValue
            let inboundLegId = itineraryJSON["InboundLegId"].stringValue
            
            if let outboundLeg = legs.first(where: {$0.Id == outboundLegId}) {
                intinerary.outboundLeg = outboundLeg
            }
            
            if let inboundLeg = legs.first(where: {$0.Id == inboundLegId}) {
                intinerary.inboundLeg = inboundLeg
            }
            
            if let pricingOptionsJSON = itineraryJSON["PricingOptions"].array  {
                if pricingOptionsJSON.count > 0 {
                    intinerary.price = Decimal(pricingOptionsJSON[0]["Price"].doubleValue)
                }
            }
            
            intineraries.append(intinerary)
        }
        
        return intineraries
    }
}

class LivePricesResult {
    
    var originName: String = ""
    var destinationName: String = ""
    var outboundDate: Date?
    var inboundDate: Date?
    var itineraries: [Itinerary] = []
    
    static func fromJSON(json: JSON) -> LivePricesResult {
        
        let livePricesResult = LivePricesResult()
        
        livePricesResult.itineraries = Itinerary.ItinerariesfromJSON(json: json)
        
        livePricesResult.outboundDate = DateUtils.dateFromString(dateString: json["Query"]["OutboundDate"].stringValue, format: .DateOnly)
        
        livePricesResult.inboundDate = DateUtils.dateFromString(dateString: json["Query"]["InboundDate"].stringValue, format: .DateOnly)
        
        
        if let placeJSON = json["Places"].array?.first(where: {$0["Id"].intValue == json["Query"]["OriginPlace"].intValue}) {
            livePricesResult.originName = placeJSON["Name"].stringValue
        }
        
        if let placeJSON = json["Places"].array?.first(where: {$0["Id"].intValue == json["Query"]["DestinationPlace"].intValue}) {
            livePricesResult.destinationName = placeJSON["Name"].stringValue
        }
        
        return livePricesResult
    }
}

