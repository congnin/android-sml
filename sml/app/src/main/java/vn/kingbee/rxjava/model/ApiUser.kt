package vn.kingbee.rxjava.model

class ApiUser {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null

    override fun toString(): String {
        return "ApiUser{id=$id, firstName='$firstName', lastName='$lastName'}"
    }
}