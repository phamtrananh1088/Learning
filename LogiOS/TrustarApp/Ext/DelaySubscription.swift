//
//  DelaySubscription.swift
//  CombineExt
//
//  Created by Jack Stone on 06/03/2021.
//  Copyright © 2021 Combine Community. All rights reserved.
//
#if canImport(Combine)
import Combine
import CoreFoundation
import CoreLocation

@available(OSX 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
public extension Publisher {

    /// Time shifts the delivery of all output to the downstream receiver by delaying
    /// the time a subscriber starts receiving elements from its subscription.
    ///
    /// Note that delaying a subscription may result in skipped elements for "hot" publishers.
    /// However, this won't make a difference for "cold" publishers.
    ///
    /// - Parameter interval: The amount of delay time.
    /// - Parameter tolerance: The allowed tolerance in the firing of the delayed subscription.
    /// - Parameter scheduler: The scheduler to schedule the subscription delay on.
    /// - Parameter options: Any additional scheduler options.
    ///
    /// - Returns: A publisher with its subscription delayed.
    ///
    func delaySubscription<S: Scheduler>(for interval: S.SchedulerTimeType.Stride,
                                         tolerance: S.SchedulerTimeType.Stride? = nil,
                                         scheduler: S,
                                         options: S.SchedulerOptions? = nil) -> Publishers.DelaySubscription<Self, S> {
        return Publishers.DelaySubscription(upstream: self,
                                            interval: interval,
                                            tolerance: tolerance ?? scheduler.minimumTolerance,
                                            scheduler: scheduler,
                                            options: options)
    }
}

// MARK: - Publisher
@available(OSX 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
public extension Publishers {

    /// A publisher that delays the upstream subscription.
    struct DelaySubscription<U: Publisher, S: Scheduler>: Publisher {

        public typealias Output = U.Output      // Upstream output
        public typealias Failure = U.Failure    // Upstream failure
        /// The publisher that this publisher receives signals from.
        public let upstream: U

        /// The amount of delay time.
        public let interval: S.SchedulerTimeType.Stride

        /// The allowed tolerance in the firing of the delayed subscription.
        public let tolerance: S.SchedulerTimeType.Stride

        /// The scheduler to run the subscription delay timer on.
        public let scheduler: S

        /// Any additional scheduler options.
        public let options: S.SchedulerOptions?

        init(upstream: U,
             interval: S.SchedulerTimeType.Stride,
             tolerance: S.SchedulerTimeType.Stride,
             scheduler: S,
             options: S.SchedulerOptions?) {
            self.upstream = upstream
            self.interval = interval
            self.tolerance = tolerance
            self.scheduler = scheduler
            self.options = options
        }

        public func receive<S>(subscriber: S) where S : Subscriber, Self.Failure == S.Failure, Self.Output == S.Input {
            self.upstream.subscribe(DelayedSubscription(publisher: self, downstream: subscriber))
        }
    }
}

// MARK: - Subscription
@available(OSX 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
private extension Publishers.DelaySubscription {

    /// The delayed subscription where the scheduler advancing takes place.
    final class DelayedSubscription<D: Subscriber>: Subscriber where D.Input == Output, D.Failure == U.Failure {

        typealias Input = U.Output      // Upstream output
        typealias Failure = U.Failure   // Upstream failure
        private let interval: S.SchedulerTimeType.Stride
        private let tolerance: S.SchedulerTimeType.Stride
        private let scheduler: S
        private let options: S.SchedulerOptions?

        private let downstream: D

        init(publisher: Publishers.DelaySubscription<U, S>,
             downstream: D) {
            self.interval = publisher.interval
            self.tolerance = publisher.tolerance
            self.scheduler = publisher.scheduler
            self.options = publisher.options
            self.downstream = downstream
        }

        func receive(subscription: Subscription) {
            scheduler.schedule(after: scheduler.now.advanced(by: interval),
                               tolerance: tolerance,
                               options: options) { [weak self] in
                self?.downstream.receive(subscription: subscription)
            }
        }

        func receive(_ input: U.Output) -> Subscribers.Demand {
            return downstream.receive(input)
        }

        func receive(completion: Subscribers.Completion<U.Failure>) {
            downstream.receive(completion: completion)
        }
    }
}

// MARK: - filterWithLast
struct FilterWithLast<Upstream: Publisher>: Publisher {
    typealias Output = Upstream.Output
    typealias Failure = Upstream.Failure
    let upstream: Upstream
    let filter: (_ upStreamLast: Output?, _ downStreamLast: Output?, _ current: Output) -> Bool
    
    init(upstream: Upstream, filter: @escaping (_ upStreamLast: Output?, _ downStreamLast: Output?, _ current: Output) -> Bool) {
        self.upstream = upstream
        self.filter = filter
    }
    // When subscribed to, subscribe my Inner _upstream_
    func receive<S>(subscriber: S)
        where S : Subscriber, S.Input == Output, S.Failure == Failure {
            self.upstream.subscribe(FilterWithLastInner(downstream:subscriber, filter: self.filter))
    }
    // ... Inner goes here ...
    class FilterWithLastInner<S:Subscriber, Input>: Subscriber, Subscription
    where S.Failure == Failure, S.Input == Input { // !
        var downstream: S?
        var upstream: Subscription?
        let filter: (_ upStreamLast: Input?, _ downStreamLast: Input?, _ current: Input) -> Bool
        private var upStreamLast: Input? = nil
        private var downStreamLast: Input? = nil
        
        init(downstream: S, filter: @escaping (_ upStreamLast: Input?, _ downStreamLast: Input?, _ current: Input) -> Bool) {
            self.downstream = downstream
            self.filter = filter
        }
        // keep subscription, pass _self_ downstream
        func receive(subscription: Subscription) {
            self.upstream = subscription
            self.downstream?.receive(subscription: self)
        }
        // pass input downstream
        func receive(_ input: Input) -> Subscribers.Demand {
            let u = upStreamLast
            upStreamLast = input
            if (filter(u, downStreamLast, input)) {
                downStreamLast = input
                return self.downstream?.receive(input) ?? .max(0)
            }
            return Subscribers.Demand.none
        }
        // pass completion downstream
        func receive(completion: Subscribers.Completion<Failure>) {
            self.downstream?.receive(completion: completion)
            self.downstream = nil
            self.upstream = nil
        }
        // pass demand upstream
        func request(_ demand: Subscribers.Demand) {
            self.upstream?.request(demand)
        }
        // pass cancel upstream
        func cancel() {
            self.upstream?.cancel()
            self.upstream = nil
            self.downstream = nil
        }
    }
}
extension Publisher {
    func filterWithLast(filter: @escaping (_ upStreamLast: Output?, _ downStreamLast: Output?, _ current: Output) -> Bool
    ) -> FilterWithLast<Self> {
        return FilterWithLast(upstream:self, filter: filter)
    }
}

// MARK: - KeepHistoryOperator
extension Publisher where Output == CLLocation {
    
    func keepHistoryUntil(dropOldWhile: @escaping (_ current: CLLocation, _ item: CLLocation) -> Bool)  -> AnyPublisher<([CLLocation], [CLLocation]),Self.Failure> {
        scan(([CLLocation](),[CLLocation]())) { acc, cur -> ([CLLocation], [CLLocation]) in
            var l = acc.1
            l.append(cur)
            let f = l.first
            var d = [CLLocation]()
            var e: Self.Output? = nil
            
            if f != nil && dropOldWhile(cur, f!) {
                var drop = [CLLocation]()
                repeat {
                    drop.append(l.removeFirst())
                    e = l.first
                } while e != nil && dropOldWhile(cur, e!)
                d = drop
            } else {
                d = [CLLocation]()
            }
            let rs = (d, l)
            return rs
        }
        .eraseToAnyPublisher()
    }
}

/*public extension Cancellable {
    func onCancel(_ block: @escaping () -> Void) -> AnyCancellable {
        AnyCancellable { [self] in
            cancel()
            block()
        }
    }
}*/

// MARK: - WithLastElementOnCancel
struct WithLastElementOnCancel<Upstream: Publisher>: Publisher {
    typealias Output = Upstream.Output
    typealias Failure = Upstream.Failure
    let upstream: Upstream
    let onCancel: (_: Output?) -> Void
    
    init(upstream: Upstream, onCancel: @escaping (_: Output?) -> Void) {
        self.upstream = upstream
        self.onCancel = onCancel
    }
    // When subscribed to, subscribe my Inner _upstream_
    func receive<S>(subscriber: S)
        where S : Subscriber, S.Input == Output, S.Failure == Failure {
            self.upstream.subscribe(WithLastElementOnCancelInner(downstream:subscriber, onCancel: self.onCancel))
    }
    // ... Inner goes here ...
    class WithLastElementOnCancelInner<S:Subscriber, Input>: Subscriber, Subscription
    where S.Failure == Failure, S.Input == Input { // !
        var downstream: S?
        var upstream: Subscription?
        let onCancel: (_: Input?) -> Void
        private var upStreamLast: Input? = nil
        
        init(downstream: S, onCancel: @escaping (_: Input?) -> Void) {
            self.downstream = downstream
            self.onCancel = onCancel
        }
        // keep subscription, pass _self_ downstream
        func receive(subscription: Subscription) {
            self.upstream = subscription
            self.downstream?.receive(subscription: self)
        }
        // pass input downstream
        func receive(_ input: Input) -> Subscribers.Demand {
            upStreamLast = input
            return self.downstream?.receive(input) ?? .max(0)
        }
        // pass completion downstream
        func receive(completion: Subscribers.Completion<Failure>) {
            self.downstream?.receive(completion: completion)
            self.downstream = nil
            self.upstream = nil
        }
        // pass demand upstream
        func request(_ demand: Subscribers.Demand) {
            self.upstream?.request(demand)
        }
        // pass cancel upstream
        func cancel() {
            self.upstream?.cancel()
            onCancel(upStreamLast)
            self.upStreamLast = nil
            self.upstream = nil
            self.downstream = nil
        }
    }
}
extension Publisher {
    func withLastElementOnCancel(onCancel: @escaping (_: Output?) -> Void) -> WithLastElementOnCancel<Self> {
        return WithLastElementOnCancel(upstream:self, onCancel: onCancel)
    }
}

// MARK: - ShareReplay
public final class ReplaySubject<Output, Failure: Error>: Subject {
    private var buffer = [Output]()
    private let bufferSize: Int
    private var subscriptions = [ReplaySubjectSubscription<Output, Failure>]()
    private var completion: Subscribers.Completion<Failure>?
    private let lock = NSRecursiveLock()

    public init(_ bufferSize: Int = 0) {
        self.bufferSize = bufferSize
    }

    /// Provides this Subject an opportunity to establish demand for any new upstream subscriptions
    public func send(subscription: Subscription) {
        lock.lock(); defer { lock.unlock() }
        subscription.request(.unlimited)
    }

    /// Sends a value to the subscriber.
    public func send(_ value: Output) {
        lock.lock(); defer { lock.unlock() }
        buffer.append(value)
        buffer = buffer.suffix(bufferSize)
        subscriptions.forEach { $0.receive(value) }
    }

    /// Sends a completion signal to the subscriber.
    public func send(completion: Subscribers.Completion<Failure>) {
        lock.lock(); defer { lock.unlock() }
        self.completion = completion
        subscriptions.forEach { subscription in subscription.receive(completion: completion) }
    }

    /// This function is called to attach the specified `Subscriber` to the`Publisher
    public func receive<Downstream: Subscriber>(subscriber: Downstream) where Downstream.Failure == Failure, Downstream.Input == Output {
        lock.lock(); defer { lock.unlock() }
        let subscription = ReplaySubjectSubscription<Output, Failure>(downstream: AnySubscriber(subscriber))
        subscriber.receive(subscription: subscription)
        subscriptions.append(subscription)
        subscription.replay(buffer, completion: completion)
    }
}

/// A class representing the connection of a subscriber to a publisher.
public final class ReplaySubjectSubscription<Output, Failure: Error>: Subscription {
    private let downstream: AnySubscriber<Output, Failure>
    private var isCompleted = false
    private var demand: Subscribers.Demand = .none

    public init(downstream: AnySubscriber<Output, Failure>) {
        self.downstream = downstream
    }

    // Tells a publisher that it may send more values to the subscriber.
    public func request(_ newDemand: Subscribers.Demand) {
        demand += newDemand
    }

    public func cancel() {
        isCompleted = true
    }

    public func receive(_ value: Output) {
        guard !isCompleted, demand > 0 else { return }

        demand += downstream.receive(value)
        demand -= 1
    }

    public func receive(completion: Subscribers.Completion<Failure>) {
        guard !isCompleted else { return }
        isCompleted = true
        downstream.receive(completion: completion)
    }

    public func replay(_ values: [Output], completion: Subscribers.Completion<Failure>?) {
        guard !isCompleted else { return }
        values.forEach { value in receive(value) }
        if let completion = completion { receive(completion: completion) }
    }
}


extension Publisher {
    /// Provides a subject that shares a single subscription to the upstream publisher and replays at most `bufferSize` items emitted by that publisher
    /// - Parameter bufferSize: limits the number of items that can be replayed
    public func shareReplay(_ bufferSize: Int) -> AnyPublisher<Output, Failure> {
        return multicast(subject: ReplaySubject(bufferSize)).autoconnect().eraseToAnyPublisher()
    }
}
#endif
