//
//  LivePricingService.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON

class LivePricingService {

    private let API_KEY = "use-your-own-key"
    
    private let uuid: UUID!
    
    init() {
        uuid = UUID()
    }
    
    private let genericError = NSError(domain: "AppChallenge", code: 0, userInfo: [NSLocalizedDescriptionKey:"Unknown error"])
    
    private var locationForCurrentSession: String?
    
    //MARK: exposed methods
    
    func getResults(completionHandler: @escaping (Result<LivePricesResult>) -> Void) {
        
        guard locationForCurrentSession != nil else {
            initSession { (result) in
                switch result {
                case .success(let value) :
                    self.locationForCurrentSession = value
                    self.pollLivePrices(location: value, isFirstPoll: true, completionHandler: completionHandler)
                case .failure(let error) :
                    completionHandler(Result.failure(error))
                }
            }
            return
        }
        
        pollLivePrices(location: self.locationForCurrentSession!, isFirstPoll: false, completionHandler: completionHandler)
    }
    
    //MARK: private methods
    
    private func pollLivePrices(location: String, isFirstPoll: Bool, completionHandler: @escaping (Result<LivePricesResult>) -> Void) {
        
        let url = "\(location)?apikey=\(API_KEY)"
        
        let destination: DownloadRequest.DownloadFileDestination = { _, _ in
            return (self.getTmpURL(), [.removePreviousFile, .createIntermediateDirectories])
        }

        Alamofire.download(url, to: destination).responseJSON { response in
            
            switch response.result {
            case .success(let value):
                if let url = response.destinationURL {
                    do {
                       _ = try FileManager.default.replaceItemAt(self.getCacheURL(), withItemAt: url)
                    } catch(let error) {
                        print("Error: \(error.localizedDescription)")
                    }
                }
                let json = JSON(value)
                /*
                 Must keep polling the results until you get 'UpdatesComplete’. The results may take up to a minute to update.
                */
                if json["Status"] == "UpdatesPending" {
                    self.scheduleDelayedPollLivePrices(location: location, isFirstPoll: isFirstPoll, completionHandler: completionHandler)
                } else {
                    completionHandler(Result.success(LivePricesResult.fromJSON(json: json)))
                }
                
            case .failure(let error):
                if(response.response?.statusCode == 304) {
                    /*
                        The API returns a 304 when there is no change since the last request.  In these cases the correct behaviour is to continue to use the previous response.  If the most recent status was "UpdatesPending" then you can be sure that subsequent polls will return a 200 response.  If the 304 is returned on the first poll, that means the session has not been fully initialized.  In this case, poll again 1 second later.
                    */
                    if isFirstPoll {
                        self.scheduleDelayedPollLivePrices(location: location, isFirstPoll: isFirstPoll, completionHandler: completionHandler)
                    } else {
                        do {
                            let json = try JSON(data: Data(contentsOf: self.getCacheURL()))
                            completionHandler(Result.success(LivePricesResult.fromJSON(json: json)))
                        } catch(let error) {
                            completionHandler(Result.failure(error))
                        }
                    }
                } else {
                    completionHandler(Result.failure(error))
                }
            }
        }
    }
    
    private func scheduleDelayedPollLivePrices(location: String, isFirstPoll: Bool, completionHandler: @escaping (Result<LivePricesResult>) -> Void) {
        
        let deadlineTime = DispatchTime.now() + .milliseconds(1000)
        DispatchQueue.main.asyncAfter(deadline: deadlineTime, execute: {
            self.pollLivePrices(location: location, isFirstPoll: isFirstPoll,  completionHandler: completionHandler)
        })
    }
    
    private func initSession(completionHandler: @escaping (Result<String>) -> Void) {
        
        let nextMondayDate = DateUtils.nextMonday(after: Date())
        let nextTuesdayDate = DateUtils.nextDay(after: nextMondayDate)
        
        let nextMondayDateString = DateUtils.stringForDate(date: nextMondayDate, format: .DateOnly)
        let nextTuesdayDateString = DateUtils.stringForDate(date: nextTuesdayDate, format: .DateOnly)
        
        let parameters: Parameters = [
            "cabinclass": "Economy",
            "country": "UK",
            "currency": "GBP",
            "locale": "GBP",
            "locationSchema": "sky",
            "originplace": "EDI-sky",
            "destinationplace": "LOND-sky",
            "outbounddate": nextMondayDateString,
            "inbounddate": nextTuesdayDateString,
            "adults": "1",
            "children": "0",
            "infants": "0",
            "stops": "0",
            "apikey": API_KEY]
        
        let url = "http://partners.api.skyscanner.net/apiservices/pricing/v1.0"
        
        Alamofire.request(url, method: .post, parameters: parameters).responseJSON { response in
            
            switch response.result {
                
            case .success(_):
                if let locationHeaderValue = response.response?.allHeaderFields["Location"] as? String {
                completionHandler(Result.success(locationHeaderValue))
                } else {
                    completionHandler(Result.failure(self.genericError))
                }
                
            case .failure(let error):
                completionHandler(Result.failure(error))
            }
        }
    }
    
    private func getTmpURL() -> URL {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("pollLivePrices_cache_\(uuid.uuidString)_tmp.json")
    }
    
    private func getCacheURL() -> URL  {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0].appendingPathComponent("pollLivePrices_cache_\(uuid.uuidString).json")
    }
    
    //MARK: lifycycle
    
    deinit {
        try? FileManager.default.removeItem(at: getTmpURL())
        try? FileManager.default.removeItem(at: getCacheURL())
    }
}
