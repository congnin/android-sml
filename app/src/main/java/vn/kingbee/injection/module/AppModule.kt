package vn.kingbee.injection.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import vn.kingbee.application.MyApp
import vn.kingbee.application.appbus.AppBus
import vn.kingbee.application.appbus.AppBusImpl
import vn.kingbee.application.runtime.Runtime
import vn.kingbee.application.runtime.RuntimeImpl
import vn.kingbee.utils.security.ObscuredSharedPreferences
import vn.kingbee.widget.R
import javax.inject.Singleton
import android.preference.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import vn.kingbee.movie.network.NetworkModule
import vn.kingbee.movie.network.NetworkModule.Companion.BASE_KIOSK_URL
import vn.kingbee.widget.BuildConfig
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.net.ssl.*

@Module
class AppModule(private val myApp: MyApp) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return myApp
    }

    @Provides
    @Singleton
    fun provideObscuredSharedPreferences(): SharedPreferences {
        return ObscuredSharedPreferences(
            myApp,
            myApp.getSharedPreferences(
                myApp.getString(vn.kingbee.widget.R.string.app_name), Context.MODE_PRIVATE
            )
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideRuntime(sharedPref: SharedPreferences, gson: Gson): Runtime {
        return RuntimeImpl(sharedPref, gson)
    }

    @Provides
    @Singleton
    fun provideApplicationBus(): AppBus {
        return AppBusImpl()
    }


    @Provides
    @Singleton
    @Named("ByPassSSL")
    fun provideOkHttpClient(): OkHttpClient {
        var client: OkHttpClient? = null

        try {
            val x509TrustManager = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }

            val trustCerts = arrayOfNulls<TrustManager>(1)
            trustCerts[0] = x509TrustManager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            client = builder.retryOnConnectionFailure(true).followRedirects(false).build()
        } catch (e: Exception) {
            Timber.e(e, e.message)
        }

        return client!!
    }

    @Provides
    @Singleton
    @Named("Kiosk")
    fun providesKioskRetrofit(@Named("ByPassSSL") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_KIOSK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}