//
//  Utils.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import Foundation

class DateUtils {
    
    enum DateFormat: String {
        case Default = "yyyy-MM-dd'T'HH:mm:ss"
        case DateOnly = "yyyy-MM-dd"
        case TimeHHMM = "HH:mm"
    }
    
    static func dateFromString( dateString: String, format: DateFormat = .Default) -> Date? {
      
        return getFormatter(withFormat: format).date(from: dateString)
    }
    
    static func stringForDate( date: Date?, format: DateFormat = .Default) -> String {
        
        guard (date != nil) else {return ""}
        
        return getFormatter(withFormat: format).string(from: date!)
    }
    
    static func nextMonday(after date: Date) -> Date {
        
        var mondayDateComponents = DateComponents()
        mondayDateComponents.weekday = 2
    
        let nextMonday = getDefaultCalendar().nextDate(after: date, matching: mondayDateComponents, matchingPolicy: .nextTime) ?? Date()
        
        return nextMonday
    }
    
    static func nextDay(after date: Date) -> Date {
        
        var nextDayComponents = DateComponents()
        nextDayComponents.day = 1
        
        let nextDay = getDefaultCalendar().date(byAdding: nextDayComponents, to: date) ?? Date()
        
        return nextDay
    }
    
    static func getPrettyString( fromDate date: Date) -> String {
        //Mar 01., Wed
        let calendar = DateUtils.getDefaultCalendar()
        let formatter = DateFormatter()
        
        let month = formatter.shortMonthSymbols[calendar.component(.month, from: date)-1]
        let weekDay = formatter.shortWeekdaySymbols[calendar.component(.weekday, from: date)-1]
        let day = calendar.component(.day, from: date)
        
        return "\(month) \(day)., \(weekDay)"
    }
    
    static func getFormatter(withFormat format: DateFormat) -> DateFormatter {
        
        let dateFormatter = DateFormatter()
        dateFormatter.calendar = Calendar(identifier: .gregorian)
        dateFormatter.dateFormat = format.rawValue
        dateFormatter.timeZone = getDefaultTimeZone()
        return dateFormatter
    }
    
    static func getDefaultCalendar() -> Calendar {
        
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = getDefaultTimeZone()
        return calendar
    }
    
    static func getDefaultTimeZone() -> TimeZone {
        return TimeZone(identifier: "UTC")!
    }
}
