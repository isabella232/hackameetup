//
//  LivePricesViewController.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import UIKit

class LivePricesViewController: UIViewController, LivePricesListModuleInterface, UITableViewDelegate, UITableViewDataSource {
   
    private let module: LivePricesListModule
    
    private let reuseIdentifier = "ItineraryTableViewCell"
    
    private var itineraries: [Itinerary]?
    
    //MARK: Outlets
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var headerView: UIView!
    
    @IBOutlet weak var headerTitleLabel: UILabel!
    
    @IBOutlet weak var headerSubTitleLabel: UILabel!
    
    @IBOutlet weak var headerResultsCountLabel: UILabel!
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        module = LivePricesListModule.init()
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        module.interface = self
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    //MARK: view lyfecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        tableView.register(UINib(nibName: "ItineraryTableViewCell", bundle: nil), forCellReuseIdentifier: reuseIdentifier)
        
        
        //Adjusts the safe area to exlude the navigation bar
        let newSafeArea = UIEdgeInsets(top: -self.navigationController!.navigationBar.frame.size.height, left: 0, bottom: 0, right: 0)
        
        self.additionalSafeAreaInsets = newSafeArea
        
        let refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)
        tableView.refreshControl = refreshControl
        
        self.headerTitleLabel.text = nil
        self.headerSubTitleLabel.text = nil
        self.headerResultsCountLabel.text = nil
    }
    
    override func viewWillAppear(_ animated: Bool) {
        module.onResume()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        module.onPause()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        
        let shadowPath = UIBezierPath(rect: headerView.bounds)
        headerView.layer.masksToBounds = false
        headerView.layer.shadowColor = UIColor.black.cgColor
        headerView.layer.shadowOffset = CGSize(width: 0.0, height:
            5.0)
        headerView.layer.shadowOpacity = 0.2
        headerView.layer.shadowPath = shadowPath.cgPath
    }
    
    //MARK: LivePricesListModuleInterface
    
    func showNetworkActivityVisible(visible: Bool) {
        UIApplication.shared.isNetworkActivityIndicatorVisible = visible
        self.navigationItem.prompt = visible ? "Fetching data" : nil
    }
    
    func showAlert(title: String, message: String) {
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    func showItineraries(itineraries: [Itinerary]) {
        self.itineraries = itineraries
        tableView.reloadData()
    }
    
    func setTripDates(outboundDate: Date, inboundDate: Date) {
        //Mar 01., Wed – Mar 05., Sun
        
        let prettyOutboundDate = DateUtils.getPrettyString(fromDate: outboundDate)
        
        let prettyInboundDate = DateUtils.getPrettyString(fromDate: inboundDate)
        
        
        headerSubTitleLabel.text = "\(prettyOutboundDate) - \(prettyInboundDate)"
    }
    
    func setTripPlaces(origin: String, destination: String) {
        headerTitleLabel.text = "\(origin) to \(destination)"
    }
    
    func setResultCount(count: Int, total: Int) {
        headerResultsCountLabel.text = "\(count) of \(total) results shown"
    }
    
    //MARK: UITableViewDelegate
    

    
    //MARK: UITableViewDataSource
    
    func tableView(_ tableView: UITableView,
                   numberOfRowsInSection section: Int) -> Int {
        return itineraries?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: reuseIdentifier, for: indexPath) as!  ItineraryTableViewCell
        
        cell.configure(withItinerary: itineraries![indexPath.row], isLast: indexPath.row == itineraries!.count-1)
        
        return cell
    }
    
    //MARK: Actions
    
    @objc private func refreshData(_ sender: UIRefreshControl) {
        
        module.onRefreshData()
        sender.endRefreshing()
    }
}
