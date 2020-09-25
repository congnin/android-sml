package com.androidtutz.anushka.ebookshop.di;

import android.app.Application;

import com.androidtutz.anushka.ebookshop.model.EBookShopRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
     EBookShopRepository providesEBookShopRepository(Application application){
         return new EBookShopRepository(application);
     }
}
