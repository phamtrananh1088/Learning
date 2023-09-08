//
//  SyncData.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import Combine

enum GetValue<T: Any> {
    case Value(value: T?)
    case Error(error: NetworkError)
}

enum SyncProcess {
    case none, post, fetch
}

class SyncData {
    
    class Task3<T: Any> {
        /**
         * タスクCompletable
         */
        internal func source() -> AnyPublisher<Any, NetworkError> {
            return Just(true)
                .setFailureType(to: NetworkError.self)
                .eraseToAnyPublisher()
        }
        
        internal func doOnSuccessSource(_ rs: Any) { }
        
        /**
         * タスク成功したら、（受信）
         * @see [Completable.andThen]
         */
        internal func fetch(lo: LoggerUser) -> AnyPublisher<Optional<T>, NetworkError> {
            return Just(nil)
                .setFailureType(to: NetworkError.self)
                .eraseToAnyPublisher()
        }
        
        internal func doOnSuccessFetch(_ rs: Any?) { }
        
        func onSuccess() {}
        
        private var bag = Set<AnyCancellable>()
        var pendings: [(Bool, [(Optional<GetValue<T>>) -> ()])] = []
        var next: [(Bool, [(Optional<GetValue<T>>) -> ()])] = []
        let initReduce: (Bool, [(Optional<GetValue<T>>) -> ()]) = (false, [])
        @Published var isReady = true
        
        init() {
            $isReady.receive(on: DispatchQueue.main)
                .sink(receiveValue: { ready in
                    if ready && self.pendings.count > 0 {
                        self.next = self.pendings
                        self.pendings.removeAll()
                        self.Start()
                    }
                }).store(in: &bag)
        }
        
        /* callback will called exactly once */
        func execute(
            preferFetch: Bool = true,
            callback: ((Optional<GetValue<T>>) -> Void)? = nil
        ) {
            let c = callback == nil ? [] : [callback!]
            pendings.append((preferFetch, c))
            if isReady {
                next = pendings
                pendings.removeAll()
                Start()
            }
        }
        
        private func Start() {
            next.publisher
                .reduce(initReduce) { acc, curr in
                    let f = acc.0 || curr.0
                    return (f, acc.1 + curr.1)
                }
                .receive(on: DispatchQueue.main)
                .sink {(fetch, callbacks) in
                    self.isReady = false
                    self.source()
                        .map { v -> AnyPublisher<Optional<T>, NetworkError> in
                            self.doOnSuccessSource(v)
                            let f = fetch ? Current.Shared.loggedUser : nil
                            return f != nil
                            ? self.fetch(lo: f!)
                            : Just(nil).setFailureType(to: NetworkError.self).eraseToAnyPublisher()
                        }
                        .switchToLatest()
                        .receive(on: DispatchQueue.main)
                        .sink { completion in
                            switch(completion) {
                            case .finished:
                                break
                            case .failure(let error):
                                // doOnError
                                callbacks.forEach{ $0(GetValue.Error(error: error))}
                                
#if DEBUG
                                debugPrint("Error:")
                                debugPrint(error)
                                debugPrint(T.self)
#endif
                                
                                switch (error) {
                                case .parseError(reason: _): break
                                default: break
                                }
                                
                                self.isReady = true
                            }
                        } receiveValue: { v in
                            // doOnSuccess
                            self.doOnSuccessFetch(v)
                            callbacks.forEach { $0(GetValue<T>.Value(value: v)) }
                            self.onSuccess()
                            
                            self.isReady = true
                        }.store(in: &self.bag)
                }.store(in: &bag)
        }
        
        func dispose() {
            bag.forEach { $0.cancel()}
        }
    }
    
    class Task2<T: Any>: Task3<T> {
        var retryTimes: Int
        init(_ retryTimes: Int) {
            self.retryTimes = retryTimes
            super.init()
        }
    }
    
    private var intervalRun: Timer? = nil
    
    init() {
        guard intervalRun == nil else { return }
        intervalRun = Timer.scheduledTimer(timeInterval: 60, target: self, selector: #selector(self.intervalRepeat), userInfo: nil, repeats: true)
    }
    
    private func handelIntervalRepeat(isOk: Bool, error: NetworkError?) {
        if !isOk {
#if DEBUG
            debugPrint(error as Any)
#endif
        }
    }
    
    @objc func intervalRepeat() {
        syncAll()
    }
    
    private func syncAll() {
        if Current.Shared.loggedUser?.token != nil {
            if currentTimeMillis() - timeSyncBin >= Resources.halfMinute {
                syncBin(callBack: handelIntervalRepeat)
            }
            
            if currentTimeMillis() - timeSyncCollection >= Resources.halfMinute {
                syncCollection(callBack: handelIntervalRepeat)
            }
            
            if currentTimeMillis() - timeSyncincidental >= Resources.halfMinute {
                syncIncidental(callBack: handelIntervalRepeat)
            }
            
            syncGeo(callBack: handelIntervalRepeat)
            syncNotice(callBack: handelIntervalRepeat)
            syncFuel(callBack: handelIntervalRepeat)
            syncFailedData(callBack: handelIntervalRepeat)
            syncImgData(callBack: handelIntervalRepeat)
            syncLogFile(callBack: handelIntervalRepeat)
            
            syncSensorCsvUpload()
            syncRest(callBack: handelIntervalRepeat)
            
            syncDeliveryChart(callBack: handelIntervalRepeat)
        }
    }
    
    private var bag = Set<AnyCancellable>()
    
    private func handleErr(err: Subscribers.Completion<NetworkError>, callBack: @escaping (Bool, NetworkError?)->()) {
        switch(err) {
        case .finished:
            break
        case .failure(let error):
            switch(error) {
            case .parseError(let reason):
                let errorCode = (reason as NSError).code
                // error != cancel
                if errorCode != -999 {
                    callBack(false, error)
                }
                break
            default:
                callBack(false, error)
                break
            }
        }
    }
    
    /**
     * 給油データ
     * @see [SyncApi.postFuel]
     */
    private var callBackFuel: ((Bool, NetworkError?) -> ())? = nil
    private var fuelRetryTimes = 5
    private var fuelProcess: SyncProcess = .none {
        didSet {
            if fuelProcess == .post {
                postFuel()
            }
        }
    }
    
    func syncFuel(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackFuel = callBack
        // post
        fuelProcess = .post
    }
    
    private func postFuel() {
        let resultDb = Config.Shared.resultDb!
        resultDb.commonKyuyuDao?.setPending()
        let pending = resultDb.commonKyuyuDao?.getPending()
        
        if pending != nil {
            let f = FuelInfo(Kyuyus: pending!, clientInfo: Config.Shared.clientInfo)
            Config.Shared.post.postFuel(fuelInfo: f)
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackFuel!)
                }, receiveValue: { res in
                    
                    self.fuelRetryTimes = self.fuelRetryTimes - 1
                    if res.result || self.fuelRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for it in f.Kyuyus {
                                    it.setSyncFinished()
                                    try it.update(db)
                                }
                            }
                            
                            Config.Shared.resultDb?.commonKyuyuDao?.deleteSyncedBeforeDate(date: currentTimeMillis() - 8 * 3600_000)
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.fuelRetryTimes = 5
                        self.fuelProcess = .fetch
                    } else {
                        Helper.Shared.sendBroadcastNotification(.Fuel)
                        self.callBackFuel!(true, nil)
                    }
                }).store(in: &bag)
        } else {
            Helper.Shared.sendBroadcastNotification(.Fuel)
            self.callBackFuel!(true, nil)
        }
    }
    
    /**
     * Masterデータ
     * @see [FetchApi.masterData]
     */
    func syncMaster(callBack: @escaping (Bool, NetworkError?)->()) {
        Config.Shared.fetch.master(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(
                receiveCompletion: { err in
                    self.handleErr(err: err, callBack: callBack)
                },
                receiveValue: { res in
                    
                    do {
                        try Config.Shared.userDb?.instanceDb?.write { db in
                            
                            // WorkStatus
                            try WorkStatus.deleteAll(db)
                            if res.workStatuses != nil && res.workStatuses!.count > 0 {
                                for ws in res.workStatuses! {
                                    try ws.save(db)
                                }
                            }
                            
                            // BinStatus
                            try BinStatus.deleteAll(db)
                            if res.binStatuses != nil && res.binStatuses!.count > 0 {
                                for bs in res.binStatuses! {
                                    try bs.save(db)
                                }
                            }
                            
                            // Work
                            try Work.deleteAll(db)
                            if res.works != nil && res.works!.count > 0 {
                                for w in res.getWorks() {
                                    try w.save(db)
                                }
                            }
                            
                            // Fuel
                            try Fuel.deleteAll(db)
                            if res.fuels != nil && res.fuels!.count > 0 {
                                for fu in res.fuels! {
                                    try fu.save(db)
                                }
                            }
                            
                            // Truck
                            try Truck.deleteAll(db)
                            if res.trucks != nil && res.trucks!.count > 0 {
                                for tr in res.trucks! {
                                    try tr.save(db)
                                }
                            }
                            
                            // Shipper
                            try Shipper.deleteAll(db)
                            if res.shippers != nil && res.shippers!.count > 0 {
                                for sp in res.shippers! {
                                    try sp.save(db)
                                }
                            }
                            
                            // WorkPlace
                            // TODO: Not see on JS return
                            
                            // DelayReason
                            try DelayReason.deleteAll(db)
                            if res.delayReason != nil && res.delayReason!.count > 0 {
                                for dl in res.delayReason! {
                                    try dl.save(db)
                                }
                            }
                            
                            // CollectionGroup
                            try CollectionGroup.deleteAll(db)
                            if res.collections != nil && res.collections!.count > 0 {
                                for co in res.collections! {
                                    try co.save(db)
                                }
                            }
                        }
                        
                    } catch {
                        callBack(false, nil)
                        debugPrint(error.localizedDescription)
                    }
                    
                    callBack(true, nil)
                }).store(in: &bag)
    }
    
    /**
     * Binデータ、運行データ
     * @see [SyncApi.postBin]
     * @see [FetchApi.binData]
     */
    private var callBackBin: ((Bool, NetworkError?) -> ())? = nil
    private var timeSyncBin: Int64 = 0
    private var binRetryTimes = 5
    private var binProcess: SyncProcess = .none {
        didSet {
            if binProcess == .post {
                postBin()
            } else if binProcess == .fetch {
                fetchBin()
            }
        }
    }
    
    func syncBin(callBack: @escaping (Bool, NetworkError?)->()) {
        timeSyncBin = currentTimeMillis()
        Helper.Shared.stopURLSessionByUrl(lstEndpoint: [.postBin, .binData])
        
        self.callBackBin = callBack
        // post
        binProcess = .post
    }
    
    private func postBin() {
        try? Current.Shared.loggedUser?.commitBinToCommon()
        Config.Shared.resultDb?.workResutlDao?.setPending()
        Config.Shared.resultDb?.binResultDao?.setPending()
        if let lstPending = Config.Shared.resultDb?.binResultDao?.getPendingWithWorkResult() {
            var br: [CommonBinResult] = []
            var wr: [CommonWorkResult] = []
            
            for it in lstPending {
                if !br.contains(where: { $0.companyCd == it.binResults.companyCd && $0.userId == it.binResults.userId && $0.allocationNo == it.binResults.allocationNo }) {
                    br.append(it.binResults)
                }
                
                if it.workResults != nil {
                    wr.append(it.workResults!)
                }
            }
            
            if !br.isEmpty || !wr.isEmpty {
                let b = BinPost(bin: br, work: wr, client: Config.Shared.clientInfo)
                Config.Shared.post.postBin(binPost: b)
                    .receive(on: DispatchQueue.main)
                    .sink(receiveCompletion: { err in
                        self.handleErr(err: err, callBack: self.callBackBin!)
                    }, receiveValue: { res in
                        
                        self.binRetryTimes = self.binRetryTimes - 1
                        if res.result || self.binRetryTimes == 0 {
                            do
                            {
                                try Config.Shared.resultDb?.instanceDb?.write { db in
                                    for b in br {
                                        try b.delete(db)
                                    }
                                    
                                    for w in wr {
                                        try w.delete(db)
                                    }
                                }
                            } catch {
                                debugPrint(error)
                            }
                            
                            self.binRetryTimes = 5
                            self.binProcess = .fetch
                        } else {
                            Helper.Shared.sendBroadcastNotification(.Bin)
                            self.callBackBin!(true, nil)
                        }
                    }).store(in: &bag)
            } else {
                self.binProcess = .fetch
            }
        } else {
            self.binProcess = .fetch
        }
    }
    
    private func fetchBin() {
        // fetch
        Config.Shared.fetch.binData(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                self.handleErr(err: err, callBack: self.callBackBin!)
            }, receiveValue: { res in
                
                do {
                    let oldBinIdList =  Config.Shared.userDb?.binDetailDao?.safeToDeleteList()
                    let oldBinHeader =  Config.Shared.userDb?.binHeaderDao?.safeToDeleteList()
                    
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        
                        // BinHeader
                        for it in oldBinHeader! {
                            try it.delete(db)
                        }
                        if res.binHeaders.count > 0 {
                            for bh in res.binHeaderList() {
                                try bh.save(db)
                            }
                        }
                        
                        // BinDetail
                        for it in oldBinIdList! {
                            try it.delete(db)
                        }
                        try BinDetail.deleteAll(db)
                        if res.binDetails != nil && res.binDetails!.count > 0 {
                            for bd in res.binDetailList() {
                                try bd.save(db)
                            }
                        }
                    }
                    
                } catch {
                    debugPrint(error)
                }
                
                Helper.Shared.sendBroadcastNotification(.Bin)
                
                self.callBackBin!(true, nil)
            }).store(in: &bag)
    }
    
    /**
     * 荷待ち、附帯データ
     * @see [SyncApi.postIncidental]
     * @see [FetchApi.incidentalData]
     */
    private var callBackIncidental: ((Bool, NetworkError?) -> ())? = nil
    private var timeSyncincidental: Int64 = 0
    private var incidentalRetryTimes = 5
    private var incidentalProcess: SyncProcess = .none {
        didSet {
            if incidentalProcess == .post {
                postIncidental()
            } else if incidentalProcess == .fetch {
                fetchIncidental()
            }
        }
    }
    
    func syncIncidental(callBack: @escaping (Bool, NetworkError?)->()) {
        timeSyncincidental = currentTimeMillis()
        self.callBackIncidental = callBack
        // post
        incidentalProcess = .post
    }
    
    private func postIncidental() {
        try? Current.Shared.loggedUser?.commitIncidentalToCommon()
        let resultDb = Config.Shared.resultDb
        resultDb?.incidentalHeaderResultDao?.setPending()
        resultDb?.incidentalTimeResultDao?.setPending()
        
        let hp = resultDb?.incidentalHeaderResultDao?.getPending()
        let tp = resultDb?.incidentalTimeResultDao?.getPending()
        
        if (hp == nil || hp!.isEmpty) && (tp == nil || tp!.isEmpty) {
            self.incidentalProcess = .fetch
        } else {
            let ip = IncidentalPost(clientInfo: Config.Shared.clientInfo, headerList: hp!, timeList: tp!)
            Config.Shared.post.postIncidental(incidentalPost: ip)
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackIncidental!)
                }, receiveValue: { res in
                    
                    self.incidentalRetryTimes = self.incidentalRetryTimes - 1
                    if res.result || self.incidentalRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for h in hp! {
                                    try h.delete(db)
                                }
                                
                                for t in tp! {
                                    try t.delete(db)
                                }
                            }
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.incidentalRetryTimes = 5
                        self.incidentalProcess = .fetch
                    } else {
                        Helper.Shared.sendBroadcastNotification(.Incidental)
                        self.callBackIncidental!(true, nil)
                    }
                }).store(in: &bag)
        }
    }
    
    private func fetchIncidental() {
        Config.Shared.fetch.incidentalData(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                self.handleErr(err: err, callBack: self.callBackIncidental!)
            }, receiveValue: { res in
                
                do {
                    Current.Shared.userDatabase?.incidentalHeaderDao?.deleteSynced()
                    Current.Shared.userDatabase?.incidentalWorkDao?.deleteAll()
                    Current.Shared.userDatabase?.incidentalTimeDao?.deleteSynced()
                    
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        
                        if let lst = res.incidentalHeaderList() {
                            for d in lst {
                                try d.save(db)
                            }
                        }
                        
                        if let lst = res.incidentalWorkList() {
                            for d in lst {
                                try d.save(db)
                            }
                        }
                        
                        if let lst = res.incidentalTimeList() {
                            for d in lst {
                                try d.save(db)
                            }
                        }
                    }
                    
                } catch {
                    debugPrint(error)
                }
                
                Helper.Shared.sendBroadcastNotification(.Incidental)
                
                self.callBackIncidental!(true, nil)
            }).store(in: &bag)
    }
    
    /**
     * 位置のデータ（運行）
     * @see [SyncApi.postGeo]
     */
    private var callBackGeo: ((Bool, NetworkError?) -> ())? = nil
    private var geoRetryTimes = 5
    private var geoProcess: SyncProcess = .none {
        didSet {
            if geoProcess == .post {
                postGeo()
            }
        }
    }
    
    func syncGeo(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackGeo = callBack
        // post
        geoProcess = .post
    }
    
    private func postGeo() {
        Config.Shared.resultDb?.coordinateDao?.setPending()
        let pending = Config.Shared.resultDb?.coordinateDao?.getPending()
        if pending != nil && pending!.count > 0 {
            let g = Geo(coordinates: pending!, clientInfo: Config.Shared.clientInfo)
            Config.Shared.post.postGeo(geo: g)
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackGeo!)
                }, receiveValue: { res in
                    self.geoRetryTimes = self.geoRetryTimes - 1
                    if res.result || self.geoRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for it in pending! {
                                    try it.delete(db)
                                }
                            }
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.geoRetryTimes = 5
                        self.geoProcess = .fetch
                    } else {
                        self.callBackGeo!(true, nil)
                    }
                }).store(in: &bag)
        } else {
            self.callBackGeo!(true, nil)
        }
    }
    
    /**
     * お知らせデータ
     * @see [SyncApi.postNotice]
     * @see [FetchApi.noticeData]
     */
    private var callBackNotice: ((Bool, NetworkError?) -> ())? = nil
    private var noticeRetryTimes = 5
    private var noticeProcess: SyncProcess = .none {
        didSet {
            if noticeProcess == .post {
                postNotice()
            } else if noticeProcess == .fetch {
                fetchNotice()
            }
        }
    }
    
    func syncNotice(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackNotice = callBack
        // post
        noticeProcess = .post
    }
    
    private func postNotice() {
        try? Current.Shared.loggedUser?.commitNoticeToCommon()
        Config.Shared.resultDb?.noticeDao?.setPending()
        let lstCommonNoticePending = Config.Shared.resultDb?.noticeDao?.getPending()
        
        if lstCommonNoticePending != nil && lstCommonNoticePending!.count > 0 {
            Config.Shared.post.postNotice(noticePost: NoticePost(notices: lstCommonNoticePending!, clientInfo: Config.Shared.clientInfo))
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackNotice!)
                }, receiveValue: { res in
                    self.noticeRetryTimes = self.noticeRetryTimes - 1
                    if res.result || self.noticeRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for notice in lstCommonNoticePending! {
                                    try notice.delete(db)
                                }
                            }
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.noticeRetryTimes = 5
                        self.noticeProcess = .fetch
                    } else {
                        self.callBackNotice!(true, nil)
                    }
                }).store(in: &bag)
        } else {
            self.noticeProcess = .fetch
        }
    }
    
    private func fetchNotice() {
        // fetch
        Config.Shared.fetch.notice(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                self.handleErr(err: err, callBack: self.callBackNotice!)
            }, receiveValue: { res in
                
                do {
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        // Notice
                        try Notice.deleteAll(db)
                        if res.notices != nil && res.notices!.count > 0 {
                            for no in res.notices! {
                                try no.insert(db)
                            }
                        }
                    }
                    
                } catch {
                    
                }
                
                Helper.Shared.sendBroadcastNotification(.Notice)
                self.callBackNotice!(true, nil)
            }).store(in: &bag)
    }
    
    /**
     * 数回送信失敗したのデータ、送信
     * @see [SyncApi.postFailedData]
     */
    private var callBackFailedData: ((Bool, NetworkError?) -> ())? = nil
    private var failedDataProcess: SyncProcess = .none {
        didSet {
            if failedDataProcess == .post {
                postFailedData()
            }
        }
    }
    
    func syncFailedData(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackFailedData = callBack
        // post
        failedDataProcess = .post
    }
    
    private func postFailedData() {
        
    }
    
    /**
     * イメージ画像（署名画像） 送信
     * @see [SyncApi.uploadImgData]
     */
    private var callBackImgData: ((Bool, NetworkError?) -> ())? = nil
    private var imgDataProcess: SyncProcess = .none {
        didSet {
            if imgDataProcess == .post {
                postImgData()
            }
        }
    }
    
    func syncImgData(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackImgData = callBack
        // post
        imgDataProcess = .post
    }
    
    private func postImgData() {
        let o = Config.Shared.imageDb?.imageSendingDao?.selectAny()
        if o != nil {
            let picPost = PicPost(clientInfo: Config.Shared.clientInfo, imageSending: o!)
            Config.Shared.post.uploadImgdata(picPost: picPost)
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackImgData!)
                }, receiveValue: { res in
                    if res.result {
                        Config.Shared.imageDb?.imageSendingDao?.deleteByPicId(picId: o!.picId)
                    } else {
                        debugPrint(res.message ?? "")
                    }
                    
                    self.callBackImgData!(true, nil)
                }).store(in: &bag)
        } else {
            self.callBackImgData!(true, nil)
        }
    }
    
    /**
     *
     * @see [SyncApi.uploadDeliveryChartData]
     */
    private var callBackDeliveryChart: ((Bool, NetworkError?) -> ())? = nil
    private var timeSyncDeliveryChart: Int64 = 0
    private var deliveryChartRetryTimes = 5
    private var deliveryChartProcess: SyncProcess = .none {
        didSet {
            if deliveryChartProcess == .post {
                postDeliveryChart()
            } else if deliveryChartProcess == .fetch {
                fetchDeliveryChart()
            }
        }
    }
    
    func syncDeliveryChart(callBack: @escaping (Bool, NetworkError?)->()) {
        timeSyncDeliveryChart = currentTimeMillis()
        Helper.Shared.stopURLSessionByUrl(lstEndpoint: [.postDeliveryChart])
        
        self.callBackDeliveryChart = callBack
        // post
        deliveryChartProcess = .post
    }
    var doneCount:Int = 0
    private func postDeliveryChart() {
        Current.Shared.loggedUser?.commitdeliveryChartToCommon()
        //let userDb = Config.Shared.userDb!
        let resultDb = Config.Shared.resultDb!
        
        resultDb.commonDeliveryChartDao?.setPending()
        let cmCharts: [CommonDeliveryChart]? = resultDb.commonDeliveryChartDao?.getPending()
        if cmCharts != nil && cmCharts?.count != 0 {
            for cmChart in cmCharts! {
                let jsonData = DeliveryChartPost(chart: cmChart)
                Config.Shared.post.postDeliveryChart(data: jsonData)
                    .receive(on: DispatchQueue.main)
                    .sink(receiveCompletion: { err in
                        self.handleErr(err: err, callBack: self.callBackDeliveryChart!)
                    }, receiveValue: { [self] res in
                        self.deliveryChartRetryTimes = self.deliveryChartRetryTimes - 1
                        if res.isEmpty || self.deliveryChartRetryTimes == 0 {
                            //doneCount += 1
                            do
                            {
                                _ = try Config.Shared.resultDb?.instanceDb?.write { db in
                                    try cmChart.delete(db)
                                }
                                
                                //if (doneCount == cmCharts?.count) {
                                    resultDb.commonDeliveryChartDao?.setPending()
                                    let cmChartsCheck: [CommonDeliveryChart]? = resultDb.commonDeliveryChartDao?.getPending()
                                    if cmChartsCheck == nil || cmChartsCheck?.count == 0 {
                                        try Config.Shared.commonChartSyncDir.deleteAll()
                                    }
                                    //doneCount = 0
                                //}
                            } catch {
                                debugPrint(error)
                            }
                            
                            self.deliveryChartRetryTimes = 5
                            self.deliveryChartProcess = .fetch
                        } else {
                            Helper.Shared.sendBroadcastNotification(.DeliveryChart)
                            self.callBackDeliveryChart!(true, nil)
                        }
                    }).store(in: &bag)
            }
        } else {
            self.deliveryChartProcess = .fetch
        }
    }
    
    private func fetchDeliveryChart() {
        if (Current.Shared.loggedUser == nil) {
            return
        }
        // fetch
        Config.Shared.fetch.deliveryChartData(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                self.handleErr(err: err, callBack: self.callBackDeliveryChart!)
            }, receiveValue: { res in
                
                do {
                    Config.Shared.userDb?.deliveryChartDao?.deleteSynced()
                    let count = Config.Shared.userDb?.deliveryChartDao?.count()
                    if (count == 0) {
                        try Current.Shared.loggedUser?.userChartSyncDir.deleteAll()
                    }
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        let charts = res.charts()
                        if charts != nil {
                            for b in charts! {
                                b.sync = 2
                                try b.insert(db)
                            }
                        }
                    }
                } catch {
                    debugPrint(error)
                }
                self.callBackDeliveryChart!(true, nil)
            }).store(in: &bag)
    }
    
    /**
     * 集配物、回収物
     * @see [SyncApi.postCollection]
     */
    private var callBackCollection: ((Bool, NetworkError?) -> ())? = nil
    private var timeSyncCollection: Int64 = 0
    private var collectionRetryTimes = 5
    private var collectionProcess: SyncProcess = .none {
        didSet {
            if collectionProcess == .post {
                postCollection()
            } else if collectionProcess == .fetch {
                fetchCollection()
            }
        }
    }
    
    func syncCollection(callBack: @escaping (Bool, NetworkError?)->()) {
        timeSyncCollection = currentTimeMillis()
        Helper.Shared.stopURLSessionByUrl(lstEndpoint: [.postCollection, .binData])
        
        self.callBackCollection = callBack
        // post
        collectionProcess = .post
    }
    
    private func postCollection() {
        Current.Shared.loggedUser?.commitCollectionResultToCommon()
        
        let resultDb = Config.Shared.resultDb!
        
        resultDb.collectionResultDao?.setPending()
        let l = resultDb.collectionResultDao?.getPending()
        let u = Current.Shared.loggedUser?.userInfo
        
        if l != nil {
            Config.Shared.post.postCollection(collectionPost: CollectionPost(userInfo: u, collections: l!))
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackCollection!)
                }, receiveValue: { res in
                    self.collectionRetryTimes = self.collectionRetryTimes - 1
                    if res.result || self.collectionRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for it in l! {
                                    try it.delete(db)
                                }
                            }
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.collectionRetryTimes = 5
                        self.collectionProcess = .fetch
                    } else {
                        Helper.Shared.sendBroadcastNotification(.Collection)
                        self.callBackCollection!(true, nil)
                    }
                }).store(in: &bag)
        } else {
            self.collectionProcess = .fetch
        }
    }
    
    private func fetchCollection() {
        // fetch
        Config.Shared.fetch.binData(loginInfo: Current.Shared.loggedUser!)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                self.handleErr(err: err, callBack: self.callBackCollection!)
            }, receiveValue: { res in
                
                do {
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        
                        // CollectionResult
                        if res.binCollections != nil {
                            try CollectionResult.deleteAll(db)
                            for b in res.collectionResults() {
                                try b.insert(db)
                            }
                        }
                    }
                } catch {
                    debugPrint(error)
                }
                
                Helper.Shared.sendBroadcastNotification(.Collection)
                
                self.callBackCollection!(true, nil)
            }).store(in: &bag)
    }
    
    /**
     * Logファイル送信
     * @see [SyncApi.postLogFile]
     */
    private var callBackLogFile: ((Bool, NetworkError?) -> ())? = nil
    private var logFileProcess: SyncProcess = .none {
        didSet {
            if logFileProcess == .post {
                postLogFile()
            }
        }
    }
    
    func syncLogFile(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackLogFile = callBack
        // post
        logFileProcess = .post
    }
    
    private func postLogFile() {
        
    }
    
    private var sendSensorCsvTask: SendSensorCsvTask = SendSensorCsvTask(1)
    private class SendSensorCsvTask: Task2<Void> {
        override func source() -> AnyPublisher<Any, NetworkError> {
            let resultDb = Config.Shared.resultDb!
            let o = resultDb.commonSensorCsvDao?.selectAny()
            if (o == nil) {
                return Just(true)
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            } else {
                let f = "\(o!.companyCd)-\(o!.userId)-\(o!.allocationNo)-\(UUID().uuidString).csv"
                return FileRepository().uploadSensorCsv(filename: f, body: o!.csv)
                    .map { _ in o! as Any}
                    .eraseToAnyPublisher()
            }
        }
        
        override func doOnSuccessSource(_ rs: Any) {
            if let o = rs as? CommonSensorCsv {
                do {
                    _ = try Config.Shared.resultDb?.instanceDb?.write { db in
                        try o.delete(db)
                    }
                } catch {
                    debugPrint(error)
                }
            }
        }
    }
    
    func syncSensorCsvUpload() {
        sendSensorCsvTask.execute()
    }
    
    /**
     * 休憩検知
     * @see [SyncApi.postRest]
     */
    private var callBackRest: ((Bool, NetworkError?) -> ())? = nil
    private var restRetryTimes = 5
    private var restProcess: SyncProcess = .none {
        didSet {
            if restProcess == .post {
                postRest()
            }
        }
    }
    
    func syncRest(callBack: @escaping (Bool, NetworkError?)->()) {
        self.callBackRest = callBack
        // post
        restProcess = .post
    }
    
    private func postRest() {
        let pending = Config.Shared.resultDb?.commonRestDao?.setThenGetPending()
        
        if pending != nil && pending!.count > 0 {
            let rest = RestPost(rests: pending!, clientInfo: Config.Shared.clientInfo)
            Config.Shared.post.postRest(restPost: rest)
                .receive(on: DispatchQueue.main)
                .sink(receiveCompletion: { err in
                    self.handleErr(err: err, callBack: self.callBackRest!)
                }, receiveValue: { res in
                    self.restRetryTimes = self.restRetryTimes - 1
                    if res.result || self.restRetryTimes == 0 {
                        do
                        {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                for it in pending! {
                                    try it.delete(db)
                                }
                            }
                        } catch {
                            debugPrint(error)
                        }
                        
                        self.restRetryTimes = 5
                    } else {
                        self.callBackRest!(true, nil)
                    }
                }).store(in: &bag)
        } else {
            self.callBackRest!(true, nil)
        }
    }
}
