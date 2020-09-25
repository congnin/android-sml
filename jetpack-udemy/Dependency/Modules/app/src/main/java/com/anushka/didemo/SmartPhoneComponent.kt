package com.anushka.didemo

import dagger.Component

@Component(modules = [MemoryCardModule::class])
interface SmartPhoneComponent {
   fun getSmartPhone() : SmartPhone
}