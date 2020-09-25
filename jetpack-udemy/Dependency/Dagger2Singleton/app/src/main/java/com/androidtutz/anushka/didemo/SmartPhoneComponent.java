package com.androidtutz.anushka.didemo;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MemoryCardModule.class,NCBatteryModule.class})
public interface SmartPhoneComponent {

   // SmartPhone getSmartPhone();
    void inject(MainActivity mainActivity);

}
