package com.mymovie.Components

class ItemObject {
    var layoutType = 0
        private set
    val id: String? = null
    var no: String? = null
        private set
    var title: String? = null
        private set
    var description: String? = null
        private set
    var duration: String? = null
        private set
    var numberOfQuestion: String? = null
        private set
    private lateinit var isiList: Array<String>

    constructor(LayoutType: Int, No: String?, Title: String?, Description: String?, NumberOfQuestion: String?, Duration: String?) {
        layoutType = LayoutType
        no = No
        title = Title
        description = Description
        numberOfQuestion = NumberOfQuestion
        duration = Duration
    }

    constructor(isi_list: Array<String>) {
        isiList = isi_list
    }

    fun getItem(i: Int): String {
        return isiList[i]
    }
}