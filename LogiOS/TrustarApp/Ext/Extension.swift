//
//  Extension.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import UIKit
import SwiftUI
import Combine
import CoreLocation
import Photos
import AVFoundation

extension UIColor {
    convenience init(red: Int,green: Int,blue: Int) {
        self.init(red: CGFloat(red) / 255.0, green: CGFloat(green) / 255.0, blue: CGFloat(blue) / 255.0, alpha: 1.0)
    }
    
    convenience init(rgb: Int) {
        self.init(
            red: (rgb >> 16) & 0xFF,
            green: (rgb >> 8) & 0xFF,
            blue: rgb & 0xFF
        )
    }
    
    convenience init(rgbText: String) {
        let rgbSplit = rgbText.split(separator: ",")
        
        if rgbSplit.count == 3 {
            self.init(red: Int.init(rgbSplit[0])!, green: Int.init(rgbSplit[1])!, blue: Int.init(rgbSplit[2])!)
        } else {
            self.init()
        }
    }
}

extension UITextField {
    func addBootomBorder() {
        let btLine = CALayer()
        btLine.frame = CGRect(x: 0, y: self.frame.size
            .height - 1, width: self.frame.size
            .width, height: 1)
        btLine.backgroundColor = UIColor.white.cgColor
        borderStyle = .none
        layer.addSublayer(btLine)
    }
}

extension Binding {
    /// When the `Binding`'s `wrappedValue` changes, the given closure is executed.
    /// - Parameter closure: Chunk of code to execute whenever the value changes.
    /// - Returns: New `Binding`.
    func onUpdate(_ closure: @escaping () -> Void) -> Binding<Value> {
        Binding(get: {
            wrappedValue
        }, set: { newValue in
            wrappedValue = newValue
            closure()
        })
    }
}

extension Notification.Name {
    static let my_onViewWillTransition = Notification.Name("MainUIHostingController_viewWillTransition")
}

class MyUIHostingController<Content> : UIHostingController<Content> where Content : View {
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        NotificationCenter.default.post(name: .my_onViewWillTransition, object: nil, userInfo: ["size": size])
        super.viewWillTransition(to: size, with: coordinator)
    }
    
}

extension Color {
    var uiColor: UIColor? {
        if #available(iOS 14.0, *) {
            return UIColor(self)
        } else {
            let scanner = Scanner(string: self.description.trimmingCharacters(in: CharacterSet.alphanumerics.inverted))
            var hexNumber: UInt64 = 0
            var r: CGFloat = 0.0, g: CGFloat = 0.0, b: CGFloat = 0.0, a: CGFloat = 0.0
            let result = scanner.scanHexInt64(&hexNumber)
            if result {
                r = CGFloat((hexNumber & 0xff000000) >> 24) / 255
                g = CGFloat((hexNumber & 0x00ff0000) >> 16) / 255
                b = CGFloat((hexNumber & 0x0000ff00) >> 8) / 255
                a = CGFloat(hexNumber & 0x000000ff) / 255
                return UIColor(red: r, green: g, blue: b, alpha: a)
            } else {
                return nil
            }
        }
    }
}

struct AdaptsToKeyboard: ViewModifier {
    @State var currentHeight: CGFloat = 0
    
    func body(content: Content) -> some View {
        GeometryReader { geometry in
            content
                .padding(.bottom, self.currentHeight)
                .onAppear(perform: {
                    NotificationCenter.Publisher(center: NotificationCenter.default, name: UIResponder.keyboardWillShowNotification)
                        .merge(with: NotificationCenter.Publisher(center: NotificationCenter.default, name: UIResponder.keyboardWillChangeFrameNotification))
                        .compactMap { notification in
                            withAnimation(.easeOut(duration: 0.16)) {
                                notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect
                            }
                        }
                        .map { rect in
                            rect.height - geometry.safeAreaInsets.bottom
                        }
                        .subscribe(Subscribers.Assign(object: self, keyPath: \.currentHeight))
                    
                    NotificationCenter.Publisher(center: NotificationCenter.default, name: UIResponder.keyboardWillHideNotification)
                        .compactMap { notification in
                            CGFloat.zero
                        }
                        .subscribe(Subscribers.Assign(object: self, keyPath: \.currentHeight))
                })
        }
    }
}

extension UIDevice {
    /// Returns `true` if the device has a notch
    var hasNotch: Bool {
        guard #available(iOS 11.0, *), let window = UIApplication.shared.windows.filter({$0.isKeyWindow}).first else { return false }
        if UIApplication.shared.windows.first?.windowScene?.interfaceOrientation.isPortrait == true {
            return window.safeAreaInsets.top >= 44
        } else {
            return window.safeAreaInsets.left > 0 || window.safeAreaInsets.right > 0
        }
    }
}

extension UIImage {
    func resize(scale: CGFloat) -> UIImage {
        let updateSize = CGSize(width:self.size.width * scale, height:self.size.height * scale)
        UIGraphicsBeginImageContextWithOptions(updateSize, false, UIScreen.main.scale)
        self.draw(in: CGRect(origin: .zero, size: updateSize))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage ?? UIImage()
    }
    
    convenience init?(color: UIColor, size: CGSize = CGSize(width: 1, height: 1)) {
        let rect = CGRect(origin: .zero, size: size)
        UIGraphicsBeginImageContextWithOptions(rect.size, false, 0.0)
        color.setFill()
        UIRectFill(rect)
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        guard let cgImage = image?.cgImage else { return nil }
        self.init(cgImage: cgImage)
    }
}

extension View {
    func onSwipe(action: @escaping () -> Void) -> some View {
        return self.modifier(SwipeToAction(action: action))
    }
    
    func onSwipeToBack(isPresented: Binding<Bool>,
                       action: @escaping () -> Void) -> some View {
        return self.modifier(SwipeToBack(isPresented: isPresented, action: action))
    }
    
    func adaptsToKeyboard() -> some View {
        return modifier(AdaptsToKeyboard())
    }
    
    func tabViewStyle(backgroundColor: Color? = nil,
                      itemColor: Color? = nil,
                      selectedItemColor: Color? = nil,
                      badgeColor: Color? = nil) -> some View {
        onAppear {
            let itemAppearance = UITabBarItemAppearance()
            if let uiItemColor = itemColor?.uiColor {
                itemAppearance.normal.iconColor = uiItemColor
                itemAppearance.normal.titleTextAttributes = [
                    .foregroundColor: uiItemColor
                ]
            }
            if let uiSelectedItemColor = selectedItemColor?.uiColor {
                itemAppearance.selected.iconColor = uiSelectedItemColor
                itemAppearance.selected.titleTextAttributes = [
                    .foregroundColor: uiSelectedItemColor
                ]
            }
            if let uiBadgeColor = badgeColor?.uiColor {
                itemAppearance.normal.badgeBackgroundColor = uiBadgeColor
                itemAppearance.selected.badgeBackgroundColor = uiBadgeColor
            }
            
            let appearance = UITabBarAppearance()
            if let uiBackgroundColor = backgroundColor?.uiColor {
                appearance.backgroundColor = uiBackgroundColor
            }
            
            appearance.stackedLayoutAppearance = itemAppearance
            appearance.inlineLayoutAppearance = itemAppearance
            appearance.compactInlineLayoutAppearance = itemAppearance
            
            UITabBar.appearance().standardAppearance = appearance
            if #available(iOS 15.0, *) {
                UITabBar.appearance().scrollEdgeAppearance = appearance
            }
        }
    }
    
    func endTextEditing() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder),
                                        to: nil, from: nil, for: nil)
    }
    
    @ViewBuilder
    func `if`<Transform: View>(_ condition: Bool, transform: (Self) -> Transform) -> some View {
        if condition { transform(self) }
        else { self }
    }
    
    @ViewBuilder func refreshableCompat<Progress: View>(showsIndicators: Bool = true,
                                                        loadingViewBackgroundColor: Color = defaultLoadingViewBackgroundColor,
                                                        threshold: CGFloat = defaultRefreshThreshold,
                                                        onRefresh: @escaping OnRefresh,
                                                        @ViewBuilder progress: @escaping RefreshProgressBuilder<Progress>) -> some View {
        self.modifier(RefreshableCompat(showsIndicators: showsIndicators,
                                        loadingViewBackgroundColor: loadingViewBackgroundColor,
                                        threshold: threshold,
                                        onRefresh: onRefresh,
                                        progress: progress))
    }
    
    public func flip() -> some View {
        return self
            .rotationEffect(.radians(.pi))
            .scaleEffect(x: -1, y: 1, anchor: .center)
    }
    
    func modify<Content>(@ViewBuilder _ transform: (Self) -> Content) -> Content {
        transform(self)
    }
}

extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners
    
    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

extension Optional where Wrapped == String {
    func nullToEmpty() -> String {
        if self == nil {
            return Resources.strEmpty
        }
        else {
            return self!
        }
    }
    
    func dateFromStringT() -> Date? {
        if self == nil {
            return nil
        }
        
        return Config.Shared.dateFormatter.parse(text: self!)
    }
    
    func dateFromString2() -> Date? {
        if self == nil {
            return nil
        }
        
        return Config.Shared.dateFormatter2.parse(text: self!)
    }
    
    func orEmpty() -> String {
        if self != nil {
            return self!
        }
        
        return Resources.strEmpty
    }
}

extension Optional where Wrapped == Int {
    func takeIf(_ condition: () -> (Bool)) -> Int? {
        if condition() {
            return self
        }
        
        return nil
    }
    
    func toString() -> String? {
        if self != nil {
            return String(self!)
        }
        
        return nil
    }
}

extension Optional where Wrapped == Double {
    func toString() -> String? {
        if self != nil {
            return String(self!)
        }
        
        return nil
    }
}

extension String {
    func hms60From4dig(_ defaultVl: Int? = nil) -> Int? {
        if self.count == 4 {
            let charArr = Array(self)
            let m1 = charArr[0].wholeNumberValue
            let m2 = charArr[1].wholeNumberValue
            if m1 != nil && m2 != nil {
                let s1 = charArr[2].wholeNumberValue
                let s2 = charArr[3].wholeNumberValue
                if s1 != nil && s2 != nil {
                    return (m1! * 10 + m2!) * 60 + s1! * 10 + s2!
                }
            }
        }
        
        return defaultVl
    }
    
    enum TrimmingOptions {
        case all
        case leading
        case trailing
        case leadingAndTrailing
    }
    
    func trimming(spaces: TrimmingOptions, using characterSet: CharacterSet = .whitespacesAndNewlines) ->  String {
        switch spaces {
        case .all: return trimmingAllSpaces(using: characterSet)
        case .leading: return trimingLeadingSpaces(using: characterSet)
        case .trailing: return trimingTrailingSpaces(using: characterSet)
        case .leadingAndTrailing:  return trimmingLeadingAndTrailingSpaces(using: characterSet)
        }
    }
    
    private func trimingLeadingSpaces(using characterSet: CharacterSet) -> String {
        guard let index = firstIndex(where: { !CharacterSet(charactersIn: String($0)).isSubset(of: characterSet) }) else {
            return self
        }
        
        return String(self[index...])
    }
    
    private func trimingTrailingSpaces(using characterSet: CharacterSet) -> String {
        guard let index = lastIndex(where: { !CharacterSet(charactersIn: String($0)).isSubset(of: characterSet) }) else {
            return self
        }
        
        return String(self[...index])
    }
    
    private func trimmingLeadingAndTrailingSpaces(using characterSet: CharacterSet) -> String {
        return trimmingCharacters(in: characterSet)
    }
    
    private func trimmingAllSpaces(using characterSet: CharacterSet) -> String {
        return components(separatedBy: characterSet).joined()
    }
    
    var length: Int {
        return count
    }
    
    subscript (i: Int) -> String {
        return self[i ..< i + 1]
    }
    
    func substring(fromIndex: Int) -> String {
        return self[min(fromIndex, length) ..< length]
    }
    
    func substring(toIndex: Int) -> String {
        return self[0 ..< max(0, toIndex)]
    }
    
    subscript (r: Range<Int>) -> String {
        let range = Range(uncheckedBounds: (lower: max(0, min(length, r.lowerBound)),
                                            upper: min(length, max(0, r.upperBound))))
        let start = index(startIndex, offsetBy: range.lowerBound)
        let end = index(start, offsetBy: range.upperBound - range.lowerBound)
        return String(self[start ..< end])
    }
}

extension Double {
    func roundToStringDecimal1() -> String {
        let round1 = Double((self * 10).rounded(.toNearestOrEven) / 10.0)
        let intValue = Int(round1)
        
        return (round1 - Double(intValue) == 0) ? "\(intValue)" : "\(round1)"
    }
}

extension FixedWidthInteger {
    var inv: Self {
        var v = self
        var s = Self(v.bitWidth)
        precondition(s.nonzeroBitCount == 1, "Bit width must be a power of two")
        var mask = ~Self(0)
        repeat  {
            s = s >> 1
            mask ^= mask << s
            v = ((v >> s) & mask) | ((v << s) & ~mask)
        } while s > 1
        return v
    }
}

extension TimeZone {
    
    // TODO: CuongNV: Dont understand this function
    func calendar() -> Calendar {
        var calendar = Calendar.current
        calendar.timeZone = self
        return calendar
    }
    
    func millisAfterOffset(millis: Int64) -> Int64 {
        let mSecond = Int64(self.secondsFromGMT() * 1000)
        return mSecond + millis
    }
}

extension Date {
    var milisecondsSince1970: Int64 {
        Int64((self.timeIntervalSince1970 * 1000.0).rounded())
    }
    
    init(miliseconds: Int64) {
        self = Date(timeIntervalSince1970: TimeInterval(miliseconds) / 1000)
    }
}


func displayHHmmRange(from: Clock, to: Clock?) -> String? {
    return "\(from.hhmm) 〜 \(to == nil ? Resources.strEmpty : to!.hhmm)"
}

func displayHHmmRange(from: Int64, to: Int64?) -> String? {
    return "\(Config.Shared.dateFormatterHHmm.format(date: Date(timeIntervalSince1970: TimeInterval(from / 1000)))) 〜 \(to == nil ? Resources.strEmpty : Config.Shared.dateFormatterHHmm.format(date: Date(timeIntervalSince1970: TimeInterval(to! / 1000))))"
}

func currentTimeMillis() -> Int64 {
    return Int64(NSDate().timeIntervalSince1970 * 1000)
}

func daysBetween(from: Int64, to: Int64) -> Int {
    return Int(to / 86400000) - Int(from / 86400000)
}

/**
 * fast calculate.
 * @see String.format
 */
func fastHHMMString(minutes: Int, showPositive: Bool = false) -> String {
    let isN = minutes < 0
    let am = isN ? -minutes : minutes
    let h = am / 60
    let m = am - h * 60
    
    var valueReturn: String = String()
    
    if isN {
        valueReturn.append("-")
    } else if showPositive {
        valueReturn.append("+")
    }
    
    if h >= 0 && h <= 9 {
        valueReturn.append("0")
    }
    valueReturn.append(String(h))
    valueReturn.append(":")
    
    if m >= 0 && m <= 9 {
        valueReturn.append("0")
    }
    valueReturn.append(String(m))
    
    return valueReturn
}

func shouldSkipLocation(range: Int, current: CLLocation, previous: CLLocation?, lastRecorded: CLLocation?) -> Bool {
    if lastRecorded == nil || previous == nil {
        return false
    }
    
    if Int(lastRecorded!.distance(from: current)) > range {
        return false
    }
    
    if lastRecorded != previous {
        return Int(previous!.distance(from: current)) <= range
    }
    
    return true
}

func shouldSkipCoordinateRecord(
    timeSampleInSec: Int,
    current: CLLocation,
    lastRecorded: CLLocation?
) -> Bool {
    if lastRecorded == nil { return false }
    let notLessThan = lastRecorded!.timestamp.milisecondsSince1970 + Int64(timeSampleInSec * 1000)
    let currentTime = current.timestamp.milisecondsSince1970
    return currentTime < notLessThan
}

extension UIDevice {
    static func vibrate() {
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
    }
}

extension URL {
    func createSubDirectory(name: String, removeBeforeCreate: Bool = false) -> URL {
        let subDir = self.appendingPathComponent(name, isDirectory: true)
        
        if FileManager.default.fileExists(atPath: subDir.path) {
            if let isDir = (try? subDir.resourceValues(forKeys: [.isDirectoryKey]))?.isDirectory {
                if isDir {
                    if removeBeforeCreate {
                        try? FileManager.default.removeItem(at: subDir)
                    } else {
                        return subDir
                    }
                } else {
                    try? FileManager.default.removeItem(at: subDir)
                }
            }
        }
        
        do {
            try FileManager.default.createDirectory(
                at: subDir,
                withIntermediateDirectories: true,
                attributes: nil
            )
        } catch {
            debugPrint(error)
        }
        
        return subDir
    }
    
    func createFile(name: String) -> URL {
        let path = self.appendingPathComponent(name, isDirectory: false)
        if FileManager.default.fileExists(atPath: path.path) {
            _ = path.delete()
        }
        
        FileManager.default.createFile(atPath: path.path, contents: nil)
        
        return path
    }
    
    func canRead() -> Bool {
        return FileManager.default.isReadableFile(atPath: self.path)
    }
    
    func moveOrCopy(to: URL) -> Bool {
        do {
            try FileManager.default.moveItem(at: self, to: to)
        } catch {
            debugPrint("moveItem: \(error)")
            
            do {
                try FileManager.default.copyItem(at: self, to: to)
            } catch {
                debugPrint("copyItem: \(error)")
                return false
            }
        }
        
        return true
    }
    
    func delete() -> Bool {
        do {
            if FileManager.default.fileExists(atPath: self.path) {
                try FileManager.default.removeItem(at: self)
            }
        } catch {
            debugPrint(error)
            return false
        }
        
        return true
    }
    
    func decodeImageSize() -> CGSize {
        if let image = UIImage(contentsOfFile: self.path) {
            return CGSize(width: image.size.width, height: image.size.height)
        }
        
        return CGSize(width: 0, height: 0)
    }
}

// Get user's documents directory path
func getDocumentDirectoryPath() -> URL {
    let arrayPaths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
    let docDirectoryPath = arrayPaths[0]
    return docDirectoryPath
}

// Get user's cache directory path
func getCacheDirectoryPath() -> URL {
    let arrayPaths = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)
    let cacheDirectoryPath = arrayPaths[0]
    return cacheDirectoryPath
}

// Get user's temp directory path
func getTempDirectoryPath() -> URL {
    let tempDirectoryPath = URL(fileURLWithPath: NSTemporaryDirectory(), isDirectory: true)
    return tempDirectoryPath
}

func pathOfUserHome(companyCd: String, userId: String) -> URL {
    let relativePath = "\(companyCd)/\(userId)/"
    return Config.Shared.userDir.createSubDirectory(name: relativePath)
}

func humanReadableSize(value: Int64) -> String {
    if value < 1024 { return "\(value) B" }
    let z = (63 - value.leadingZeroBitCount) / 10
    return String(format: "%.1f %@iB", Float(value) / Float(Int64(1) << (z * 10)), " KMGTPE"[z])
}

func readToTmpFile(uri: URL, dir: URL) -> URL? {
    let nameFile = uri.lastPathComponent
    
    let des = dir.appendingPathComponent(nameFile, isDirectory: false)
    do {
        if FileManager.default.fileExists(atPath: des.path) {
            try FileManager.default.removeItem(at: des)
        }
        
        try FileManager.default.copyItem(at: uri, to: des)
        return des
    } catch {
        debugPrint(error)
        return nil
    }
}

func timeString() -> String {
    return Config.Shared
        .timeFormatter
        .format(date: Date(miliseconds: currentTimeMillis()))
}

extension PHAsset {
    
    func getURL(completionHandler : @escaping ((_ responseURL : URL?) -> Void)){
        if self.mediaType == .image {
            let options: PHContentEditingInputRequestOptions = PHContentEditingInputRequestOptions()
            options.canHandleAdjustmentData = {(adjustmeta: PHAdjustmentData) -> Bool in
                return true
            }
            self.requestContentEditingInput(with: options, completionHandler: {(contentEditingInput: PHContentEditingInput?, info: [AnyHashable : Any]) -> Void in
                completionHandler(contentEditingInput!.fullSizeImageURL as URL?)
            })
        } else if self.mediaType == .video {
            let options: PHVideoRequestOptions = PHVideoRequestOptions()
            options.version = .original
            PHImageManager.default().requestAVAsset(forVideo: self, options: options, resultHandler: {(asset: AVAsset?, audioMix: AVAudioMix?, info: [AnyHashable : Any]?) -> Void in
                if let urlAsset = asset as? AVURLAsset {
                    let localVideoUrl: URL = urlAsset.url as URL
                    completionHandler(localVideoUrl)
                } else {
                    completionHandler(nil)
                }
            })
        }
    }
}

extension Array {
    func lastIndex() -> Int {
        return self.count - 1
    }
    
    func getOrNull(index: Int) -> Self.Element? {
        let lastIdx = self.lastIndex()
        
        if (index >= 0 && index <= lastIdx)
        {
            return self[index]
        } else {
            return nil
        }
    }
    
    func secondToLast() -> Self.Element? {
        let lastIdx = self.lastIndex()
        return getOrNull(index: lastIdx - 1)
    }
}

extension Publisher {
    
    /// Includes the current element as well as the previous element from the upstream publisher in a tuple where the previous element is optional.
    /// The first time the upstream publisher emits an element, the previous element will be `nil`.
    ///
    ///     let range = (1...5)
    ///     cancellable = range.publisher
    ///         .withPrevious()
    ///         .sink { print ("(\($0.previous), \($0.current))", terminator: " ") }
    ///      // Prints: "(nil, 1) (Optional(1), 2) (Optional(2), 3) (Optional(3), 4) (Optional(4), 5) ".
    ///
    /// - Returns: A publisher of a tuple of the previous and current elements from the upstream publisher.
    func withPrevious() -> AnyPublisher<(previous: Output?, current: Output), Failure> {
        scan(Optional<(Output?, Output)>.none) { ($0?.1, $1) }
            .compactMap { $0 }
            .eraseToAnyPublisher()
    }
    
    /// Includes the current element as well as the previous element from the upstream publisher in a tuple where the previous element is not optional.
    /// The first time the upstream publisher emits an element, the previous element will be the `initialPreviousValue`.
    ///
    ///     let range = (1...5)
    ///     cancellable = range.publisher
    ///         .withPrevious(0)
    ///         .sink { print ("(\($0.previous), \($0.current))", terminator: " ") }
    ///      // Prints: "(0, 1) (1, 2) (2, 3) (3, 4) (4, 5) ".
    ///
    /// - Parameter initialPreviousValue: The initial value to use as the "previous" value when the upstream publisher emits for the first time.
    /// - Returns: A publisher of a tuple of the previous and current elements from the upstream publisher.
    func withPrevious(_ initialPreviousValue: Output) -> AnyPublisher<(previous: Output, current: Output), Failure> {
        scan((initialPreviousValue, initialPreviousValue)) { ($0.1, $1) }.eraseToAnyPublisher()
    }
}

extension CLLocation {
    func elapsedMillis (_ old: CLLocation) -> Int64 {
        return Int64((self.timestamp.timeIntervalSince(old.timestamp) * 1000.0).rounded())
    }
}

func dateBasedOnCurrentDate(realtimeNanos: Int64) -> Int64 {
    //android: Location will return elapsed realtime since system boot
    //let now = ProcessInfo.processInfo.systemUptime //in seconds (android:SystemClock.elapsedRealtimeNanos())
    //let passed = ((now * 1000) - Double(realtimeNanos))  //realtimeNanos in miliseconds
    //return currentTimeMillis() - Int64(passed)
    
    //ios: CLLocation will return timestamp, so doesn't need to convert
    return realtimeNanos
}

func downloadToFile(picId: String, toPath: URL) -> AnyPublisher<URL?, Never> {
    let future =  Future<URL?, Never> { promise in
        downloadToFile(picId: picId) { data in
            if data != nil && !data!.isEmpty {
                try? data?.write(to: toPath, options: .atomic)
                return promise(.success(toPath))
            } else {
                return promise(.success(nil))
            }
            
        }
    }
    return AnyPublisher(future)
}

private func downloadToFile(picId: String, onCompletion: @escaping (Data?) -> Void) {
    if Current.Shared.loggedUser == nil { return }
    var request = URLRequest(url: URL(string: Resources.HOST_URL + Resources.API_URL + Endpoint.getChartImage.path() + "?api_key=" + Current.Shared.loggedUser!.token)!)
    
    request.httpMethod = "POST"
    request.addValue("application/json", forHTTPHeaderField: "Content-type")
    request.addValue("\(picId)", forHTTPHeaderField: "fileId")
    request.addValue("\(Current.Shared.loggedUser!.userInfo.userId)", forHTTPHeaderField: "userId")
    request.addValue("\(Current.Shared.loggedUser!.userInfo.companyCd)", forHTTPHeaderField: "companyCd")
    
    URLSession.shared.dataTask(with: request, completionHandler: { data, response, error in
        //guard let _ = data, error == nil else { return }
        
        onCompletion(data)
    }).resume()
}
