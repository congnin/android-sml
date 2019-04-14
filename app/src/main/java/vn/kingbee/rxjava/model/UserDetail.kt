package vn.kingbee.rxjava.model

data class UserDetail(var id: Long, var firstName: String, var lastName: String) {
    override fun toString(): String {
        return "UserDetail{id=$id, firstname='$firstName', lastname='$lastName'}"
    }
}