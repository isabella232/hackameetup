//
//  ItineraryTableViewCell.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import UIKit
import AlamofireImage

class ItineraryTableViewCell: UITableViewCell {

    @IBOutlet weak var outboundCarrierIcon: UIImageView!
    
    @IBOutlet weak var outboundTimes: UILabel!
    
    @IBOutlet weak var outboundDuration: UILabel!
    
    @IBOutlet weak var outboundConnection : UILabel!
    
    @IBOutlet weak var inboundCarrierIcon: UIImageView!
    
    @IBOutlet weak var inboundTimes: UILabel!
    
    @IBOutlet weak var inboundDuration: UILabel!
    
    @IBOutlet weak var inboundConnection : UILabel!

    @IBOutlet weak var price : UILabel!
    
    @IBOutlet weak var separator: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    func configure(withItinerary itinerary: Itinerary, isLast: Bool) {
        if let url = URL(string: itinerary.outboundLeg.carrierIconUrl) {
            outboundCarrierIcon.af_setImage(withURL: url)
        } else {
            outboundCarrierIcon.image = nil
        }
        
        outboundTimes.text = formatTimesString(withDepartureDate: itinerary.outboundLeg.departureDatetime, andArrivalDate: itinerary.outboundLeg.arrivalDatetime)
        
        outboundConnection.text = formatConnectionString(withDepartureStationCode: itinerary.outboundLeg.originStationCode, arrivalStationCode: itinerary.outboundLeg.destinationStationCode, andCarrierName: itinerary.outboundLeg.carrierName)
        
        outboundDuration.text = formatDuration(duration: itinerary.outboundLeg.duration)
        
        if let url = URL(string: itinerary.inboundLeg.carrierIconUrl) {
            inboundCarrierIcon.af_setImage(withURL: url)
        } else {
            inboundCarrierIcon.image = nil
        }
        
        inboundTimes.text = formatTimesString(withDepartureDate: itinerary.inboundLeg.departureDatetime, andArrivalDate: itinerary.inboundLeg.arrivalDatetime)
        
        inboundConnection.text = formatConnectionString(withDepartureStationCode: itinerary.inboundLeg.originStationCode, arrivalStationCode: itinerary.inboundLeg.destinationStationCode, andCarrierName: itinerary.inboundLeg.carrierName)
        
        inboundDuration.text = formatDuration(duration: itinerary.inboundLeg.duration)
        
        price.text = formatPrice(price: itinerary.price)
        
        separator.isHidden = isLast
    }
    
    private func formatTimesString( withDepartureDate departureDate: Date?, andArrivalDate arrivalDate: Date?) -> String {
       
        let departureTime = DateUtils.stringForDate(date: departureDate, format: .TimeHHMM)
        
        let arrivalTime = DateUtils.stringForDate(date: departureDate, format: .TimeHHMM)
        
        return "\(departureTime) - \(arrivalTime)"
    }
    
    private func formatConnectionString(withDepartureStationCode departureCode: String, arrivalStationCode arrivalCode: String, andCarrierName carrierName: String) -> String {
        
        return "\(departureCode) - \(arrivalCode), \(carrierName)"
    }
    
    private func formatDuration(duration: Int) -> String {
        return "\(duration/60)h \(duration%60)m"
    }
    
    private func formatPrice(price: Decimal) -> String {
        let formatter = NumberFormatter()
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        if let formattedPrice = formatter.string(from: NSDecimalNumber(decimal: price)) {
            return "£ \(formattedPrice)"
        }
        return "-"
    }
}
