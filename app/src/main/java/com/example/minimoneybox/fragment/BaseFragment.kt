package com.example.minimoneybox.fragment

import android.os.Bundle
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}