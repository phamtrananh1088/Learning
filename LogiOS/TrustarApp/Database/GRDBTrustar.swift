//
//  GRDBTrustar.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class GRDBTrustar {
    static let dbVersion: String = "3"
    
    static func removeCurrentDb() {
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first!
        
        let enumerator = FileManager.default.enumerator(atPath: path)
        let filePaths = enumerator?.allObjects as! [String]
        let sqliteFilePaths = filePaths.filter{$0.contains(".sqlite")}
        for sqliteFile in sqliteFilePaths{
            try? FileManager.default.removeItem(at: URL(fileURLWithPath: path).appendingPathComponent(sqliteFile, isDirectory: false))
        }
    }
    
    private let dbName: String
    private let databasePath: String
    private var db: DatabaseQueue?
        
    init(_ dbName: String) {
        self.dbName = dbName

        let path = NSSearchPathForDirectoriesInDomains(
            .documentDirectory, .userDomainMask, true
        ).first!
        databasePath = Resources.pathDb.replacingOccurrences(of: "{0}", with: path + "/" + dbName)
    }
    
    func isDbExisted() -> Bool {
        return FileManager.default.fileExists(atPath: databasePath)
    }
    
    func makeDatabaseQueue() throws -> DatabaseQueue {
        if !isDbExisted() {
            FileManager.default.createFile(atPath: databasePath, contents: nil, attributes: nil)
        }
        
        do {
            db = try DatabaseQueue(path: databasePath)
        } catch {
            debugPrint(error)
        }
        
        return db!
    }
    
    func createTableIfNotExist<T: BaseEntity>(table: T, lstProp: [String: String]!) {
        
        if lstProp == nil || db == nil {
            return
        }
        
        let createTableTemplate = "CREATE TABLE IF NOT EXISTS {0} ({1}, {2} {3})"
        let createColumnTemplate = "{0} {1}"
        let createPrimaryKeyTemplate = "PRIMARY KEY ({0})"
        let foreignKeyTemplate = """
            ,CONSTRAINT "fk_{entity}"
             FOREIGN KEY ("{childColumns}")
             REFERENCES "{entity}"("{parentColumns}")
             ON DELETE CASCADE
        """
        var createColumn: [String] = []
        
        let tableName = T.databaseTableName
        
        for prop in lstProp {
            
            var t = Resources.strEmpty

            if prop.value.lowercased().contains("string") {
                t = TypeColumn.text.text()
            } else if prop.value.lowercased().contains("int") {
                t = TypeColumn.integer.text()
            } else if prop.value.lowercased().contains("decimal")
                        || prop.value.lowercased().contains("bool")
                        || prop.value.lowercased().contains("double"){
                t = TypeColumn.numeric.text()
            } else if prop.value.lowercased().contains("data") {
                t = TypeColumn.blob.text()
            } else {
                t = TypeColumn.json.text()
            }
            
            createColumn.append(createColumnTemplate
                                    .replacingOccurrences(of: "{0}", with: prop.key)
                                    .replacingOccurrences(of: "{1}", with: t))
        }
        
        let lstPrimaryKey = T.primaryKey.lowercased().components(separatedBy: ",")

        let primaryKey = createPrimaryKeyTemplate
            .replacingOccurrences(of: "{0}", with: "\"" + lstPrimaryKey.joined(separator: "\",\"") + "\"")
        
        let foreignKey = T.parentTable.isEmpty
        ? ""
        : foreignKeyTemplate
            .replacingOccurrences(of: "{entity}", with: T.parentTable)
            .replacingOccurrences(of: "{parentColumns}", with: T.parentColumns)
            .replacingOccurrences(of: "{childColumns}", with: T.childColumns)
        
        do {
            try db?.write { ctx in
                    try? ctx.drop(table: tableName)
                    
                    try ctx.execute(sql: createTableTemplate
                                        .replacingOccurrences(of: "{0}", with: tableName)
                                        .replacingOccurrences(of: "{1}", with: createColumn.joined(separator: ","))
                                        .replacingOccurrences(of: "{2}", with: primaryKey)
                                        .replacingOccurrences(of: "{3}", with: foreignKey)
                   )
            }
        } catch {
            debugPrint(error)
        }
    }
}
