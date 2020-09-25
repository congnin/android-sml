package com.anushka.didemo

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class NCBatteryModule {
    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery):Battery
}