//
//  TSUITextField.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
//https://stackoverflow.com/questions/58673159/how-to-move-to-next-textfield-in-swiftui
struct TSUITextField: UIViewRepresentable {
    let label: String
    @Binding var text: String
    
    var focusable: Bool = false
    var isSelectAllWhenFocus: Bool = false
    var isSecureTextEntry: Binding<Bool>? = nil
    
    var returnKeyType: UIReturnKeyType = .default
    var autocapitalizationType: UITextAutocapitalizationType = .none
    var keyboardType: UIKeyboardType = .default
    var textContentType: UITextContentType? = nil
    
    var tag: Int? = nil
    var inputAccessoryView: UIToolbar? = nil
    var onCommit: ((String) -> Void)? = nil
    var onChange: ((String) -> Void)? = nil
    var maxLength: Int?
    var regexExp: String?
    var textAlignment: NSTextAlignment = .left
    var foregroundColor: UIColor = .black
    //Create UITextField with customized keyboard button
    func makeUIView(context: Context) -> UITextField {
        let textField = UITextField(frame: .zero)
        textField.delegate = context.coordinator
        textField.placeholder = label
        
        textField.returnKeyType = returnKeyType
        textField.autocapitalizationType = autocapitalizationType
        textField.keyboardType = keyboardType
        textField.isSecureTextEntry = isSecureTextEntry?.wrappedValue ?? false
        textField.textContentType = textContentType
        textField.textAlignment = textAlignment
        if focusable {
            textField.becomeFirstResponder()
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.35, execute: {
                if textField.text != nil && !textField.text!.isEmpty && textField.text != " " {
                    textField.selectedTextRange = textField.textRange(from: textField.beginningOfDocument, to: textField.endOfDocument)
                }
            })
        }
        
        //customize keyboard
        textField.textColor = foregroundColor
        textField.keyboardAppearance = UIKeyboardAppearance.light
        let toolBar = UIToolbar(frame: CGRect(x: 0, y: 0, width: textField.frame.size.width, height: 44))
        toolBar.sizeToFit()
        toolBar.backgroundColor = UIColor.white
        
        //creating flexible space
        let flexibleSpace = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        
        if returnKeyType == .next {
            let doneButtonIcon = UIImage(systemName: "arrow.right.to.line")!.withRenderingMode(.automatic)

            let doneButton = UIBarButtonItem(image: doneButtonIcon.withTintColor(UIColor.white),
                                             style: .plain,
                                             target: self,
                                             action: #selector(textField.doneButtonTapped2(button:)))
            //adding space and button to toolbar
            toolBar.items = [flexibleSpace,doneButton]
            toolBar.setItems([flexibleSpace,doneButton], animated: true)
            textField.inputAccessoryView = toolBar
            
        } else if returnKeyType == .done {
            let doneButtonIcon = UIImage(systemName: "chevron.down")!.withRenderingMode(.automatic)

            let doneButton = UIBarButtonItem(image: doneButtonIcon.withTintColor(UIColor.white),
                                             style: .done,
                                             target: self,
                                             action: #selector(textField.doneButtonTapped2(button:)))
            //adding space and button to toolbar
            toolBar.items = [flexibleSpace,doneButton]
            toolBar.setItems([flexibleSpace,doneButton], animated: true)
            textField.inputAccessoryView = toolBar
        }
        
        if let tag = tag {
            textField.tag = tag
        }
        
        //textField.inputAccessoryView = inputAccessoryView
        textField.addTarget(context.coordinator, action: #selector(Coordinator.textFieldDidChange(_:)), for: .editingChanged)
        
        textField.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        
        return textField
    }
    
    //Just display text on UITextField
    func updateUIView(_ uiView: UITextField, context: Context) {
        uiView.text = text
        uiView.isSecureTextEntry = isSecureTextEntry?.wrappedValue ?? false
        uiView.textColor = foregroundColor
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self, isSelectAllWhenFocus)
    }
    
    //Create delegate for UITextField
    final class Coordinator: NSObject, UITextFieldDelegate {
        let control: TSUITextField
        let isSelectAllWhenFocus: Bool
        
        init(_ control: TSUITextField, _ isSelectAllWhenFocus: Bool) {
            self.control = control
            self.isSelectAllWhenFocus = isSelectAllWhenFocus
        }
        
        func textFieldDidBeginEditing(_ textField: UITextField) {
            if isSelectAllWhenFocus {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35, execute: {
                    if textField.text != nil && !textField.text!.isEmpty && textField.text != " " {
                        textField.selectedTextRange = textField.textRange(from: textField.beginningOfDocument, to: textField.endOfDocument)
                    }
                })
            }
        }
        
        //Handle dismiss keyboard or focus to next textfield
        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            if let next = textField.superview?.superview?.viewWithTag(textField.tag + 1) as? UITextField {
                next.becomeFirstResponder()
            } else {
                textField.resignFirstResponder()
            }
            return false
        }
        
        //validate here
        func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
            //ensure having data
            guard let textFieldText = textField.text,
                  let rangeOfTextToReplace = Range(range, in: textFieldText)
            else {
                return false
            }
            let updatedText = textFieldText.replacingCharacters(in: rangeOfTextToReplace, with: string)
            
            //check max-length
            if let maxLength = control.maxLength {
                return updatedText.count <= maxLength
            }
            
            //check by regular expression
            if let regexExp = control.regexExp {
                let valRs = updatedText.range(of: regexExp, options: .regularExpression) != nil
                if valRs {
                    //accept
                      return true
                } else {
                    //reject
                      return false
                }
            }
            return true
        }
        
        func textFieldDidEndEditing(_ textField: UITextField) {
            control.onCommit?(textField.text ?? String())
            //textField.resignFirstResponder()
            //textField.endEditing(true)
        }
        
        @objc func textFieldDidChange(_ textField: UITextField) {
            control.text = textField.text ?? ""
            control.onChange?(control.text)
        }
    }
}

extension  UITextField {
    
    @objc func doneButtonTapped2(button:UIBarButtonItem) -> Void {
        if self.returnKeyType == .done {
            //Hide keyboard
            self.resignFirstResponder()
        } else if self.returnKeyType == .next {
            //Focus to next UITextField
            _ = self.delegate?.textFieldShouldReturn?(self)
        }
    }
}
