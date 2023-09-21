//
//  DeliveryChartView.swift
//  TrustarApp
//
//  Created by hoanx on 8/3/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import SwiftUI
import PhotosUI
import Combine

struct IOSVersionTextEditor: View {
    @Binding var string: String
    @State var maxLength: Int? = nil
    var returnKeyType: UIReturnKeyType = .next
    var keyboardType: UIKeyboardType = .default
    var label: String = ""
    var tag: Int = 0
    var body: some View {
        TSUITextField(label: label, text: $string,
                      returnKeyType: returnKeyType,
                      keyboardType: keyboardType,
                      tag:tag,
                      onCommit: {_ in },
                      maxLength: maxLength,
                      textAlignment: .left)
        .foregroundColor(.black)
        .frame(height: 20)
        .padding(8)
        .overlay(
            RoundedRectangle(cornerRadius: 2)
                .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
        )
    }
}

struct BinDeliveryChartView: View {
    @Binding var isPresented: Bool
    @State private var editMode: Bool
    @ObservedObject var vm: DeliveryChartViewModel
    var onDissmiss: (Bool) -> Void
    
    init(isPresented: Binding<Bool>, vm: DeliveryChartViewModel,
         onDissmiss: @escaping (Bool) -> Void){
        self._isPresented = isPresented
        self.vm = vm
        self.onDissmiss = onDissmiss
        
        self.editMode = vm.isNewCreate
    }
    var newCreate: Bool { return vm.isNewCreate }
    var body: some View {
        ZStack{
            if editMode {
                EditDeliveryChartView(isPresented: $isPresented,
                                      vm: vm,
                                      onBack: {
                    if newCreate {
                        onDissmiss(false)
                    } else {
                        editMode = !editMode
                    }
                },
                                      onDone: {
                    editMode = !editMode
                })
            } else {
                DeliveryChartView(isPresented: $isPresented,
                                  vm: vm,
                                  onEdit: { editMode = !editMode
                },
                                  onBack: {onDissmiss(false)})
            }
            
        }.frame(maxWidth: .infinity)
            .background(Color.white)
    }
}

struct EditDeliveryChartView: View {
    @Binding var isPresented: Bool
    @ObservedObject var vm: DeliveryChartViewModel
    var onBack: () -> Void
    var onDone: () -> Void
    
    var newCreate: Bool { return vm.isNewCreate }
    
    @State private var eHighlight: Bool = false
    @State private var isShowPhotoLibrary: Bool = false
    @State private var isShowPhotoLauncher: Bool = false
    @State private var picSourceType: UIImagePickerController.SourceType = .photoLibrary
    
    @State private var images: [UIImage] = []
    
    init(isPresented: Binding<Bool>, vm: DeliveryChartViewModel,
         onBack: @escaping () -> Void, onDone: @escaping () -> Void) {
        self._isPresented = isPresented
        self.vm = vm
        self.onBack = onBack
        self.onDone = onDone
    }
    var body: some View {
        ZStack {
            VStack {
                
                VStack {
                    // header
                    HStack {
                        Button(action: {
                            onBack()
                        }, label: {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                                .font(.system(size: 18, weight: .bold))
                        })
                        
                        Text(newCreate ? Resources.new_create : Resources.delivery_edit)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundColor(.white)
                            .font(.system(size: 20, weight: .bold))
                            .padding()
                        
                        Button(action: {
                            saveDeliveryChart()
                        }, label: {
                            Image(systemName: "checkmark")
                                .foregroundColor(.white)
                                .font(.system(size: 18, weight: .bold))
                        })
                    }.padding(8)
                }.background(Color(Resources.colorPrimary))
                
                ScrollView {
                    VStack {
                        //dest info
                        HStack {
                            Text(Resources.delivery_dest_info)
                                .foregroundColor(.black)
                                .font(.system(size: 18, weight: .bold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding([.trailing], 8)
                        .padding([.top, .bottom], 20)
                        
                        // info
                        VStack{
                            EditItemView(title: {
                                HStack(){
                                    Text("*").foregroundColor(.red)
                                    +
                                    Text(Resources.delivery_dest)
                                        .foregroundColor(.black)
                                    
                                }
                                
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.dest, maxLength: 400, tag: 0)
                            })
                            
                            EditItemView(title: {
                                Text(Resources.delivery_dest_addr1)
                                    .foregroundColor(.black)
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.addr1, maxLength: 100, tag: 1)
                            })
                            
                            EditItemView(title: {
                                Text(Resources.delivery_dest_addr2)
                                    .foregroundColor(.black)
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.addr2, maxLength: 300, tag: 2)
                            })
                            
                            EditItemView(title: {
                                Text(Resources.delivery_dest_tel)
                                    .foregroundColor(.black)
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.tel, maxLength: 50,
                                                     keyboardType: .phonePad, tag: 3)
                            })
                            
                            EditItemView(title: {
                                Text(Resources.delivery_carrier)
                                    .foregroundColor(.black)
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.carrier, maxLength: 100, tag: 4)
                            })
                            
                            EditItemView(title: {
                                Text(Resources.delivery_carrier_tel)
                                    .foregroundColor(.black)
                            }, content: {
                                IOSVersionTextEditor(string: $vm.deliveryChart.carrierTel, maxLength: 50,
                                                     returnKeyType: .done, keyboardType: .phonePad, tag: 5)
                            })
                        }
                        .padding(8)
                        
                        //memo
                        VStack{
                            HStack{
                                Text(Resources.memo)
                                    .foregroundColor(.black)
                                    .font(.system(size: 18, weight: .bold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                Button(action: {
                                    eHighlight = !eHighlight
                                }, label: {
                                    Text(Resources.delivery_emphasis)
                                        .foregroundColor(.white)
                                        .fontWeight(.bold)
                                        .padding(8)
                                })
                                .background(Color(Resources.colorPrimary))
                                .cornerRadius(50)
                                .padding(20)
                                
                                Button(action: {
                                    DispatchQueue.main.async {
                                        vm.Tmemos.append(
                                            ChartMemos()
                                        )
                                    }
                                }, label: {
                                    Image(systemName: "plus")
                                        .foregroundColor(.black)
                                        .font(.system(size: 18, weight: .bold))
                                })
                            }
                            .padding(.trailing, 8)
                            
                            VStack(spacing: 8) {
                                ForEach($vm.Tmemos, id: \.self.id) { $memo in
                                    let idx = vm.Tmemos.firstIndex(where: {it in
                                        it.id == memo.id
                                    })
                                    EditMemoView(memo: $memo, idx: idx ?? 0,
                                                 highlight: eHighlight, onRemove: { $key in
                                        if let index = vm.Tmemos.firstIndex(where: {it in
                                            it.id == key.id
                                        }) {
                                            vm.Tmemos.remove(at: index)
                                        }
                                    })
                                }.onDelete(perform: { offsets in
                                    vm.Tmemos.remove(atOffsets: offsets)
                                })
                            }
                            .padding(8)
                        }
                     
                        EditImagePartView(
                            vm: vm,
                            request: fetchImage,
                            isShowPhotoLibrary: $isShowPhotoLibrary,
                            isShowPhotoLauncher: $isShowPhotoLauncher,
                            images: $images,
                            picSourceType: $picSourceType
                        )
                    }.padding(8)
                }
            }
            
            PopUpBottomView(popupVm: vm.popupVm, show: $vm.isShowingAlert)
            
            // Image Picker
            if isShowPhotoLibrary {
                if #available(iOS 14, *) {
                    PhotosPicker(selectedImages: $images, isPresented: $isShowPhotoLibrary,
                                 onDone: { ok in
                        if ok {
                            addTmpFile(vm.readImageToTmpJPEG(uri: images))
                        }
                    })
                } else {
                    ImagePicker(sourceType: $picSourceType,
                                selectedImages: $images,
                                isPresented: $isShowPhotoLibrary,
                                onDone: { ok in
                        if ok {
                            addTmpFile(vm.readImageToTmpJPEG(uri: images))
                        }
                    })
                }
            }
            
            //take camera
            if isShowPhotoLauncher {
                ImagePicker(sourceType: $picSourceType,
                            selectedImages: $images,
                            isPresented: $isShowPhotoLauncher,
                            onDone: { ok in
                    if ok {
                        addTmpFile(vm.readImageToTmpJPEG(uri: images))
                    }
                })
            }
        }
        
    }
    
    private func addTmpFile (_ fs: [FileRef.ByAnyFile]) -> Void
    {
        fs.forEach {f in
            vm.Timages.append(EditImage.Add(fileRef: f, saveTo: vm.userStoredChartDir!))
        }
        
    }
    
    private func fetchImage(file: FileRef) -> AnyPublisher<URL?, Never> {
        if let pic = file as? FileRef.ByAnyFile {
            return Just(pic.file as URL).eraseToAnyPublisher()
        } else if let pic = file as? FileRef.ByKey {
            return vm.download(fileRef: pic)
        } else if let pic = file as? FileRef.ByStored {
            return Just(pic.stored.storedFile).eraseToAnyPublisher()
        }
        return Just(nil).eraseToAnyPublisher()
    }
    
    private func saveDeliveryChart(){
        DispatchQueue.main.async {
            let images = vm.Timages
            let memos = vm.Tmemos.filter{ it in
                !it.label.isEmpty || !it.note.isEmpty
            }
            let saved = vm.save(
                memos: memos, images: images)
            if saved {
                onDone()
            }
        }
    }
}

struct DeliveryChartView: View {
    @Binding var isPresented: Bool
    @ObservedObject var vm: DeliveryChartViewModel
    var onEdit: () -> Void
    var onBack: () -> Void
    
    private var eMemos: [Int: ChartMemos] = [:]
    
    private var pics: [FileRef] = []
    
    init(isPresented: Binding<Bool>, vm: DeliveryChartViewModel,
         onEdit: @escaping () -> Void, onBack: @escaping () -> Void) {
        self._isPresented = isPresented
        self.vm = vm
        self.onEdit = onEdit
        self.onBack = onBack
        
        for i in 0..<vm.Tmemos.count {
            eMemos[i] = vm.Tmemos[i]
        }
        
        pics = vm.Timages.map {imageFile in
            imageFile.fileRef
        }
    }
    
    var body: some View {
        ZStack {
            VStack {
                VStack {
                    // header
                    HStack {
                        Button(action: {
                            onBack()
                        }, label: {
                            Image(systemName: "chevron.left")
                                .foregroundColor(.white)
                                .font(.system(size: 18, weight: .bold))
                        })
                        
                        Text(Resources.delivery_chart)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundColor(.white)
                            .font(.system(size: 20, weight: .bold))
                            .padding()
                        
                        Button(action: {
                            onEdit()
                        }, label: {
                            Image(systemName: "pencil")
                                .foregroundColor(.white)
                                .font(.system(size: 18, weight: .bold))
                        })
                        .background(Color(Resources.colorPrimary))
                        .cornerRadius(50)
                    }.padding(8)
                }.background(Color(Resources.colorPrimary))
                
                ScrollView{
                    VStack {
                        //dest info
                        HStack {
                            Text(Resources.delivery_dest_info)
                                .foregroundColor(.black)
                                .font(.system(size: 18, weight: .bold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding([.trailing], 8)
                        .padding([.top, .bottom], 20)
                        
                        // info
                        VStack{
                            ItemView(title: {
                                Text(Resources.delivery_dest)
                                    .foregroundColor(.black)
                                    .fontWeight(.bold)
                            }, content: {
                                Text(vm.deliveryChart.dest)
                            })
                            
                            ItemView(title: {
                                Text(Resources.delivery_dest_addr)
                                    .foregroundColor(.black)
                                    .fontWeight(.bold)
                            }, content: {
                                Button(action: {
                                    if vm.deliveryChart.address().isEmpty {
                                        return
                                    }
                                    // open map
                                    if let url = URL(string: "maps://?q=\(vm.deliveryChart.address().addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)") {
                                        UIApplication.shared.open(url)
                                    }
                                }, label: {
                                    Text(vm.deliveryChart.address())
                                })
                            })
                            
                            ItemView(title: {
                                Text(Resources.delivery_dest_tel)
                                    .foregroundColor(.black)
                                    .fontWeight(.bold)
                            }, content: {
                                Button(action: {
                                    if vm.deliveryChart.tel.isEmpty {
                                        return
                                    }
                                    //dial
                                    if let url = URL(string: "tel://\(vm.deliveryChart.tel)") {
                                        UIApplication.shared.open(url)
                                    }
                                }, label: {
                                    Text(vm.deliveryChart.tel)
                                })
                            })
                            
                            ItemView(title: {
                                Text(Resources.delivery_carrier)
                                    .foregroundColor(.black)
                                    .fontWeight(.bold)
                            }, content: {
                                Text(vm.deliveryChart.carrier)
                            })
                            
                            ItemView(title: {
                                Text(Resources.delivery_carrier_tel)
                                    .foregroundColor(.black)
                                    .fontWeight(.bold)
                            }, content: {
                                Button(action: {
                                    if vm.deliveryChart.carrierTel.isEmpty {
                                        return
                                    }
                                    //dial
                                    if let url = URL(string: "tel://\(vm.deliveryChart.carrierTel)") {
                                        UIApplication.shared.open(url)
                                    }
                                }, label: {
                                    Text(vm.deliveryChart.carrierTel)
                                })
                            })
                        }
                        .padding(8)
                        
                        //memo
                        VStack{
                            Text(Resources.memo)
                                .foregroundColor(.black)
                                .font(.system(size: 18, weight: .bold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding([.top, .bottom], 20)
                            VStack{
                                ForEach(eMemos.keys.sorted(), id: \.self) { key in
                                    MemoView(memo: eMemos[key]!)
                                }
                            }.padding(8)
                        }
                        
                        ImagePartView(
                            vm: vm,
                            pageCount: pics.count,
                            page: 0,
                            request: fetchImage
                        )
                    }.padding(8)
                }
            }
        }
    }
    
    private func fetchImage(file: FileRef) -> AnyPublisher<URL?, Never> {
        if let pic = file as? FileRef.ByAnyFile {
            return Just(pic.file as URL).eraseToAnyPublisher()
        } else if let pic = file as? FileRef.ByKey {
            return vm.download(fileRef: pic).eraseToAnyPublisher()
        } else if let pic = file as? FileRef.ByStored {
            return Just(pic.stored.storedFile).eraseToAnyPublisher()
        }
        return Just(nil).eraseToAnyPublisher()
    }
}

struct EditItemView<Title: View, Content: View>: View {
    @ViewBuilder var title: Title
    @ViewBuilder var content: Content
    
    var body: some View {
        HStack (spacing: 0) {
            // title:
            title
                .frame(maxWidth: 110 ,alignment: .leading)
                .font(.system(size: 15, weight: .bold))
            
            // content
            content
                .frame(maxWidth: .infinity, alignment: .leading)
                .font(.system(size: 18))
        }
        
        Divider().background(Color.black)
    }
}

struct ImageView: View {
    @Binding var isShowImageView: Bool
    @Binding var page: Int
    var key: (_ page: Int) -> FileRef
    var request: (_ key: FileRef) -> AnyPublisher<URL?, Never>
    @State var imageDataa:UIImage?
    @State var state:Int = 0
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .topLeading) {
                if isShowImageView {
                    // PopUp background color
                    Color.black.edgesIgnoringSafeArea(.all)
                    @State var imageData:UIImage = UIImage(systemName: "x.square.fill")!
                    VStack {
                        switch state {
                        case 0:
                            Spin(color: .blue)
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                        case 1:
                            if imageDataa != nil {
                                Image(uiImage: imageDataa!)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .scaledToFit()
                                    .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                
                            } else {
                                Image(systemName: "xmark")
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .scaledToFit()
                                    .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                            }
                            
                        default:
                            Image(systemName: "xmark")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .scaledToFit()
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                        }
                    }.onAppear {
                        self.state = 0
                        request(key(page)).sink { url in
                            if url == nil {
                                self.state = 2
                            } else {
                                self.imageDataa = UIImage(contentsOfFile: url!.path)!
                                self.state = 1
                            }
                        }.store(in: &key(page).bag)
                    }
                    Button(action: {
                        isShowImageView = false
                    }, label: {
                        Image(systemName: "xmark")
                            .foregroundColor(.white)
                            .font(.system(size: 18, weight: .bold))
                            .frame(alignment: .topLeading)
                            .padding()
                    })
                }
            }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
            
        }
    }
}

struct IOSCheckBoxToggleStyle: ToggleStyle {
    var toggle: (Bool) -> Void
    
    func makeBody(configuration: Configuration) -> some View {
        Button(action: {
            configuration.isOn.toggle()
            toggle(configuration.isOn)
        }, label: {
            Image(systemName:  configuration.isOn ? "checkmark.square.fill" : "square")
                .foregroundColor(configuration.isOn ? Color(Resources.colorPrimary) : .gray)
                .font(.system(size: 22))
        })
    }
}

struct EditMemoView: View {
    @Binding var memo: ChartMemos
    var idx: Int
    var highlight: Bool
    var onRemove: (Binding<ChartMemos>) -> Void
    
    var body: some View {
        HStack(spacing: 8) {
            if highlight {
                Toggle("", isOn: $memo.higtlight)
                    .toggleStyle(IOSCheckBoxToggleStyle(toggle: { check in
                        
                    }))
                //.padding(8)
                
            }
            
            TSUITextField(label: "", text: $memo.label,
                          returnKeyType: .next,
                          keyboardType: .default,
                          tag:20 + idx * 3,
                          onCommit: {_ in },
                          maxLength: nil,
                          textAlignment: .left,
                          foregroundColor: memo.higtlight ? .red : .black)
            //.foregroundColor(memo.higtlight ? .red : .black)
            //.font(.system(size: 40))
            .frame(height: 20)
            .padding(8)
            .overlay(
                RoundedRectangle(cornerRadius: 2)
                    .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
            )
            .frame(maxWidth: 130, alignment: .leading)
            
            TSUITextField(label: "", text: $memo.note,
                          returnKeyType: .done,
                          keyboardType: .default,
                          tag:21 + idx * 3,
                          onCommit: {_ in },
                          maxLength: nil,
                          textAlignment: .left,
                          foregroundColor: memo.higtlight ? .red : .black)
            //.foregroundColor(memo.higtlight ? .red : .black)
            //.font(.system(size: 18))
            .frame(height: 20)
            .padding(8)
            .overlay(
                RoundedRectangle(cornerRadius: 2)
                    .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
            )
            .frame(maxWidth: .infinity, alignment: .leading)
            
            Button(action: {
                onRemove($memo)
            }, label: {
                Image(systemName: "trash.fill")
                    .foregroundColor(.black)
                    .font(.system(size: 18, weight: .bold))
            })
        }
        
        Divider().background(Color.black)
    }
}


struct MemoView: View {
    var memo: ChartMemos
    
    var body: some View {
        ItemView(title: {
            Text(memo.label)
                .foregroundColor(
                    memo.higtlight ? .red : .black
                )
                .fontWeight(.bold)
        }, content: {
            Text(memo.note)
                .foregroundColor(
                    memo.higtlight ? .red : .black
                )
        })
    }
}

struct EditImagePartView: View {
    var vm: DeliveryChartViewModel
    private var pageCount: Int {
        vm.Timages.count
    }
    @State private var page: Int = 0
    var request: (_ key: FileRef) -> AnyPublisher<URL?, Never>
    
    @Binding var isShowPhotoLibrary: Bool
    @Binding var isShowPhotoLauncher: Bool
    @Binding var images: [UIImage]
    @Binding var picSourceType: UIImagePickerController.SourceType
    @State private var isShowImageView: Bool = false
    
    var body: some View {
        ZStack {
            VStack {
                HStack {
                    Text(Resources.delivery_photo)
                        .foregroundColor(.black)
                        .font(.system(size: 18, weight: .bold))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding([.top, .bottom], 20)
                    
                    Button(action: {
                        Task {
                            await addPicLaucher()
                        }
                    }, label: {
                        Image(systemName: "photo.fill")
                            .foregroundColor(.black)
                            .font(.system(size: 18, weight: .bold))
                        
                    }).padding(.horizontal, 8)
                    
                    Button(action: {
                        Task {
                            await takePhotoLauncher()
                        }
                    }, label: {
                        Image(systemName: "camera.fill")
                            .foregroundColor(.black)
                            .font(.system(size: 18, weight: .bold))
                    }).padding(.leading, 8)
                }
                .padding(8)
                
                ImagePartCommon(
                    vm: vm,
                    pageCount: pageCount,
                    page: $page,
                    key: key,
                    onDelete: onDeleteConfirm,
                    request: request,
                    clickImage: clickImage
                )
            }
        }
        .sheet(isPresented: $isShowImageView, content: {
            ImageView(isShowImageView: $isShowImageView, page: $page,
                      key: key, request: request)
        })
    }
    
    private func clickImage() {
        isShowImageView = true
    }
    
    private func onDeleteConfirm(_ index: Int) -> Void {
        vm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.delivery_photo_del_msg,
                                                 isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.delete,
                                                 rightBtnClick: {
            removeImage(index)
        }))
    }
    
    private func removeImage(_ index: Int) -> Void {
        vm.Timages.remove(at: index)
        let pageCount = vm.Timages.count
        if pageCount == 0 {
            page = 0
        } else if page > pageCount - 1 {
            page = pageCount - 1
        }
    }
    
    private func key(_ page: Int) -> FileRef {
        vm.Timages[page].fileRef
    }
    
    private func addPicLaucher() async {
        withAnimation {
            PhotoLibrary.checkAuthorization({ isAuthorized in
                if isAuthorized {
                    DispatchQueue.main.async {
                        isShowPhotoLibrary.toggle()
                        picSourceType = .photoLibrary
                    }
                } else {
                    vm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_permission_settings_image, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                        DispatchQueue.main.async {
                            UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
                        }
                    }))
                }
            })
        }
    }
    
    private func takePhotoLauncher()  async {
        let isAuthorized = await PhotoLibrary.checkAuthorizationCamera()
        
        if isAuthorized {
            DispatchQueue.main.async {
                isShowPhotoLauncher.toggle()
                picSourceType = .camera
            }
        } else {
            vm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_permission_settings_camera, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                DispatchQueue.main.async {
                    UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
                }
            }))
        }
    }
}

struct ImagePartView: View {
    var vm: DeliveryChartViewModel
    @State var pageCount: Int
    @State var page: Int
    var request: (_ key: FileRef) -> AnyPublisher<URL?, Never>
    
    @State private var isShowImageView: Bool = false
    
    var body: some View {
        VStack{
            HStack {
                Text(Resources.delivery_photo)
                    .foregroundColor(.black)
                    .font(.system(size: 18, weight: .bold))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding([.top, .bottom], 20)
            }
            
            ImagePartCommon(
                vm: vm,
                pageCount: pageCount,
                page: $page,
                key: key,
                onDelete: nil,
                request: request,
                clickImage: clickImage
            )
        }.sheet(isPresented: $isShowImageView, content: {
            ImageView(isShowImageView: $isShowImageView, page: $page,
                      key: key, request: request)
        })
    }
    
    private func key(_ page: Int) -> FileRef {
        vm.Timages[page].fileRef
    }
    
    private func clickImage() {
        isShowImageView = true
    }
}

struct ImagePicker: UIViewControllerRepresentable {
    @Binding var sourceType: UIImagePickerController.SourceType
    @Binding var selectedImages: [UIImage]
    @Binding var isPresented: Bool
    var onDone: (Bool) -> Void
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<ImagePicker>) -> UIImagePickerController {
        let imagePicker = UIImagePickerController()
        imagePicker.allowsEditing = false
        imagePicker.sourceType = sourceType
        imagePicker.delegate = context.coordinator
        
        return imagePicker
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: UIViewControllerRepresentableContext<ImagePicker>) {
        
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    final class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
        var parent: ImagePicker
        init(_ parent: ImagePicker) {
            self.parent = parent
        }
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            var processedImages: [UIImage] = []
            if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
                processedImages.append(image)
            }
            parent.selectedImages = processedImages
            parent.isPresented = false
            parent.onDone(true)
        }
        
        func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
            parent.isPresented = false
            parent.onDone(false)
        }
        
    }
    
}

@available(iOS 14.0, *)
struct PhotosPicker: UIViewControllerRepresentable {
    @Binding var selectedImages: [UIImage]
    @Binding var isPresented: Bool
    var onDone: (Bool) -> Void
    
    func makeUIViewController(context: Context) -> UIViewController {
        var configuration = PHPickerConfiguration(photoLibrary: PHPhotoLibrary.shared())
        configuration.filter = .images
        if #available(iOS 15, *) {
            configuration.selection = .ordered
        }
        configuration.selectionLimit = 25
        
        
        let photoPicker = PHPickerViewController(configuration: configuration)
        photoPicker.delegate = context.coordinator
        
        return photoPicker
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    final class Coordinator: PHPickerViewControllerDelegate {
        
        var parent: PhotosPicker
        init(_ parent: PhotosPicker) {
            self.parent = parent
        }
        
        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            var processedImages: [UIImage] = []
            var leftToLoad = results.count
            let checkFinished: (Bool) -> Void = { [weak self] ok in
                leftToLoad -= 1
                if leftToLoad == 0 {
                    DispatchQueue.main.async {
                        self?.parent.selectedImages = processedImages
                        self?.parent.isPresented = false
                        self?.parent.onDone(ok)
                    }
                }
            }
            if results.count == 0 {
                self.parent.selectedImages = processedImages
                self.parent.isPresented = false
                self.parent.onDone(false)
            }
            for image in results {
                if image.itemProvider.canLoadObject(ofClass: UIImage.self) {
                    image.itemProvider.loadObject(ofClass: UIImage.self){newImage, error in
                        if let error = error {
                            debugPrint(error)
                        } else if let image = newImage as? UIImage {
                            processedImages.append(image)
                        }
                        checkFinished(true)
                    }
                }else {
                    debugPrint("Can not load Image")
                    checkFinished(false)
                }
            }
        }
        
    }
    
}

struct ImageInner: View {
    @Binding var image: EditImage
    var index: Int
    @Binding var page: Int
    var key: (_ page: Int) -> FileRef
    var request: (_ key: FileRef) -> AnyPublisher<URL?, Never>
    var clickImage: () -> Void
    @State private var imageDataa:UIImage?
    @State private var state:Int = 0
    
    var body: some View {
        
        GeometryReader { geometry in
            VStack {
                switch state {
                case 0:
                    Spin(color: .blue)
                        .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                        .padding(.leading, geometry.size.width*CGFloat(page))
                case 1:
                    if page == index {
                        if imageDataa != nil {
                            Image(uiImage: imageDataa!)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .onTapGesture {
                                    clickImage()
                                }
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .padding(.leading, geometry.size.width*CGFloat(page))
                        } else {
                            Image(systemName: "xmark")
                                .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                                .padding(.leading, geometry.size.width*CGFloat(page))
                        }
                    }
                default:
                    Image(systemName: "xmark")
                        .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                        .padding(.leading, geometry.size.width*CGFloat(page))
                }
            }.onAppear {
                self.state = 0
                request(image.fileRef).sink { url in
                    if url == nil {
                        self.state = 2
                    } else {
                        self.imageDataa = UIImage(contentsOfFile: url!.path)
                        self.state = 1
                    }
                }.store(in: &image.fileRef.bag)
            }
        }
    }
}
struct ImagePartCommon: View {
    @ObservedObject var vm: DeliveryChartViewModel
    var pageCount: Int
    @Binding var page: Int
    var onDelete: ((_ index: Int) -> Void)? = nil
    var key: (_ page: Int) -> FileRef
    var request: (_ key: FileRef) -> AnyPublisher<URL?, Never>
    var clickImage: () -> Void
    
    private var image: AnyPublisher<URL?, Never> {
        return self.pageCount == 0 ? Just(nil).eraseToAnyPublisher() : request(key(self.page))
    }
    init (
        vm: DeliveryChartViewModel,
        pageCount: Int,
        page: Binding<Int>,
        key: @escaping (_ page: Int) -> FileRef,
        onDelete: ((_ index: Int) -> Void)?,
        request: @escaping (_ key: FileRef) -> AnyPublisher<URL?, Never>,
        clickImage: @escaping () -> Void
    ) {
        self.vm = vm
        self.pageCount = pageCount
        self._page = page
        self.key = key
        self.onDelete = onDelete
        self.request = request
        self.clickImage = clickImage
    }
    
    var body: some View {
        PagerView(pageCount: pageCount, currentIndex: _page,
                  onDelete: onDelete
        ) {
            ZStack(alignment: .leading) {
                GeometryReader { geometry in
                    ForEach($vm.Timages, id: \.self.id) { $image in
                        let index = vm.Timages.firstIndex(where: {it in
                            it.id == image.id
                        })!
                        ImageInner(image: $image, index: index, page: $page,
                                   key: key, request: request, clickImage: clickImage)
                        .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                    }
                }
            }
        }
    }
}


struct PagerView<Content: View>: View {
    var pageCount: Int
    @Binding var currentIndex: Int
    var onDelete: ((_ index: Int) -> Void)? = nil
    @ViewBuilder var content: Content
    
    @GestureState private var translation: CGFloat = 0
    
    var body: some View {
        if pageCount == 0 {
            HStack{
                Text(Resources.empty_placeholder)
            }
        } else {
            GeometryReader { geometry in
                ZStack(alignment: .bottomTrailing) {
                    Rectangle()
                        .fill(.gray)
                        .frame(width: geometry.size.width, height: geometry.size.height - 20)
                        .offset(y: -20)
                    
                    HStack(spacing: 0){
                        self.content.frame(width: geometry.size.width, height: geometry.size.height - 20)
                    }
                    .frame(width: geometry.size.width, height: geometry.size.height - 20)
                    .offset(y: -20)
                    .offset(x: -CGFloat(self.currentIndex) * geometry.size.width)
                    .offset(x: self.translation)
                    
                    .animation(.interactiveSpring())
                    .gesture(
                        DragGesture().updating(self.$translation) { value, state, _ in
                            state = value.translation.width
                        }.onEnded{ value in
                            let offset = value.translation.width / geometry.size.width
                            let newIndex = (CGFloat(self.currentIndex) - offset).rounded()
                            self.currentIndex = min(max(Int(newIndex), 0), self.pageCount - 1)
                        }
                    )
                    
                    
                    HStack() {
                        Button(action: {
                            if self.currentIndex > 0 {
                                self.currentIndex -= 1
                            }
                        }, label: {
                            VStack {
                                Image(systemName: "chevron.left")
                                    .foregroundColor(.white.opacity(self.currentIndex == 0 ? 0.5 : 1))
                                    .font(.system(size: 30, weight: .bold))
                                    .padding(8)
                            }.frame(maxWidth: .infinity, maxHeight: .infinity)
                            
                        })
                        .disabled(self.currentIndex <= 0)
                        .background(Color.gray.opacity(0.5))
                        
                    }
                    .frame(width: 50, height: geometry.size.height - 20)
                    .offset(x: -geometry.size.width + 50, y: -20)
                    
                    HStack() {
                        Button(action: {
                            if (self.currentIndex < pageCount - 1) {
                                self.currentIndex += 1
                            }
                        }, label: {
                            VStack {
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.white.opacity(self.currentIndex == pageCount - 1 ? 0.5 : 1))
                                    .font(.system(size: 30, weight: .bold))
                                    .padding(8)
                            }
                            .frame(maxWidth: .infinity, maxHeight: .infinity)
                        })
                        .disabled(self.currentIndex >= pageCount - 1)
                        .background(Color.gray.opacity(0.5))
                        
                    }
                    .frame(width: 50, height: geometry.size.height - 20)
                    .offset(y: -20)
                    
                    if onDelete != nil {
                        HStack {
                            Button(action: {
                                onDelete?(currentIndex)
                            }, label: {
                                ZStack {
                                    Image(systemName: "trash.fill")
                                        .foregroundColor(.white)
                                        .font(.system(size: 18, weight: .bold))
                                }
                            })
                            .frame(width: 40, height: 40)
                            .background(Color.red)
                            .clipShape(Circle())
                        }.frame(width: geometry.size.width, height: 50)
                            .offset(y: -20)
                    }
                    
                    HStack {
                        Text("\(currentIndex + 1)／\(pageCount)")
                    }.frame(width: geometry.size.width, height: 18)
                    
                }
                
            }
            .aspectRatio(CGSize(width: 4, height: 3), contentMode: .fill)
            .frame(minHeight: 200)
        }
    }
}
