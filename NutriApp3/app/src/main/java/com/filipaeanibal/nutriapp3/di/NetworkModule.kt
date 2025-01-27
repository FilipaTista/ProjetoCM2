package com.filipaeanibal.nutriapp3.di


import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.IngredientInformationApi
import com.filipaeanibal.nutriapp3.util.RandomRecipeApi
import com.filipaeanibal.nutriapp3.util.RecipeDetailsApi
import com.filipaeanibal.nutriapp3.util.RecipeInstructionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RandomRecipeApi {
        return retrofit.create(RandomRecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeDetailsApi(retrofit: Retrofit): RecipeDetailsApi {
        return retrofit.create(RecipeDetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeInstructionsApi(retrofit: Retrofit): RecipeInstructionsApi {
        return retrofit.create(RecipeInstructionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIngredientInformationApi(retrofit: Retrofit): IngredientInformationApi {
        return retrofit.create(IngredientInformationApi::class.java)
    }
}