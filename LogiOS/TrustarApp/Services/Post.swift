//
//  PostAPI.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/13.
//

import Foundation
import Combine

struct Post
{
    func postNotice(noticePost: NoticePost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postNotice, noticePost, Current.Shared.loggedUser!.token)
    }
    
    func postBin(binPost: BinPost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postBin, binPost.result(), Current.Shared.loggedUser!.token)
    }
    
    func postIncidental(incidentalPost: IncidentalPost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postIncidental, incidentalPost.result(), Current.Shared.loggedUser!.token)
    }
    
    func postCollection(collectionPost: CollectionPost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postCollection, collectionPost.result(), Current.Shared.loggedUser!.token)
    }
    
    func postFuel(fuelInfo: FuelInfo) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postFuel, fuelInfo, Current.Shared.loggedUser!.token)
    }
    
    func uploadImgdata(picPost: PicPost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.uploadImgData, picPost.result(), Current.Shared.loggedUser!.token)
    }
    
    func postGeo(geo: Geo) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.postGeo, geo, Current.Shared.loggedUser!.token)
    }
    
    func postRest(restPost: RestPost) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callAPI(.rest, restPost, Current.Shared.loggedUser!.token)
    }
    
//    func postDeliveryChart(deliveryChartPost: DeliveryChartPost) -> AnyPublisher<StateResult, NetworkError> {
//        return API.shared.callAPI(.postDeliveryChart, deliveryChartPost.result(), Current.Shared.loggedUser!.token)
//    }
    
    func postDeliveryChart(data: DeliveryChartPost) -> AnyPublisher<Data, NetworkError> {
        return API.shared.callPostDeliveryChartAPI(
            .postDeliveryChart,
            [.ApiKeyQuery],
            data.result(),
            Current.Shared.loggedUser!.token
        )
    }
}
