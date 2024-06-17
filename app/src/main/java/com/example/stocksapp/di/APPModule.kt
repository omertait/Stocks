package com.example.stocksapp.di

import android.content.Context
import com.example.stocksapp.data.local.ItemsDatabase
import com.example.stocksapp.data.remote_db.StockService
import com.example.stocksapp.data.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Dagger Hilt module that provides dependencies for the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object APPModule {

    /**
     * Provides the Retrofit instance for making network calls.
     *
     * @param gson The Gson instance used for JSON serialization/deserialization.
     * @return The Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Provides the Gson instance used for JSON serialization/deserialization.
     *
     * @return The Gson instance.
     */
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    /**
     * Provides the StockService instance for making stock-related network calls.
     *
     * @param retrofit The Retrofit instance.
     * @return The StockService instance.
     */
    @Provides
    fun provideStockService(retrofit: Retrofit): StockService =
        retrofit.create(StockService::class.java)

    /**
     * Provides the local ItemsDatabase instance.
     *
     * @param appContext The application context.
     * @return The ItemsDatabase instance.
     */
    @Provides
    fun provideLocalDatabase(@ApplicationContext appContext: Context): ItemsDatabase =
        ItemsDatabase.getDatabase(appContext)

    /**
     * Provides the ItemDao instance from the local database.
     *
     * @param database The ItemsDatabase instance.
     * @return The ItemDao instance.
     */
    @Provides
    @Singleton
    fun provideItemDao(database: ItemsDatabase) = database.itemsDao()

}