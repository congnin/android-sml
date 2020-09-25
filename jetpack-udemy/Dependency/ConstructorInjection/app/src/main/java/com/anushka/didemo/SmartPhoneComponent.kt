package com.anushka.didemo

import dagger.Component

@Component
interface SmartPhoneComponent {
   fun getSmartPhone() : SmartPhone
}