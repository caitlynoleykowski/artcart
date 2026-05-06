package com.coco0o0o.artcart.core.network.di

import com.coco0o0o.artcart.core.network.NetworkDataSource
import com.coco0o0o.artcart.core.network.retrofit.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetwork): NetworkDataSource
}
