package com.androidtutz.anushka.ebookshop.di;

import com.androidtutz.anushka.ebookshop.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class,RepositoryModule.class})
public interface EBookShopComponent {

    void inject(MainActivity mainActivity);
}
