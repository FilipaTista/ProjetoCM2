package com.filipaeanibal.nutriapp3.di


import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.IngredientInformationApi
import com.filipaeanibal.nutriapp3.util.IngredientSearchApi
import com.filipaeanibal.nutriapp3.util.RandomRecipeApi
import com.filipaeanibal.nutriapp3.util.RecipeDetailsApi
import com.filipaeanibal.nutriapp3.util.RecipeInstructionsApi
import com.filipaeanibal.nutriapp3.util.SearchRecipesbyIngredientsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    @Provides
    @Singleton
    fun provideSearchRecipesbyIngredientsApi(retrofit: Retrofit): SearchRecipesbyIngredientsApi {
        return retrofit.create(SearchRecipesbyIngredientsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIngredientSearchApi(retrofit: Retrofit): IngredientSearchApi {
        return retrofit.create(IngredientSearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}

