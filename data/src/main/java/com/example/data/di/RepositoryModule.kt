package com.example.data.di

import com.example.data.dataSource.DashboardDataSourceImpl
import com.example.data.dataSource.LoginDataSourceImpl
import com.example.domain.repository.DashboardRepository
import com.example.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
/** RepositoryModule is module for repository*/
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginDataSource(loginDataSource: LoginDataSourceImpl): LoginRepository {
        return loginDataSource
    }

    @Provides
    @Singleton
    fun provideDashBoardDataSource(dashboardDataSourceImpl: DashboardDataSourceImpl): DashboardRepository {
        return dashboardDataSourceImpl
    }


}