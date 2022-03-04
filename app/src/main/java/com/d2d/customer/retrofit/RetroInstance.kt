package com.d2d.customer.retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object {
        //val baseUrl = "https://reqres.in/api/"

          //val baseUrl="http://10.12.12.103:3000/"
         val baseUrl="https://d2dbfhprod.azurewebsites.net"
        fun getRetroInstance(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
            client.addInterceptor(logging)

           return Retrofit.Builder()
               .baseUrl(baseUrl)
               .client(client.build())
               .addConverterFactory(GsonConverterFactory.create())
               .build()
        }
    }
}