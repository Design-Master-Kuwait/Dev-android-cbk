package com.example.newbankingproject.listener

interface AlertDialogInterface {
    fun onYesClick() {}
    fun onNoClick() {}
    fun onSelection(type: String, position: Int) {}
    fun onSortSelection(position: Int) {}
    fun onSelectionLocation(latitude: Double, longitude: Double, address: String) {}
    fun onSubmitPhone(valueOne: String?, valueTwo: String?) {}

}