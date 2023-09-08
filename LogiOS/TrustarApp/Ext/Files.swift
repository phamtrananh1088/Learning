//
//  Files.swift
//  TrustarApp
//
//  Created by hoanx on 8/3/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import UIKit

class FilesInDir {
    let dir: URL
    
    init(dir: URL) {
        self.dir = dir
    }
    
    func fileByName(filename: String) -> URL {
        return dir.appendingPathComponent(filename)
    }
    
    func deleteAll() throws {
        try FileManager.default.removeItem(at: dir)
        try FileManager.default.createDirectory(at: dir, withIntermediateDirectories: true)
    }
}

// local stored file or key of remote file.
class StoredFile {
    func fileRef() -> FileRef {
        return FileRef()
    }
    
    func asDbStoredFile () -> DbFileRecord {
        preconditionFailure("must override asDbStoredFile")
    }
    
    class ByKey: StoredFile {
        
        let key: String
        
        init(key: String) {
            self.key = key
        }
        
        override func fileRef() -> FileRef.ByKey {
            return FileRef.ByKey(key: key)
        }
        
        override func asDbStoredFile () -> DbFileRecord {
            return DbFileRecord(type: typeKey, value: key)
        }
    }
    
    class SyncDirFile: StoredFile {
        let filesInDir: FilesInDir
        let filename: String
        
        init(filesInDir: FilesInDir, filename: String) {
            self.filesInDir = filesInDir
            self.filename = filename
        }
        
        var storedFile: URL {
            return filesInDir.fileByName(filename: filename)
        }
        
        var fileExtension: String? {
            return storedFile.pathExtension
        }
        
        func isSameFile(file: SyncDirFile) -> Bool {
            return storedFile.standardizedFileURL == file.storedFile.standardizedFileURL
        }
        
        func delete() {
            _ = storedFile.delete()
        }
        
        override func fileRef() -> FileRef.ByStored {
            return FileRef.ByStored(stored: self)
        }
        
        override func asDbStoredFile () -> DbFileRecord {
            return DbFileRecord(type: typeFileName, value: filename)
        }
    }
    
}

// For local or remote file access.
class FileRef {
    var bag = Set<AnyCancellable>()
    class ByKey : FileRef {
        let key: String
        
        init(key: String) {
            self.key = key
        }
    }
    
    class ByAnyFile : FileRef {
        let file: File
        let dotExt: String
        
        init(file: File, dotExt: String) {
            self.file = file
            self.dotExt = dotExt
        }
    }
    
    class ByStored : FileRef {
        let stored: StoredFile.SyncDirFile
        
        init(stored: StoredFile.SyncDirFile) {
            self.stored = stored
        }
    }
    
    func storeFileByMove(dir: FilesInDir) -> StoredFile {
        var val: StoredFile
        if let byAnyFile = self as? FileRef.ByAnyFile {
            val =  createSyncDirFile(dir: dir, source: byAnyFile.file, _extension: byAnyFile.dotExt)
        }else if let byKey = self as?  FileRef.ByKey {
            val = StoredFile.ByKey(key: byKey.key)
        } else {
            val = (self as? FileRef.ByStored)!.stored
        }
        return val
    }
}

private func createSyncDirFile(dir: FilesInDir,
                               source: File,
                               _extension: String?,
                               retry: Int = 3) -> StoredFile.SyncDirFile {
    let s = StoredFile.SyncDirFile(filesInDir: dir,
                                   filename: UUID().uuidString + _extension.orEmpty())
    if (source.moveOrCopy(to: s.storedFile))
    {
        return s
    }
    return s
}


func copySyncDirFile (dir: FilesInDir, source: URL, dotExt: String, retry: Int = 3) -> StoredFile.SyncDirFile{
    var s = StoredFile.SyncDirFile(filesInDir: dir, filename: UUID().uuidString + "." + dotExt)
    if !FileManager.default.fileExists(atPath: s.storedFile.path) {
        _ = secureCopyItem(at: source, to: s.storedFile)
        return s
    }
    return s
}

func secureCopyItem(at srcURL: URL, to dstURL: URL) -> Bool {
        do {
            if FileManager.default.fileExists(atPath: dstURL.path) {
                try FileManager.default.removeItem(at: dstURL)
            }
            try FileManager.default.copyItem(at: srcURL, to: dstURL)
        } catch (let error) {
            print("Cannot copy item at \(srcURL) to \(dstURL): \(error)")
            return false
        }
        return true
    }

private let typeKey = 0
private let typeFileName = 1

// file record in db
class DbFileRecord : Decodable, Encodable {
    enum CodingKeys : CodingKey {
        case type
        case value
    }
    
    var type: Int
    var value: String
    
    init(type: Int, value: String) {
        self.type = type
        self.value = value
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        if let v = try? values.decode(Int.self, forKey: .type) {
            type = v
        } else {type = 0}
        if let v = try? values.decode(String.self, forKey: .value) {
            value = v
        } else {value = ""}
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(type, forKey: .type)
        try container.encode(value, forKey: .value)
    }
    
    func asStoredFile(dir: FilesInDir) -> StoredFile? {
        if (type == typeKey) {
            return StoredFile.ByKey(key: value)
        } else if (type == typeFileName){
            return StoredFile.SyncDirFile(filesInDir: dir, filename: value)
        } else {
            return nil
        }
    }
    
    func fileRef(dir: FilesInDir) -> FileRef? {
        return asStoredFile(dir: dir)?.fileRef()
    }
    
    func copyToNewDir(fromDir: FilesInDir, toDir: FilesInDir) -> DbFileRecord? {
     var stored = asStoredFile(dir: fromDir)
        
        if stored is StoredFile.ByKey {
            return self
        } else if stored is StoredFile.SyncDirFile {
            var dirFile = stored as! StoredFile.SyncDirFile
            return copySyncDirFile(dir: toDir, source: dirFile.storedFile, dotExt: dirFile.fileExtension ?? "").asDbStoredFile()
        }
        return nil
    }
}

public typealias File = URL
extension File {
    func exists() -> Bool {
        return FileManager.default.fileExists(atPath: self.path)
    }
    
    func copyTo(to: File) throws {
        try FileManager.default.copyItem(at: self, to: to)
    }
    
    static func createTempFileInDir(prefix: String = "tmp", suffix: String? = nil, contents: Data?) -> File {
        let tmpDir = Config.Shared.tmpDir //.createSubDirectory(name: "tmp") //Current.Shared.loggedUser?.userChartSyncDir
        let des = tmpDir.appendingPathComponent(
            prefix + UUID().uuidString + (suffix != nil ? suffix! : ""), isDirectory: false).path
        if !FileManager.default.createFile(atPath: des, contents: contents) {
            debugPrint("can not create file \(des)")
        }
        return File(fileURLWithPath: des)
    }
}
