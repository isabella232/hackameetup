//
//  LivePricesViewController.swift
//  AppChallenge
//
//  Copyright © 2018 Skyscanner. All rights reserved.
//

import UIKit

class RootViewController: UIViewController, RootModuleInterface {
    
    private let module: RootModule
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        module = RootModule.init()
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
                
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: .default)
        
        navigationController?.navigationBar.tintColor = UIColor(red: 84/255, green: 76/255, blue: 99/266, alpha: 1)
        
        self.title = ""
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        module.onResume()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        module.onPause()
    }
    
    //MARK: LivePricesListModuleInterface
    
    func showNetworkActivityVisible(visible: Bool) {
        UIApplication.shared.isNetworkActivityIndicatorVisible = visible
    }
    
    func showAlert(title: String, message: String) {
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    func showItineraries(itineraries: [Itinerary]) {
        
    }
    
    //MARK: actions
    
    @IBAction func startButtonPressed(_ sender: Any) {
        module.startButtonPressed()
    }
    
    //MARK: RootModuleInterface
    
    func goToPriceList() {
        let livePricesViewController = LivePricesViewController(nibName: "LivePricesViewController", bundle: nil)
        self.navigationController?.pushViewController(livePricesViewController, animated: true)
    }
}
