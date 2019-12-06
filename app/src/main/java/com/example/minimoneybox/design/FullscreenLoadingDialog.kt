package com.example.minimoneybox.design

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.example.minimoneybox.R

class FullscreenLoadingDialog(context: Context) : Dialog(context, R.style.FullscreenLoading) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(R.layout.fullscreen_layout)
    }
}