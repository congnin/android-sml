package com.anushka.didemo

import dagger.Component

@Component(modules = [MemoryCardModule::class,NCBatteryModule::class])
interface SmartPhoneComponent {

   fun inject(mainActivity: MainActivity)
}

