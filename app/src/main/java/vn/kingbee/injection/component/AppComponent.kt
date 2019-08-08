package vn.kingbee.injection.component

import dagger.Component
import vn.kingbee.injection.module.AppModule
import vn.kingbee.injection.module.RepositoryModule

@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {
}